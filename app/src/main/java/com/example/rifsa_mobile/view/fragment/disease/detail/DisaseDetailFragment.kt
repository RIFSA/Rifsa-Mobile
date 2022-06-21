package com.example.rifsa_mobile.view.fragment.disease.detail

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentDisaseDetailBinding
import com.example.rifsa_mobile.model.entity.remote.disease.restapivm.DiseaseResultResponse
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.gms.location.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
class DisaseDetailFragment : Fragment() {
    private lateinit var binding : FragmentDisaseDetailBinding

    private val remoteViewModel : RemoteViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private val authViewModel : UserPrefrencesViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }


    private var randomId = 0
    private var image = ""
    private var isDetail = false


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
                    lifecycleScope.launch {
                        getCurrentLocation()
                    }
                }
                permission[coarseLocation] ?: false ->{
                    lifecycleScope.launch {
                        getCurrentLocation()
                    }
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

        fusedLocation =
            LocationServices.getFusedLocationProviderClient(requireContext())



        try {
//            val detail = DisaseDetailFragmentArgs.fromBundle(requireArguments()).diseaseDetail
//            if (detail != null){
//                showDetailDisease(detail)
//                isDetail = true
//                randomId = detail.idPenyakit
//                binding.btnDiseaseSave.visibility = View.GONE
//            }
        }catch (e : Exception){ }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createLocationRequest()



        if (!isDetail){
            showImageCapture()
            binding.pgdiseaseBar.visibility = View.VISIBLE
            showStatus("Proses")
            lifecycleScope.launch {
                delay(2000)
                postPrediction()
            }
        }



        binding.btnDiseaseSave.setOnClickListener {
            lifecycleScope.launch {
                binding.pgdiseaseBar.visibility = View.VISIBLE
                findNavController().navigate(
                    DisaseDetailFragmentDirections.actionDisaseDetailFragmentToDisaseFragment()
                )
            }
        }

        binding.btnDiseaseBackhome.setOnClickListener {
            findNavController().navigate(
                DisaseDetailFragmentDirections.actionDisaseDetailFragmentToDisaseFragment()
            )
        }
        binding.btnDiseaseComplete.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setTitle("Selesai teratasi")
                setMessage("apakah penyakit telah teratasi ?")
                apply {
                    setPositiveButton("ya") { _, _ ->
                        binding.pgdiseaseBar.visibility = View.VISIBLE
//                        stopAlarm()
//                        deleteDiseaseLocal()
                        deleteDiseaseRemote()
                    }
                    setNegativeButton("tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                }
                create()
                show()
            }
        }

    }


    private fun showImageCapture(){
        image = DisaseDetailFragmentArgs.fromBundle(requireArguments()).photoDisase.toString()
        binding.imgDisaseDetail.setImageURI(image.toUri())

    }


    private fun showDetailDisease(data : DiseaseResultResponse){
        binding.btnDiseaseComplete.visibility = View.VISIBLE
        binding.tvdisasaeDetailIndication.setText(data.indikasi)

        Glide.with(requireContext())
            .load("http://34.101.115.114:5000/${data.url}")
            .into(binding.imgDisaseDetail)

        showDescription(data.indikasi)
        binding.btnDiseaseComplete.visibility = View.VISIBLE
    }


    private fun postPrediction(){
        authViewModel.getUserToken().observe(viewLifecycleOwner){ _ ->


            val currentImage = Utils.uriToFile(image.toUri(),requireContext())
            val typeFile = currentImage.asRequestBody("image/jpg".toMediaTypeOrNull())
            val multiPartFile : MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                currentImage.name,
                typeFile
            )


            lifecycleScope.launch {
                remoteViewModel.postDiseasePrediction(
                    multiPartFile,
                    curLatitude,
                    curLongitude
                )
                    .observe(viewLifecycleOwner){
                        when(it){
                            is FetchResult.Loading->{
                                binding.pgdiseaseBar.visibility = View.VISIBLE
                            }
                            is FetchResult.Success ->{
                                showStatus("Berhasil")
                                binding.tvdisasaeDetailIndication.setText(it.data.data.result)
                                binding.pgdiseaseBar.visibility = View.GONE
                                showDescription(it.data.data.result)
                            }
                            is FetchResult.Error ->{
                                showStatus(it.error)
                            }
                            else -> {}
                        }
                    }
            }
        }

    }



    private fun deleteDiseaseRemote(){
        authViewModel.getUserToken().observe(viewLifecycleOwner){ token ->
            lifecycleScope.launch {
                remoteViewModel.deleteDiseaseRemote(randomId,token).observe(viewLifecycleOwner){
                    when(it){
                        is FetchResult.Loading->{
                            binding.pgdiseaseBar.visibility = View.VISIBLE
                        }
                        is FetchResult.Success->{
                            showStatus(it.data.message)
                            findNavController().navigate(
                                DisaseDetailFragmentDirections.actionDisaseDetailFragmentToDisaseFragment()
                            )
                        }
                        is FetchResult.Error->{
                            showStatus(it.error)
                        }
                    }
                }
            }
        }
    }

    private fun showDescription(search : String){
        val inputStream = resources.openRawResource(R.raw.disease_solustion)
        val jsonString: String = Scanner(inputStream).useDelimiter("\\A").next()

        val jsonObj = JSONObject(jsonString)

        val dataSolustion = jsonObj.getJSONObject("Solusi")
            .getJSONObject(search)["deskripsi"]

        Log.d("Disease",dataSolustion.toString())


        binding.btnDiseaseSave.visibility = View.VISIBLE
        binding.tvdisasaeDetailDescription.text = dataSolustion.toString()
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
                lifecycleScope.launch {
                    getCurrentLocation()
                }
            }
            .addOnFailureListener {
                Log.d(page_key,it.message.toString())
            }

    }

    private fun getCurrentLocation(){
        if (checkPermission(fineLocation) && checkPermission(coarseLocation)){
            fusedLocation.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        curLatitude = location.latitude
                        curLongitude = location.longitude
                        Log.d("disease", curLatitude.toString())
                        binding.tvdisasaeDetailLocation.visibility = View.VISIBLE
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



    private fun showStatus(title: String){
        binding.apply {
            pgdiseaseTitle.text = title
            pgdiseaseBar.visibility = View.GONE
            pgdiseaseTitle.visibility = View.VISIBLE
        }
        Log.d(page_key,title)
    }




    companion object{
        const val page_key = "Disease_detail"
    }


}