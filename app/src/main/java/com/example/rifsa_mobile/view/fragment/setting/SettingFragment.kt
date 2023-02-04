package com.example.rifsa_mobile.view.fragment.setting

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.databinding.FragmentSettingBinding
import com.example.rifsa_mobile.helpers.update.DiseaseUploadWorker
import com.example.rifsa_mobile.helpers.update.FinanceUploadWorker
import com.example.rifsa_mobile.helpers.update.HarvestUploadWorker
import com.example.rifsa_mobile.model.entity.openweatherapi.request.UserLocation
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.gms.location.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {
    private lateinit var binding : FragmentSettingBinding

    private var isTracking = false
    private lateinit var fusedLocation : FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var userFirebaseId = ""
    private val fineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private var uploadedReminderId = (1..1000).random()

    private val viewModel : SettingViewModel by viewModels{
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
        viewModel.getLocationListener().observe(viewLifecycleOwner){ state->
            isTracking = state
            binding.switchLocation.isChecked = state
        }
        viewModel.getFirebaseUserId().observe(viewLifecycleOwner){ id->
            userFirebaseId = id
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
        binding.btnUnggahdata.setOnClickListener {
            setUploadReminder()
        }
    }

    private fun setUploadReminder(){
        DiseaseUploadWorker().setDailyUpload(
            requireContext(),
            uploadedReminderId
        )
        HarvestUploadWorker().setDailyUpload(
            requireContext(),
            uploadedReminderId
        )
        FinanceUploadWorker().setDailyUpload(
            requireContext(),
            uploadedReminderId
        )
    }

    /*
    uploaded offline data
     */
    private fun uploadData(){
        binding.pgbarUnggah.visibility = View.VISIBLE
        viewModel.getDiseaseNotUploaded().observe(viewLifecycleOwner){ data->
            try {
                val notUploaded = data.filter { key->(!key.isUploaded) }
                notUploaded.forEach { value ->
                    Log.d("settingFragment",value.toString())
                    uploadDiseaseImage(value)
                }
                }catch (e : Exception){
                    binding.pgbarUnggah.visibility = View.GONE
                    Log.d("settingFragment",e.message.toString())
                }
            }
    }
    private fun uploadDiseaseImage(data : DiseaseEntity){
        viewModel.insertDiseaseImage(
            name = data.diseaseId,
            fileUri = data.imageUrl.toUri(),
            userId = userFirebaseId
        )
            .addOnSuccessListener {
                it.storage.downloadUrl
                    .addOnSuccessListener { imgUrl ->
                        lifecycleScope.launch {
                            updateDiseaseLocal(imgUrl,data)
                        }
                    }
                    .addOnFailureListener { e ->
                        binding.pgbarUnggah.visibility = View.GONE
                        Log.d("settingFragment",e.message.toString())
                    }
            }
            .addOnFailureListener { e ->
                binding.pgbarUnggah.visibility = View.GONE
                Log.d("settingFragment",e.message.toString())
            }
    }

    private suspend fun updateDiseaseLocal(url : Uri,data : DiseaseEntity){
        delay(5000)
        viewModel.updateDiseaseUpload(url,data.diseaseId)
        uploadDiseaseData(data)
    }

    private suspend fun uploadDiseaseData(data : DiseaseEntity){
        delay(8000)
        try {
            viewModel.insertDiseaseRemote(
                data,
                userFirebaseId
            ).addOnSuccessListener {
                showMessage("berhasil terunggah")
            }.addOnFailureListener {
                showMessage("gagal menggungah")
                Log.d("settingFragment",it.message.toString())
            }
        }catch (e : Exception){
            showMessage("gagal menggungah")
            Log.d("settingFragment",e.message.toString())
        }
    }

    /*
    Get Location
     */
    private fun setUpdatelocation(){
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations){
                    Log.d("location",location.latitude.toString())
                    saveLocation(
                        UserLocation(
                            location = null,
                            latitude = location.latitude,
                            longtitude = location.longitude
                        )
                    )
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
                        saveLocation(
                            UserLocation(
                                location = null,
                                latitude = location.latitude,
                                longtitude = location.longitude
                            )
                        )
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
    private fun saveLocation(request: UserLocation){
        lifecycleScope.launch {
            viewModel.saveLocation(request)
        }
    }

    private fun showMessage(title : String){
        binding.pgbarUnggah.visibility = View.GONE
        binding.tvNotifUpload.apply {
            visibility = View.VISIBLE
            text = title
            lifecycleScope.launch {
                delay(3000)
                visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isTracking){
            setUpdatelocation()
        }
    }

}
