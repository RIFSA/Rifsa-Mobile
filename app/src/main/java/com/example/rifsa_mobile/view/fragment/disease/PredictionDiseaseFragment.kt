package com.example.rifsa_mobile.view.fragment.disease

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.databinding.FragmentPredictionDiseaseBinding
import com.example.rifsa_mobile.helpers.diseasedetection.DiseasePrediction
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseDetailEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.view.fragment.disease.adapter.DiseaseMiscRecyclerViewAdapter
import com.example.rifsa_mobile.view.fragment.disease.detail.DiseaseDetailFragment
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.gms.location.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class PredictionDiseaseFragment : Fragment() {
    private lateinit var binding : FragmentPredictionDiseaseBinding
    private val viewModel : DiseaseDetailViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private val authViewModel : UserPrefrencesViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var classification : DiseasePrediction
    private var solutionList = ArrayList<String>()
    private var indicationList = ArrayList<String>()

    private var currentDate = LocalDate.now().toString()
    private var diseaseId = UUID.randomUUID().toString()
    private var diseaseIndex = 0
    private var indicationName = ""
    private var isUploaded = false
    private var alarmId = (1..1000).random()

    private lateinit var firebaseUserId : String
    private lateinit var imageUri : Uri
    private lateinit var imageBitmap  : Bitmap

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
    ): View? {
        binding = FragmentPredictionDiseaseBinding.inflate(layoutInflater)
        classification = DiseasePrediction(requireContext())
        fusedLocation = LocationServices.getFusedLocationProviderClient(
            requireContext()
        )
        authViewModel.getUserId().observe(viewLifecycleOwner){userId ->
            firebaseUserId = userId
        }

        createLocationRequest()
        showDiseaseDetail()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveDisease2.setOnClickListener {
            uploadDiseaseImage()
            insertDiseaseLocal()
        }
    }

    private fun showDiseaseDetail(){
        imageUri = PredictionDiseaseFragmentArgs.fromBundle(
            requireArguments()
        ).diseaseImage.toUri()

        val bitmapImage = MediaStore.Images.Media.getBitmap(
            requireContext().contentResolver,imageUri
        )
        imageBitmap = bitmapImage
        binding.imgDisaseDetail2.setImageBitmap(bitmapImage)

        indicationName = PredictionDiseaseFragmentArgs.fromBundle(
            requireArguments()
        ).diseaseName
        binding.tvdisasaeDetailIndication2.text = indicationName

        diseaseIndex = PredictionDiseaseFragmentArgs.fromBundle(
            requireArguments()
        ).diseaseId
        showDiseaseInformation(diseaseIndex)
    }

    private fun uploadDiseaseImage(){
        viewModel.uploadDiseaseImage(diseaseId,imageUri,firebaseUserId)
            .addOnSuccessListener {
                it.storage.downloadUrl
                    .addOnSuccessListener { imgUrl ->
                        insertDiseaseRemote(imgUrl)
                        imageUri = imgUrl
                    }
                    .addOnFailureListener { respon ->
                        showStatus(respon.message.toString())
                        isUploaded = false
                    }
            }
            .addOnFailureListener {
                showStatus(it.message.toString())
                isUploaded = false
            }
    }

    private fun insertDiseaseRemote(imageUrl : Uri){
        val tempData = DiseaseEntity(
            localId = 0,
            diseaseId = diseaseId,
            reminderID = alarmId,
            firebaseUserId = firebaseUserId,
            nameDisease = indicationName,
            indexDisease = diseaseIndex,
            dateDisease = currentDate,
            latitude = curLatitude.toString(),
            longitude = curLongitude.toString(),
            imageUrl = imageUrl.toString(),
            isUploaded = isUploaded
        )

        viewModel.saveDisease(tempData,firebaseUserId)
            .addOnSuccessListener {
                showStatus("penyakit tersimpan")
                isUploaded = true
            }
            .addOnFailureListener {
                showStatus(it.message.toString())
                isUploaded = false
            }
    }

    private fun insertDiseaseLocal(){
        lifecycleScope.launch {
            delay(2000)
            val localData = DiseaseEntity(
                localId = 0,
                diseaseId = diseaseId,
                firebaseUserId = firebaseUserId,
                nameDisease = indicationName,
                indexDisease = diseaseIndex,
                dateDisease = currentDate,
                latitude = curLatitude.toString(),
                longitude = curLongitude.toString(),
                imageUrl = imageUri.toString(),
                reminderID = alarmId,
                isUploaded = isUploaded
            )

            try {
                viewModel.insertDiseaseLocal(localData)
                showStatus("tersimpan lokal")
                //add setReminder
            }catch (e : Exception){
                showStatus("gagal menyimpan")
                Log.d("diseaseDetail",e.toString())
            }
        }
    }

    private fun showDiseaseInformation(id : Int){
        viewModel.getDiseaseInformation(id.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(DiseaseDetailEntity::class.java)
                    if (data != null) {
                        binding.tvdisasaeDetailDescription3.text = data.Cause
                        binding.pgdiseaseBar2.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showStatus(error.message)
                }
            })
        showTreatment(id.toString(),"Treatment")
        showIndication(id.toString(),"Indication")
    }

    private fun showTreatment(id: String, parentDisease : String){
        viewModel.getDiseaseInformationMisc(id,parentDisease)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        solutionList.add(it.value.toString())
                        val adapter = DiseaseMiscRecyclerViewAdapter(solutionList)
                        val recyclerView = binding.recvTreatment.recyclerviewTreatment
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showStatus(error.message)
                }
            })
    }

    private fun showIndication(id: String, parentInformation : String){
        viewModel.getDiseaseInformationMisc(id,parentInformation)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        indicationList.add(it.value.toString())
                        val adapter = DiseaseMiscRecyclerViewAdapter(indicationList)
                        val recyclerView = binding.recvIndefication.recyclerviewTreatment
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    showStatus(error.message)
                }
            })
    }

    private fun showStatus(title: String){
        binding.apply {
            pgdiseaseTitle2.text = title
            pgdiseaseBar2.visibility = View.GONE
            pgdiseaseTitle2.visibility = View.VISIBLE
        }
        Log.d(DiseaseDetailFragment.page_key,title)
    }

    /*
        Location Request
     */
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
                Log.d(DiseaseDetailFragment.page_key,it.message.toString())
            }

    }

    private fun getCurrentLocation(){
        if (checkPermission(fineLocation) && checkPermission(coarseLocation)){
            fusedLocation.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        curLatitude = location.latitude
                        curLongitude = location.longitude
                        Log.d("current location", curLatitude.toString())
                        binding.tvdisasaeDetailLocation2.visibility = View.VISIBLE
                    }
                }
                .addOnFailureListener {
                    Log.d(DiseaseDetailFragment.page_key,it.message.toString())
                }
        }else{
            requestPermissionLauncher.launch(arrayOf(
                fineLocation,
                coarseLocation
            ))
        }
    }

}