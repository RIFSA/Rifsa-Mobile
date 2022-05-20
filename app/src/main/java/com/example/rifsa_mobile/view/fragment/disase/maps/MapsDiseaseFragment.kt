package com.example.rifsa_mobile.view.fragment.disase.maps

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsDiseaseFragment : Fragment() {
    private lateinit var viewModel : LocalViewModel
    private lateinit var binding : FragmentMapsDiseaseBinding


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
                        addMarker(MarkerOptions().position(dummyMarker).title("Sawah anda"))
                        addMarker(MarkerOptions().position(diseaseMarker).title("penyakit"))
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