package com.example.rifsa_mobile.view.fragment.disease.detail

import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rifsa_mobile.databinding.FragmentDisaseDetailBinding
import com.example.rifsa_mobile.helpers.diseasedetection.DiseasePrediction
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseDetailEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.view.fragment.disease.viewmodel.DiseaseDetailViewModel
import com.example.rifsa_mobile.view.fragment.disease.adapter.DiseaseMiscRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class DiseaseDetailFragment : Fragment() {
    private lateinit var binding : FragmentDisaseDetailBinding

    private val viewModel : DiseaseDetailViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private val authViewModel : UserPrefrencesViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var classification : DiseasePrediction
    private var solutionList = ArrayList<String>()
    private var indicationList = ArrayList<String>()

    private var isDetail = false
    private var dateRecord = ""
    private var diseaseId = ""
    private lateinit var firebaseUserId : String
    private lateinit var imageUri : Uri
    private lateinit var imageBitmap  : Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisaseDetailBinding.inflate(layoutInflater)
        classification = DiseasePrediction(requireContext())

        authViewModel.getUserId().observe(viewLifecycleOwner){userId ->
            firebaseUserId = userId
        }

        //insert disease
        try {
            val detail = DiseaseDetailFragmentArgs.fromBundle(
                requireArguments()
            ).diseaseData

            if (detail != null) {
                showDetailDisease(detail)
                isDetail = true
            }
            showDiseaseImage()

        }catch (e : Exception){ }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDiseaseBackhome.setOnClickListener {
            findNavController().navigate(
                DiseaseDetailFragmentDirections
                    .actionDisaseDetailFragmentToDisaseFragment()
            )
        }

        binding.btnDiseaseComplete.setOnClickListener {
            alertDialogComplete()
        }
    }

    private fun showDiseaseImage(){
        imageUri = DiseaseDetailFragmentArgs.fromBundle(
            requireArguments()
        ).diseaseData.imageUrl.toUri()

        val bitmapImage = MediaStore.Images.Media.getBitmap(
            requireContext().contentResolver,imageUri
        )
        dateRecord = DiseaseDetailFragmentArgs.fromBundle(
            requireArguments()
        ).diseaseData.dateDisease

        diseaseId = DiseaseDetailFragmentArgs.fromBundle(
            requireArguments()
        ).diseaseData.diseaseId

        imageBitmap = bitmapImage

        Glide.with(requireContext())
            .load(imageUri)
            .into(binding.imgDisaseDetail)
    }


    private fun deleteImageRemote(){
        viewModel.deleteDiseaseImage(diseaseId,firebaseUserId)
            .addOnSuccessListener {
                deleteDataRemote()
            }
            .addOnFailureListener {
                showStatus(it.message.toString())
            }
    }

    private fun deleteDataRemote(){
        viewModel.deleteDisease(dateRecord,diseaseId,firebaseUserId)
            .addOnSuccessListener {
                deleteDataLocal()
                findNavController().navigate(
                    DiseaseDetailFragmentDirections
                        .actionDisaseDetailFragmentToDisaseFragment()
                )
            }
            .addOnFailureListener {
                showStatus(it.message.toString())
            }
    }

    private fun deleteDataLocal(){
        lifecycleScope.launch {
            viewModel.deleteDiseaseLocal(diseaseId)
            Log.d("diseae",diseaseId)
        }
    }

    private fun showDetailDisease(data : DiseaseEntity){
        binding.tvdisasaeDetailIndication.text = data.nameDisease
        Glide.with(requireContext())
            .load(data.imageUrl)
            .into(binding.imgDisaseDetail)
        showDiseaseInformation(data.indexDisease)
    }

    private fun showDiseaseInformation(id : Int){
        viewModel.getDiseaseInformation(id.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(DiseaseDetailEntity::class.java)
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

    private fun showTreatment(id: String, parentDisease : String){
        viewModel.getDiseaseInformationMisc(id,parentDisease)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        solutionList.add(it.value.toString())
                        val adapter = DiseaseMiscRecyclerViewAdapter(solutionList)
                        val recyclerView = binding.recvTreatment.recyclerviewCharacteristic
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
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        indicationList.add(it.value.toString())
                        val adapter = DiseaseMiscRecyclerViewAdapter(indicationList)
                        val recyclerView = binding.recvIndefication.recyclerviewCharacteristic
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
            pgdiseaseTitle.text = title
            pgdiseaseBar.visibility = View.GONE
            pgdiseaseTitle.visibility = View.VISIBLE
        }
        Log.d(page_key,title)
    }

    private fun alertDialogComplete(){
        AlertDialog.Builder(requireActivity()).apply {
            setTitle("Selesai teratasi")
            setMessage("apakah penyakit telah teratasi ?")
            apply {
                setPositiveButton("ya") { _, _ ->
                    binding.pgdiseaseBar.visibility = View.VISIBLE
                    deleteDataLocal()
                }
                setNegativeButton("tidak") { dialog, _ ->
                    dialog.dismiss()
                }
            }
            create()
            show()
        }
    }

    companion object{
        const val page_key = "Disease_detail"
    }


}