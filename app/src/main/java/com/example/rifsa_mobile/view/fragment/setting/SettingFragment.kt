package com.example.rifsa_mobile.view.fragment.setting

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.rifsa_mobile.databinding.FragmentSettingBinding
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {
    private lateinit var binding : FragmentSettingBinding
    private var currentLatitude = 0F
    private var currentLongtitude = 0F

    private lateinit var fusedLocation : FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private val fineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION

    private val viewModel : SettingFragmentViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    private fun checkPermission(permission : String): Boolean{
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        fusedLocation = LocationServices.getFusedLocationProviderClient(
            requireContext()
        )
        viewModel.getLocationListener().observe(viewLifecycleOwner){
            binding.switchLocation.isChecked = it
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSettingBackhome.setOnClickListener {

        }
        binding.switchLocation.setOnCheckedChangeListener { _, checked ->
            if(checked){
               createLocationRequest()
               locationListener(true)
            }else{
                //location worker off
                locationListener(false)
                Log.d("check","off")
            }
        }
    }

    private fun locationListener(condition : Boolean){
        lifecycleScope.launch{
            viewModel.saveLocationListener(condition)
        }
    }
    private fun createLocationRequest(){
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client : SettingsClient = LocationServices.getSettingsClient(requireContext())
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                lifecycleScope.launch {
                    getCurrentLocation()
                }
            }
            .addOnFailureListener {
                Log.d("settingFragment",it.message.toString())
            }

    }

    private fun getCurrentLocation(){
        if (checkPermission(fineLocation) && checkPermission(coarseLocation)){
            fusedLocation.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null){
                        Log.d("location",location.latitude.toString())
                    }
                }
                .addOnFailureListener {

                }
        }
    }
}