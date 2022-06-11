package com.example.rifsa_mobile.view.fragment.disease.maps

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentMapsDiseaseBinding
import com.example.rifsa_mobile.model.entity.remote.disease.DiseaseResultDataResponse
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.utils.Utils.vectorToBitmap
import com.example.rifsa_mobile.view.fragment.inventory.insert.InvetoryInsertFragmentDirections
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

//todo maps bug
class MapsDiseaseFragment : Fragment(), OnMapReadyCallback{
    private lateinit var viewModel : LocalViewModel
    private lateinit var binding : FragmentMapsDiseaseBinding


    private val remoteViewModel : RemoteViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private val authViewModel : UserPrefrencesViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var gMap : GoogleMap


    private var fineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val dummyMarker = LatLng(-7.1001066327587745, 112.46945935192291)

    private var requestPermissionLaunch =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){ getCurrentLocation() }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsDiseaseBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        authViewModel.getUserToken().observe(viewLifecycleOwner){ token ->
            showDiseaseMarker(token)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDiseaseMapsBackhome.setOnClickListener {
            findNavController().navigate(
                MapsDiseaseFragmentDirections.actionMapsDiseaseFragmentToDisaseFragment()
            )
        }
    }

    private fun showDiseaseMarker(token : String){
        lifecycleScope.launch {
            remoteViewModel.getDiseaseRemote(token).observe(viewLifecycleOwner){ respon ->
                when(respon){
                    is FetchResult.Loading->{}
                    is FetchResult.Success->{
                        respon.data.DiseaseResultDataResponse.forEach { loc->
                            showMarker(
                                loc.latitude,
                                loc.longitude,
                                loc.indikasi,
                                loc.idPenyakit.toString()
                            )
                        }
                    }
                    is FetchResult.Error->{}
                    else -> {}
                }
            }
        }
    }


    override fun onMapReady(maps: GoogleMap) {
        gMap = maps
        getCurrentLocation()
        gMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        gMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isMapToolbarEnabled = true
            isCompassEnabled = true
            isMyLocationButtonEnabled = true
        }

        // directly go to owned field position
        gMap.apply {
            addMarker(MarkerOptions()
                .position(dummyMarker)
                .title("Sawah anda")
                .icon(vectorToBitmap(
                    R.drawable.ic_own_field,
                    Color.parseColor("#56CCF2")
                    ,requireContext())
                )
            )
            moveCamera(CameraUpdateFactory.newLatLng(
                dummyMarker))
            animateCamera(CameraUpdateFactory.newLatLngZoom(
                dummyMarker, 19f))
            setOnMapClickListener {
                setOwnedField(it)
            }

        }
    }

    private fun showMarker(lattidue : Double,longtidue : Double,title : String,id : String) {
        if (lattidue != 0.0){
            gMap.apply {
                addMarker(MarkerOptions()
                    .position(dummyMarker).title("Sawah anda")
                    .icon(vectorToBitmap(
                        R.drawable.ic_own_field,
                        Color.parseColor("#56CCF2")
                        ,requireContext())
                    )
                )
                addMarker(MarkerOptions()
                    .position(LatLng(lattidue, longtidue))
                    .snippet(id)
                    .title(title)
                )
                setOnInfoWindowClickListener {
                    detailDisease(it.snippet!!.toInt())
                }
            }
        }
    }

    private fun detailDisease(id : Int){
        authViewModel.getUserToken().observe(viewLifecycleOwner){ token ->
            lifecycleScope.launch {
                remoteViewModel.getDiseaseRemoteById(token,id).observe(viewLifecycleOwner){
                    when(it){
                        is FetchResult.Loading->{

                        }
                        is FetchResult.Success->{
                            Log.d("disease",it.data.message)
                            findNavController().navigate(MapsDiseaseFragmentDirections
                                .actionMapsDiseaseFragmentToDisaseDetailFragment(
                                    null,
                                    it.data.DiseasePostDataResponse
                                ))
                        }
                        is FetchResult.Error->{
                            Log.d("disease",it.error)
                        }
                        else -> {}
                    }

                }
            }
        }
    }

    private fun getCurrentLocation(){
        if (ContextCompat.checkSelfPermission(requireContext(),fineLocation) ==
             PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { maps ->
                    if (maps != null){
                        gMap.isMyLocationEnabled = true
                        gMap.moveCamera(CameraUpdateFactory.newLatLng(
                            LatLng(maps.latitude,maps.longitude)
                        ))
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            LatLng(maps.latitude,maps.longitude),19f
                        ))
                    }
                }
                .addOnFailureListener {
                    showStatus(it.message.toString())
                }
        }else{
            requestPermissionLaunch.launch(fineLocation)
        }
    }

    private fun setOwnedField(data : LatLng){
        AlertDialog.Builder(requireActivity()).apply {
            setTitle("Lokasi lahan")
            setMessage("atur sebagai titik lahan ?")
            apply {
                setPositiveButton("ya") { _, _ ->
                    // User clicked OK button
                }
                setNegativeButton("tidak") { dialog, _ ->
                    dialog.dismiss()
                }
            }
            create()
            show()
        }
    }

    private fun showStatus(title: String){
        binding.textView34.text = title
    }



}