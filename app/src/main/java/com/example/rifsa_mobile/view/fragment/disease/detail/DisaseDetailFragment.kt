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
import com.example.rifsa_mobile.model.entity.local.disase.Disease
import com.example.rifsa_mobile.model.entity.remote.disease.DiseaseResultDataResponse
import com.example.rifsa_mobile.model.entity.remote.disease.restapivm.NewDiseaseResultResponItem
import com.example.rifsa_mobile.utils.AlarmReceiver
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.gms.location.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit


class DisaseDetailFragment : Fragment() {
    private lateinit var binding : FragmentDisaseDetailBinding

    private val remoteViewModel : RemoteViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private val authViewModel : UserPrefrencesViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var localViewModel: LocalViewModel

    private lateinit var alarmReceive : AlarmReceiver
    private var alarmID = (1..1000).random()

    private var randomId = 0
    private var image = ""
    private var isDetail = false
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
        localViewModel = ObtainViewModel(requireActivity())
        alarmReceive = AlarmReceiver()
        fusedLocation =
            LocationServices.getFusedLocationProviderClient(requireContext())

        createLocationRequest()

        try {
            val detail = DisaseDetailFragmentArgs.fromBundle(requireArguments()).diseaseDetail
            if (detail != null){
                showDetailDisease(detail)
                isDetail = true
                randomId = detail.idPenyakit
                binding.btnDiseaseSave.visibility = View.GONE
//                alarmID  = detail.reminderID
//                sortId = detail.id_sort
            }
        }catch (e : Exception){ }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isDetail){
            showImageCapture()
        }

        binding.btnDiseaseSave.setOnClickListener {
            lifecycleScope.launch {
                binding.pgdiseaseBar.visibility = View.VISIBLE
                showStatus("Proses")
//                postDiseaseRemote()
                if (!isDetail){
                    postPrediction()
                }
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


    private fun showDetailDisease(data : NewDiseaseResultResponItem){
        binding.btnDiseaseComplete.visibility = View.VISIBLE
        binding.tvdisasaeDetailIndication.setText(data.indikasi)

        Glide.with(requireContext())
            .load("http://34.101.50.17:5000/images/${data.image}")
            .into(binding.imgDisaseDetail)
    }


    private fun postPrediction(){
        authViewModel.getUserToken().observe(viewLifecycleOwner){ token->
            val image = image.toUri()
            val name = binding.tvdisasaeDetailIndication.text.toString()
            val date = LocalDate.now().toString()

            val currentImage = Utils.uriToFile(image,requireContext())
            val typeFile = currentImage.asRequestBody("image/jpg".toMediaTypeOrNull())
            val multiPartFile : MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                currentImage.name,
                typeFile
            )


            lifecycleScope.launch {
                remoteViewModel.postDiseasePrediction(
                    multiPartFile,
                    "test",
                    date,
                    "test",
                    token
                )
                    .observe(viewLifecycleOwner){
                        when(it){
                            is FetchResult.Loading->{
                                binding.pgdiseaseBar.visibility = View.VISIBLE
                            }
                            is FetchResult.Success ->{
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


    private fun postDiseaseRemote(){
        val image = image.toUri()
        val name = binding.tvdisasaeDetailIndication.text.toString()

        val currentImage = Utils.uriToFile(image,requireContext())
        val typeFile = currentImage.asRequestBody("image/jpg".toMediaTypeOrNull())
        val filePart : MultipartBody.Part = MultipartBody.Part.createFormData(
            "file",
            currentImage.name,
            typeFile
        )
        authViewModel.getUserToken().observe(viewLifecycleOwner){ token->
            lifecycleScope.launch {
                remoteViewModel.postDiseaseRemote(
                    name,
                    filePart,
                    name,
                    name,
                    curLatitude,
                    curLongitude,
                    token
                ).observe(viewLifecycleOwner){
                    when(it){
                        is FetchResult.Success->{
                            showStatus(it.data.message)
                            findNavController().navigate(
                                DisaseDetailFragmentDirections.actionDisaseDetailFragmentToDisaseFragment()
                            )
                        }
                        is FetchResult.Error->{
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

        binding.tvdisasaeDetailDescription.text = dataSolustion.toString()
    }

    private suspend fun insertDiseaseLocal(){
        delay(2000)
        val date = LocalDate.now().toString()

        val tempInsert = Disease(
            sortId,
            randomId.toString(),
            "test peyakit",
            binding.tvdisasaeDetailIndication.text.toString(),
            image,
            date,
            curLatitude,
            curLongitude,
            binding.tvdisasaeDetailDescription.text.toString(),
            alarmID,
            isUploaded = false
        )


        try {
            showStatus("Tersimpan")
            localViewModel.insertDiseaseLocal(tempInsert)
            setReminder()
            findNavController().navigate(
                DisaseDetailFragmentDirections.actionDisaseDetailFragmentToDisaseFragment()
            )
        }catch (e : Exception){
            showStatus(e.message.toString())
        }
    }

    private fun deleteDiseaseLocal(){
        try {
            localViewModel.deleteDiseaseLocal(randomId.toString())
            showStatus("Terhapus")
            findNavController().navigate(
                DisaseDetailFragmentDirections.actionDisaseDetailFragmentToDisaseFragment()
            )
        }catch (e : Exception){
            showStatus(e.message.toString())
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
                    if (location != null){
                        curLatitude = location.latitude
                        curLongitude = location.longitude
                        Log.d("disease",curLatitude.toString())
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

    private fun setReminder(){
        val time = Date()
        val currentTime = timeFormat.format(time)

        alarmReceive.setRepeatReminder(
            requireContext(),
            AlarmReceiver.type_alarm,
            currentTime,
            binding.tvdisasaeDetailIndication.text.toString(),
            alarmID
        )
    }

    private fun stopAlarm(){
        alarmReceive.cancelAlarm(requireContext(),alarmID)
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
        val timeFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    }


}