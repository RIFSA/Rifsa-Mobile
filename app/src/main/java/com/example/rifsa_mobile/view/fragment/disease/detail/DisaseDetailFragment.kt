package com.example.rifsa_mobile.view.fragment.disease.detail

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentDisaseDetailBinding
import com.example.rifsa_mobile.model.entity.remote.disease.restapivm.DiseaseResultResponse
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseFirebaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseTreatmentEntity
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.utils.prediction.DiseasePrediction
import com.example.rifsa_mobile.view.fragment.disease.adapter.DiseaseMiscRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.gms.location.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class DisaseDetailFragment : Fragment() {
    private lateinit var binding : FragmentDisaseDetailBinding

    private val remoteViewModel : RemoteViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private val authViewModel : UserPrefrencesViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }


    private lateinit var classification : DiseasePrediction
    private var solustionList = ArrayList<String>()

    private var randomId = 0
    private var image = ""
    private lateinit var imageBitmap  : Bitmap
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
        classification = DiseasePrediction(requireContext())
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
                predictionLocal()
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

        val bitmapImage = MediaStore.Images.Media.getBitmap(requireContext().contentResolver,image.toUri())

        imageBitmap = bitmapImage
        binding.imgDisaseDetail.setImageBitmap(bitmapImage)
    }


    private fun predictionLocal(){
        classification
            .initPrediction(imageBitmap)
            .addOnSuccessListener { result ->
                binding.tvdisasaeDetailIndication.setText(result.name)
                showDiseaseInformation(result.id)
            }
    }


    private fun showDiseaseInformation(id : Int){
        remoteViewModel.getDiseaseInformation(id.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(DiseaseFirebaseEntity::class.java)
                    if (data != null) {
                        binding.tvdisasaeDetailDescription.text = data.Cause
                        binding.tvdisasaeDetailIndecation.text = data.Indetfy
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        showTreatment(id.toString(),"Treatment")
    }

    private fun showTreatment(id: String,parent : String){
        remoteViewModel.getDiseaseInformationMisc(id,parent)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        solustionList.add(it.value.toString())
                        showListTreament(solustionList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun showListTreament(data : List<String>){
        val adapter = DiseaseMiscRecyclerViewAdapter(data)
        val recyclerView = binding.recviewDiseaseTreatment
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
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