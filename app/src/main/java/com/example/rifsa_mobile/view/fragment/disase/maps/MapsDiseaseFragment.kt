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
class MapsDiseaseFragment : Fragment(){
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

        showDiseaseMaps()

    }


    private fun showDiseaseMaps(){
        val callback = OnMapReadyCallback { googleMap ->
            viewModel.readDiseaseLocal().observe(viewLifecycleOwner){ respon ->
                respon.forEach { loc ->
                    val dummyMarker = LatLng(-7.1001066327587745, 112.46945935192291)
                    val diseaseMarker = LatLng(loc.latitude,loc.longitude)
                    googleMap.apply {
                        uiSettings.isZoomControlsEnabled = true
                        uiSettings.isMapToolbarEnabled = true
                        uiSettings.isCompassEnabled = true
                        uiSettings.isMyLocationButtonEnabled = true
                        addMarker(MarkerOptions().position(dummyMarker).title("Sawah anda"))
                        addMarker(MarkerOptions().position(diseaseMarker).title("Penyakit"))
                        moveCamera(CameraUpdateFactory.newLatLng(dummyMarker))
                        animateCamera(CameraUpdateFactory.newLatLngZoom(dummyMarker,19f))
                        mapType = GoogleMap.MAP_TYPE_SATELLITE
                    }
                }

            }
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}