package com.example.rifsa_mobile.view.fragment.maps

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentMapsBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.FieldDetailEntity
import com.example.rifsa_mobile.view.fragment.disease.diseasefragment.DisaseFragment
import com.example.rifsa_mobile.view.fragment.profile.ProfileFragment
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class MapsFragment : Fragment(), OnMapReadyCallback{

    private lateinit var binding : FragmentMapsBinding

    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var gMap : GoogleMap
    private var diseaseList = ArrayList<DiseaseEntity>()

    private var fineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION

    private var requestPermissionLaunch =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){ getCurrentLocation() }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        val mapType = MapsFragmentArgs.fromBundle(requireArguments()).maptype

        authViewModel.apply {
            when(mapType){
                DisaseFragment.map_key ->{
                    getUserId().observe(viewLifecycleOwner){getDiseaseData(it)}
                    binding.tvDiseaseMapsTitle.text = "Peta persebaran penyakit"
                }
                ProfileFragment.map_key ->{
                    getUserId().observe(viewLifecycleOwner){getFarmingData(it)}
                    binding.tvDiseaseMapsTitle.text = "Ladang pertanian"
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDiseaseMapsBackhome.setOnClickListener {
            findNavController().navigate(
                MapsFragmentDirections.actionMapsDiseaseFragmentToDisaseFragment()
            )
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

    }
    private fun getDiseaseData(userId : String){
        remoteViewModel.readDiseaseList(userId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { child ->
                    child.children.forEach { main ->
                        val data = main.getValue(DiseaseEntity::class.java)
                        if (data != null) {
                            diseaseList.add(data)
                            diseaseList.forEach {
                                showDiseaseMarker(
                                    it.latitude.toDouble(),
                                    it.longitude.toDouble(),
                                    it.nameDisease,
                                    it.diseaseId
                                )
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
               showStatus(error.message)
            }
        })
    }

    private fun showDiseaseMarker(lattidue : Double, longtidue : Double, title : String, id : String) {
        if (lattidue != 0.0){
            gMap.apply {
                addMarker(MarkerOptions()
                    .position(LatLng(lattidue, longtidue))
                    .snippet(id)
                    .title(title)
                )
                setOnInfoWindowClickListener {
                    detailDisease(it.snippet!!.toString())
                }
                moveCamera(CameraUpdateFactory.newLatLng(LatLng(lattidue, longtidue)))
                animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lattidue, longtidue), 19f))
            }
        }
    }

    private fun getFarmingData(userId : String){
        remoteViewModel.readFarming(userId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(FieldDetailEntity::class.java)
                if (data != null) {
                    showFarmingField(data)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                showStatus(error.message)
            }
        })
    }

    private fun showFarmingField(data : FieldDetailEntity){
        val location = LatLng(data.longitude, data.latitude)
        gMap.apply {
            addMarker(MarkerOptions()
                .position(location)
                .snippet(data.idField)
                .title("Ladang anda")
            )

            setOnInfoWindowClickListener {
                findNavController().navigate(
                    MapsFragmentDirections.actionMapsDiseaseFragmentToFieldDetailFragment(
                        data,"",""
                    )
                )
            }
            moveCamera(CameraUpdateFactory.newLatLng(location))
            animateCamera(CameraUpdateFactory.newLatLngZoom(location, 19f))
        }
    }


    private fun detailDisease(id : String){
        authViewModel.getUserId().observe(viewLifecycleOwner){userId->
            remoteViewModel.readDiseaseList(userId).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { child ->
                        val data = child.child(id).getValue(DiseaseEntity::class.java)
                        if (data != null){
                            findNavController().navigate(
                                MapsFragmentDirections.actionMapsDiseaseFragmentToDisaseDetailFragment(
                                    data
                                )
                            )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showStatus(error.message)
                }
            })
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



    private fun showStatus(title: String){
        binding.tvMapsTitle.visibility = View.VISIBLE
        binding.tvMapsTitle.text = title
    }



}