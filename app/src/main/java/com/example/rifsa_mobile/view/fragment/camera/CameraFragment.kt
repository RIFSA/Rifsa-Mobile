package com.example.rifsa_mobile.view.fragment.camera

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentCameraBinding
import com.example.rifsa_mobile.helpers.diseasedetection.DiseasePrediction
import com.example.rifsa_mobile.helpers.utils.Utils
import com.example.rifsa_mobile.view.fragment.inventory.insert.InvetoryInsertFragment.Companion.camera_key_inventory
import com.google.android.material.bottomnavigation.BottomNavigationView

class CameraFragment : Fragment() {
    private var _binding : FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var classification : DiseasePrediction
    private var diseaseName = ""
    private var imageCapture : ImageCapture? = null

    private var type = ""

    private val launchIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ respon ->
        if (respon.resultCode == Activity.RESULT_OK){
            val uriImage : Uri = respon.data?.data as Uri
            diseaseClassificer(uriImage)
        }
    }

    private fun allPermissionGranted() = required_permission.all {
        ContextCompat.checkSelfPermission(requireContext(),it) == PackageManager.PERMISSION_GRANTED
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (!allPermissionGranted()){
                showToast("tidak ada izin")
                requireActivity().finishAffinity()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(layoutInflater)
        classification = DiseasePrediction(requireContext())
        type = CameraFragmentArgs.fromBundle(requireArguments()).pageKey.toString()


        if (!allPermissionGranted()){
            ActivityCompat.requestPermissions(requireActivity(), required_permission, REQUEST_CODE_PERMISSIONS)
        }

        startCamera()

        binding.btncameraCapture.setOnClickListener {
            capturePhoto()
        }

        binding.btnGallery.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "*/*"
            launchIntentGallery.launch(intent)
        }

        return binding.root
    }

    private fun startCamera(){
        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.GONE

        val cameraProvider = ProcessCameraProvider.getInstance(requireContext())
        cameraProvider.addListener({
            val provider : ProcessCameraProvider = cameraProvider.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()
            try {
                provider.unbindAll()
                provider.bindToLifecycle(
                    viewLifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture
                )
            }catch (e : Exception){
                Toast.makeText(
                    context,
                    e.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(camera_fragment,e.message.toString())
            }

        },ContextCompat.getMainExecutor(requireContext()))
    }

    private fun capturePhoto(){
        val imageResult = imageCapture?:return
        val imageFile = Utils.createFile(requireActivity().application)

        val outputFiles = ImageCapture.OutputFileOptions.Builder(imageFile).build()
        imageResult.takePicture(
            outputFiles,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    showToast("berhasil mengambil gambar")
                    val uriCapture = Uri.fromFile(imageFile)
                    diseaseClassificer(uriCapture)
                }

                override fun onError(exception: ImageCaptureException) {
                   showToast("gagal mengambil gambar")
                }

            }
        )
    }

    //predict
    private fun diseaseClassificer(image: Uri){
        binding.pgbarClassficer.visibility = View.VISIBLE
        val bitmapImage = MediaStore.Images.Media.getBitmap(
            requireContext().contentResolver,image
        )
        classification
            .initPrediction(bitmapImage)
            .addOnSuccessListener { result ->
                diseaseName = result.name
                goToDetailDisease(
                    image,
                    diseaseName,
                    result.id
                )
                binding.pgbarClassficer.visibility = View.GONE
            }
            .addOnFailureListener { e ->
                Log.d("disease",e.message.toString())
                binding.pgbarClassficer.visibility = View.GONE
            }
    }

    private fun goToDetailDisease(data : Uri,diseaseName : String,diseaseId : Int){
        if (type == "back"){
            findNavController()
                .previousBackStackEntry?.savedStateHandle
                ?.set(camera_key_inventory,data)
            findNavController()
        }else{
            findNavController().navigate(
                CameraFragmentDirections.actionCameraFragmentToPredictionDiseaseFragment(
                    data.toString(),
                    diseaseName,
                    diseaseId
                )
            )
        }
    }

    private fun showToast(title : String){
        Toast.makeText(requireContext(),title,Toast.LENGTH_SHORT).show()
    }

    companion object{
        private val required_permission = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val camera_fragment = "camera_fragment"
    }



}