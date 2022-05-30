package com.example.rifsa_mobile.view.fragment.disase.maps

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentMapsDiseaseBinding
import com.example.rifsa_mobile.utils.Utils.vectorToBitmap
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

//todo maps bug
class MapsDiseaseFragment : Fragment(), OnMapReadyCallback{
    private lateinit var viewModel : LocalViewModel
    private lateinit var binding : FragmentMapsDiseaseBinding

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

        viewModel.readDiseaseLocal().observe(viewLifecycleOwner){ respon ->
            respon.forEach { loc ->
                showMarker(loc.latitude,loc.longitude,loc.indication)
            }
        }

        // directly go to owned field position
        binding.textView34.setOnClickListener {
            gMap.apply {
                moveCamera(CameraUpdateFactory.newLatLng(
                    dummyMarker))
                animateCamera(CameraUpdateFactory.newLatLngZoom(
                    dummyMarker, 19f))
            }
        }

        return binding.root
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
    }

    private fun showMarker(lattidue : Double,longtidue : Double,title : String) {
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
                .position(LatLng(lattidue, longtidue)).title(title)
            )

            setOnMapClickListener {

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
                        showStatus("ladang")
                    }else{
                        showStatus("lokasi gagal")
                    }
                }
                .addOnFailureListener {
                    showStatus(it.message.toString())
                }
        }else{
            requestPermissionLaunch.launch(fineLocation)
        }
    }

    private fun showStatus(title: String){
        binding.textView34.text = title
    }
}