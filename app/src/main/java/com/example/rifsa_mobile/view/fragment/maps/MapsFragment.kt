package com.example.rifsa_mobile.view.fragment.maps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentMapsBinding
import com.example.rifsa_mobile.model.entity.preferences.LastLocationPref
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
import kotlinx.coroutines.launch


class MapsFragment : Fragment(), OnMapReadyCallback{

    private lateinit var binding : FragmentMapsBinding

    private val remoteViewModel : RemoteViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private val authViewModel : MapsFragmentViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var googleMap : GoogleMap
    private var diseaseList = ArrayList<DiseaseEntity>()

    private var fineLocation = Manifest.permission.ACCESS_FINE_LOCATION

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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            requireContext()
        )
        setHasOptionsMenu(true)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        val mapType = MapsFragmentArgs.fromBundle(requireArguments()).maptype

        authViewModel.apply {
            when(mapType){
                DisaseFragment.map_key ->{
                    getUserIdKey().observe(viewLifecycleOwner){getDiseaseData(it)}
                    binding.tvDiseaseMapsTitle.text = "Peta persebaran penyakit"
                }
                ProfileFragment.map_key ->{
                    getUserIdKey().observe(viewLifecycleOwner){getFarmingData(it)}
                    binding.tvDiseaseMapsTitle.text = "Ladang pertanian"
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.btnDiseaseMapsBackhome.setOnClickListener {
            findNavController().navigate(
                MapsFragmentDirections.actionMapsDiseaseFragmentToDisaseFragment()
            )
        }

        binding.spinTypeMap.onItemSelectedListener = object
            :AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0->{
                        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                        binding.tvDiseaseMapsTitle.setTextColor(Color.BLACK)
                        binding.btnDiseaseMapsBackhome.setBackgroundResource(R.drawable.ic_back)
                        true
                    }
                    1->{
                        googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                        binding.tvDiseaseMapsTitle.setTextColor(Color.WHITE)
                        binding.btnDiseaseMapsBackhome.setBackgroundResource(R.drawable.ic_back_white)
                        true
                    }
                    2->{
                        googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        binding.tvDiseaseMapsTitle.setTextColor(Color.BLACK)
                        binding.btnDiseaseMapsBackhome.setBackgroundResource(R.drawable.ic_back)
                        true
                    }
                    3->{
                        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                        binding.tvDiseaseMapsTitle.setTextColor(Color.WHITE)
                        binding.btnDiseaseMapsBackhome.setBackgroundResource(R.drawable.ic_back_white)
                        true
                    }
                    else->{
                        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                        binding.tvDiseaseMapsTitle.setTextColor(Color.BLACK)
                        binding.btnDiseaseMapsBackhome.setBackgroundResource(R.drawable.ic_back)
                        true
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    override fun onMapReady(maps: GoogleMap) {
        googleMap = maps
        getCurrentLocation()

        //check location
        setupMapLocation()

        //enable gmaps button
        googleMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isMapToolbarEnabled = true
            isCompassEnabled = true
        }
    }

    private fun setupMapLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true

        } else {
            requestPermissionLaunch.launch(fineLocation)
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

    private fun showDiseaseMarker(
        lattidue : Double,
        longtidue : Double,
        title : String,
        id : String
    ) {
        if (lattidue != 0.0){
            googleMap.apply {
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
        googleMap.apply {
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
        //fetch disease record from firebase
        authViewModel.getUserIdKey().observe(viewLifecycleOwner){userId->
            remoteViewModel.readDiseaseList(userId).addValueEventListener(object
                : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { child ->
                        val data = child.child(id).getValue(DiseaseEntity::class.java)
                        if (data != null){
                            findNavController().navigate(
                                MapsFragmentDirections
                                    .actionMapsDiseaseFragmentToDisaseDetailFragment(
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
                        //save last position as reference
                        directToLastPosition(
                            langtitude = maps.longitude,
                            lattidue = maps.latitude
                        )
                        lifecycleScope.launch {
                            authViewModel.saveLastLocation(LastLocationPref(
                                langitude = maps.longitude,
                                lattidue = maps.latitude
                            ))
                        }
                    }else{
                        //direct to last position if gps off
                        authViewModel.getUserLastLocation().observe(viewLifecycleOwner){coordinate->
                            directToLastPosition(
                                langtitude = coordinate.langitude,
                                lattidue = coordinate.lattidue
                            )
                        }
                    }
                }
                .addOnFailureListener {
                    showStatus(it.message.toString())
                }
        }else{
            requestPermissionLaunch.launch(fineLocation)
        }
    }

    private fun directToLastPosition(
        langtitude : Double,
        lattidue: Double
    ){
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(
            LatLng(lattidue,langtitude)
        ))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
            LatLng(lattidue,langtitude),19f
        ))
    }


    private fun showStatus(title: String){
        binding.tvMapsTitle.visibility = View.VISIBLE
        binding.tvMapsTitle.text = title
    }
}