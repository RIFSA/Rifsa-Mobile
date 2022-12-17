package com.example.rifsa_mobile.view.fragment.setting

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.databinding.FragmentSettingBinding
import com.example.rifsa_mobile.model.entity.openweatherapi.request.WeatherRequest
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {
    private lateinit var binding : FragmentSettingBinding

    private var isTracking = false
    private lateinit var fusedLocation : FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

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
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        fusedLocation = LocationServices.getFusedLocationProviderClient(
            requireContext()
        )
        viewModel.getLocationListener().observe(viewLifecycleOwner){
            isTracking = it
            binding.switchLocation.isChecked = it
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchLocation.apply {
            setOnCheckedChangeListener { _, checked ->
                if(checked){
                    createLocationRequest()
                    setUpdatelocation()
                    locationListener(true)
                }else{
                    stopTracking()
                    locationListener(false)
                }
            }
        }

        binding.btnSettingBackhome.setOnClickListener {
            findNavController().navigate(
                SettingFragmentDirections.actionSettingFragmentToProfileFragment()
            )
        }
    }

    private fun setUpdatelocation(){
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations){
                    Log.d("location",location.latitude.toString())
                    saveLocation(
                        WeatherRequest(
                            location = null,
                            latitude = location.latitude,
                            longtitude = location.longitude
                    ))
                }
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
            fusedLocation.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            fusedLocation.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        saveLocation(WeatherRequest(
                            location = null,
                            latitude = location.latitude,
                            longtitude = location.longitude
                        ))
                    }
                }
                .addOnFailureListener {
                    Log.d("settingFragment",it.message.toString())
                }
        }
    }

    private fun stopTracking(){
        fusedLocation.removeLocationUpdates(locationCallback)
    }
    private fun saveLocation(request: WeatherRequest){
        lifecycleScope.launch {
            viewModel.saveLocation(request)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isTracking){
            setUpdatelocation()
        }
    }

}
