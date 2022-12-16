package com.example.rifsa_mobile.view.fragment.disease.detail

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
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
import com.example.rifsa_mobile.databinding.FragmentDisaseDetailBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseDetailFirebaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseFirebaseEntity
import com.example.rifsa_mobile.helpers.diseasedetection.DiseasePrediction
import com.example.rifsa_mobile.view.fragment.disease.adapter.DiseaseMiscRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
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

@Suppress("DEPRECATION")
class DiseaseDetailFragment : Fragment() {
    private lateinit var binding : FragmentDisaseDetailBinding

    private val remoteViewModel : RemoteViewModel by viewModels{
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
    private var indicationId = ""
    private var isDetail = false

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
    ): View {
        binding = FragmentDisaseDetailBinding.inflate(layoutInflater)
        classification = DiseasePrediction(requireContext())
        fusedLocation = LocationServices.getFusedLocationProviderClient(
            requireContext()
        )

        try {
            val detail = DiseaseDetailFragmentArgs.fromBundle(
                requireArguments()
            ).diseaseData

            val isDiseaseBook = DiseaseDetailFragmentArgs.fromBundle(
                requireArguments()
            ).diseaseDetail

            val imageUrl = DiseaseDetailFragmentArgs.fromBundle(
                requireArguments()
            ).photoDisase

            if (detail != null){
                showDetailDisease(detail)
                isDetail = true
                currentDate = detail.dateDisease
                diseaseId = detail.id
                binding.btnSaveDisease.visibility = View.GONE
                binding.btnDiseaseComplete.visibility = View.VISIBLE
            }else if(isDiseaseBook != null){
                showDetailDiseaseBook(isDiseaseBook)
                isDetail = true
            }

        }catch (e : Exception){ }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isDetail){
            createLocationRequest()
            showImageCapture()
            binding.pgdiseaseBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                delay(2000)
                predictionLocal()
            }
        }

        binding.btnDiseaseBackhome.setOnClickListener {
            findNavController().navigate(
                DiseaseDetailFragmentDirections
                    .actionDisaseDetailFragmentToDisaseFragment()
            )
        }

        binding.btnDiseaseComplete.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setTitle("Selesai teratasi")
                setMessage("apakah penyakit telah teratasi ?")
                apply {
                    setPositiveButton("ya") { _, _ ->
                        binding.pgdiseaseBar.visibility = View.VISIBLE
                        deleteDiseaseImage()
                    }
                    setNegativeButton("tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                }
                create()
                show()
            }
        }

        binding.btnSaveDisease.setOnClickListener {
            uploadDiseaseImage()
            binding.pgdiseaseBar.visibility = View.VISIBLE
        }

    }


    private fun showImageCapture(){
        imageUri = DiseaseDetailFragmentArgs.fromBundle(
            requireArguments()
        ).photoDisase?.toUri()!!

        val bitmapImage = MediaStore.Images.Media.getBitmap(
            requireContext().contentResolver,imageUri
        )
        imageBitmap = bitmapImage
        binding.imgDisaseDetail.setImageBitmap(bitmapImage)
    }

    private fun showDetailDisease(data : DiseaseFirebaseEntity){
        binding.tvdisasaeDetailIndication.text = data.nameDisease
        Glide.with(requireContext())
            .load(data.imageUrl)
            .into(binding.imgDisaseDetail)
        showDiseaseInformation(data.idDisease.toInt())
    }

    private fun showDetailDiseaseBook(data : DiseaseDetailFirebaseEntity){
        binding.tvdisasaeDetailIndication.text = data.Name
        Glide.with(requireContext())
            .load(data.imageUrl)
            .into(binding.imgDisaseDetail)
        showDiseaseInformation(data.id.toInt())
    }


    private fun predictionLocal(){
        classification
            .initPrediction(imageBitmap)
            .addOnSuccessListener { result ->
                binding.tvdisasaeDetailIndication.text = result.name
                binding.pgdiseaseBar.visibility = View.VISIBLE
                showDiseaseInformation(result.id)
                indicationId = result.id.toString()
            }
            .addOnFailureListener { expectation ->
                showStatus(expectation.message.toString())
            }
    }


    private fun showDiseaseInformation(id : Int){
        remoteViewModel.getDiseaseInformation(id.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(DiseaseDetailFirebaseEntity::class.java)
                    if (data != null) {
                        binding.tvdisasaeDetailDescription.text = data.Cause
                        binding.pgdiseaseBar.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showStatus(error.message)
                }
            })

        showTreatment(id.toString(),"Treatment")
        showIndication(id.toString(),"Indication")
    }

    private fun showTreatment(id: String, path : String){
        remoteViewModel.getDiseaseInformationMisc(id,path)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        solutionList.add(it.value.toString())
                        showListTreatment(solutionList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showStatus(error.message)
                }
            })
    }

    private fun showIndication(id: String,parent : String){
        remoteViewModel.getDiseaseInformationMisc(id,parent)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        indicationList.add(it.value.toString())
                        showListIndication(indicationList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showStatus(error.message)
                }
            })
    }

    private fun showListTreatment(data : List<String>){
        val adapter = DiseaseMiscRecyclerViewAdapter(data)
        val recyclerView = binding.recviewDiseaseTreatment
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showListIndication(data: List<String>){
        val adapter = DiseaseMiscRecyclerViewAdapter(data)
        val recyclerView = binding.recviewDiseaseIndication
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun uploadDiseaseImage(){
        val diseaseIndication = binding.tvdisasaeDetailIndication.text.toString()

        authViewModel.getUserId().observe(viewLifecycleOwner){userId ->
            remoteViewModel.uploadDiseaseImage(diseaseId,imageUri,userId)
                .addOnSuccessListener {
                    it.storage.downloadUrl
                        .addOnSuccessListener { respon ->
                            saveDisease(respon,diseaseIndication,userId)
                        }
                        .addOnFailureListener { respon ->
                            showStatus(respon.message.toString())
                        }
                }
                .addOnFailureListener {
                    showStatus(it.message.toString())
                }
        }
    }

    private fun saveDisease(imageUrl : Uri,name : String,userId : String){
        val tempData = DiseaseFirebaseEntity(
            diseaseId,
            name,
            indicationId,
            currentDate,
            curLatitude.toString(),
            curLatitude.toString(),
            imageUrl.toString()
        )

        remoteViewModel.saveDisease(tempData,userId)
            .addOnSuccessListener {
                showStatus("penyakit tersimpan")
                findNavController().navigate(
                    DiseaseDetailFragmentDirections
                        .actionDisaseDetailFragmentToDisaseFragment()
                )
            }
            .addOnFailureListener {
                showStatus(it.message.toString())
            }
    }

    private fun deleteDiseaseImage(){
        authViewModel.getUserId().observe(viewLifecycleOwner){userId ->
            remoteViewModel.deleteDiseaseImage(diseaseId,userId)
                .addOnSuccessListener {
                    deleteDisease()
                }
                .addOnFailureListener {
                    showStatus(it.message.toString())
                }
        }
    }

    private fun deleteDisease(){
        authViewModel.getUserId().observe(viewLifecycleOwner){userId ->
            remoteViewModel.deleteDisease(currentDate,diseaseId,userId)
                .addOnSuccessListener {
                    findNavController().navigate(
                        DiseaseDetailFragmentDirections
                            .actionDisaseDetailFragmentToDisaseFragment()
                    )
                }
                .addOnFailureListener {
                    showStatus(it.message.toString())
                }
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