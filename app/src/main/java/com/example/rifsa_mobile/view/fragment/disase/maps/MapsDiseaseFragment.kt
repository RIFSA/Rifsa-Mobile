package com.example.rifsa_mobile.view.fragment.disase.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentMapsDiseaseBinding
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

//todo maps bug
class MapsDiseaseFragment : Fragment(), OnMapReadyCallback {
    private lateinit var viewModel : LocalViewModel
    private lateinit var binding : FragmentMapsDiseaseBinding

    private lateinit var gMap : GoogleMap

    private var longtitude = 0.0
    private var lattidue = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsDiseaseBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDiseaseMapsBackhome.setOnClickListener {
            findNavController().navigate(
                MapsDiseaseFragmentDirections.actionMapsDiseaseFragmentToDisaseFragment()
            )
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        viewModel.readDiseaseLocal().observe(viewLifecycleOwner){ respon ->
            respon.forEach { loc ->
                showMarker(lattidue,longtitude)
            }
        }
    }



    override fun onMapReady(maps: GoogleMap) {
        gMap = maps

        gMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

        gMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isMapToolbarEnabled = true
            isCompassEnabled = true
            isMyLocationButtonEnabled = true
        }

    }

    private fun showMarker(lattidue : Double,longtidue : Double){
        val currentMarker = LatLng(-7.1001066327587745, 112.46945935192291)
        val diseaseMarker = LatLng(lattidue,longtidue)

        gMap.apply {
            addMarker(MarkerOptions().position(currentMarker).title("Sawah anda"))
            addMarker(MarkerOptions().position(diseaseMarker).title("Penyakit"))

            moveCamera(CameraUpdateFactory.newLatLng(currentMarker))
            animateCamera(CameraUpdateFactory.newLatLngZoom(diseaseMarker,19f))
        }


    }
}