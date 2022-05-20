package com.example.rifsa_mobile.view.fragment.disase.detail

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.databinding.FragmentDisaseDetailBinding
import com.example.rifsa_mobile.model.entity.disase.Disease
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.google.android.gms.location.*
import java.time.LocalDate
import java.util.concurrent.TimeUnit


class DisaseDetailFragment : Fragment() {
    private lateinit var binding : FragmentDisaseDetailBinding
    private lateinit var viewModel: LocalViewModel


    private var randomId = Utils.randomId()
    private var image = ""
    private var isDetail = false
    private var detailId = ""
    private var sortId = 0

    private var curLatitude = 0.0
    private var curLongitude = 0.0

    private val fineLocation =
        android.Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocation =
        android.Manifest.permission.ACCESS_COARSE_LOCATION

    private lateinit var fusedLocation :
            FusedLocationProviderClient

    private lateinit var locationRequest :
            LocationRequest

    //launch after permit granted
    private var requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){ permission ->
            when{
                permission[fineLocation] ?: false -> {
                    getCurrentLocation()
                }
                permission[coarseLocation] ?: false ->{
                    getCurrentLocation()
                }
                else ->{}
            }
        }

    //launch and check permission
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
        binding = FragmentDisaseDetailBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())

        fusedLocation =
            LocationServices.getFusedLocationProviderClient(requireContext())

        createLocationRequest()

        try {
            showImage()
            val detail = DisaseDetailFragmentArgs.fromBundle(requireArguments()).diseaseDetail
            if (detail != null){
                isDetail = true
                randomId = detail.id_disease
                sortId = detail.id_sort
            }
        }catch (e : Exception){ }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnHarvestSave.setOnClickListener {
            insertDiseaseLocal()
        }

        binding.btnHarvestdetailBackhome.setOnClickListener {
            findNavController().navigate(
                DisaseDetailFragmentDirections.actionDisaseDetailFragmentToDisaseFragment()
            )
        }
    }

    private fun showImage(){
        image = DisaseDetailFragmentArgs.fromBundle(requireArguments()).photoDisase.toString()
        binding.imgDisaseDetail.setImageURI(image.toUri())
    }

    private fun insertDiseaseLocal(){
        val date = LocalDate.now().toString()

        val tempInsert = Disease(
            sortId,
            randomId,
            "test peyakit",
            binding.tvdisasaeDetailIndication.text.toString(),
            image,
            date,
            curLatitude,
            curLongitude,
            binding.tvdisasaeDetailDescription.text.toString(),
            true,
            false
        )


        try {
            viewModel.insertDiseaseLocal(tempInsert)
            showToast("Berhasil disimpan")
            findNavController().navigate(
                DisaseDetailFragmentDirections.actionDisaseDetailFragmentToDisaseFragment()
            )
        }catch (e : Exception){
            showToast(e.message.toString())
        }
    }


    // Location Request
    private fun createLocationRequest(){
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getCurrentLocation()
            }
            .addOnFailureListener {
                Log.d(page_key,it.message.toString())
            }

    }

    private fun getCurrentLocation(){
        if (checkPermission(fineLocation) && checkPermission(coarseLocation)){
            fusedLocation.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null){
                        curLatitude = location.latitude
                        curLongitude = location.longitude
                        Log.d(page_key,curLatitude.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d(page_key,it.message.toString())
                }
        }else{
            requestPermissionLauncher.launch(arrayOf(
                fineLocation,
                coarseLocation
            ))
        }
    }


    private fun showToast(title : String){
        Toast.makeText(requireContext(),title, Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val page_key = "Disease_detail"
    }


}