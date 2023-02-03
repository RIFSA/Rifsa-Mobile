package com.example.rifsa_mobile.view.fragment.harvestresult.insert

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHarvestInsertDetailBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.helpers.utils.Utils
import com.example.rifsa_mobile.view.fragment.harvestresult.HarvestInsertViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class HarvestInsertDetailFragment : Fragment() {
    private lateinit var binding : FragmentHarvestInsertDetailBinding
    private val viewModel : HarvestInsertViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private val authViewModel : UserPrefrencesViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var date = LocalDate.now().toString()
    private var firebaseUserId = ""
    private var harvestId = UUID.randomUUID().toString()
    private var uploadedReminderId = (1..1000).random()
    private var localId = 0
    private var isDetail = false
    private var isConnected = false
    private var isUploaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHarvestInsertDetailBinding.inflate(layoutInflater)
        isConnected = Utils.internetChecker(requireContext())
        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.GONE

        authViewModel.getUserId().observe(viewLifecycleOwner){ firebaseUserId = it}
        try {
            val data = HarvestInsertDetailFragmentArgs.fromBundle(requireArguments()).detailResult
            if (data != null) {
                showDetailHarvest(data)
                localId = data.localId
                isDetail = true
                harvestId = data.id
                date = data.date
            }
        }catch (e : Exception){
            Log.d("HarvestInsertDetail",e.toString())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHarvestSave.setOnClickListener {
            if (!isConnected){
                insertHarvestLocally()
            }else{
                insertUpdateHarvestRemote()
            }
        }

        binding.btnharvestInsertDelete.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setTitle("Hapus data")
                setMessage("apakah anda ingin menghapus data ini ?")
                apply {
                    setPositiveButton("ya") { _, _ ->
                        if (!isUploaded){
                            deleteHarvest(localId)
                        }else{
                            deleteHarvestRemote()
                        }
                    }
                    setNegativeButton("tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                }
                create()
                show()
            }
        }

        binding.btnHarvestdetailBackhome.setOnClickListener {
            findNavController().navigate(
                HarvestInsertDetailFragmentDirections.actionHarvestInsertDetailFragmentToHarvetResultFragment()
            )
        }
    }


    //showing delete button
    private fun showDetailHarvest(data : HarvestEntity){
        binding.apply {
            tvharvestInsertName.setText(data.typeOfGrain)
            tvharvestInsertBerat.setText(data.weight)
            tvharvestInsertCatatan.setText(data.note)
            tvharvestInsertHasil.setText(data.income)
            btnharvestInsertDelete.visibility = View.VISIBLE
            "Detail Data".also { tvHarvestresultInsertdetail.text = it }
        }
    }

    private fun insertUpdateHarvestRemote(){
        val harvestData = HarvestEntity(
            localId = 0,
            id = harvestId,
            firebaseUserId = firebaseUserId,
            date = date,
            typeOfGrain = binding.tvharvestInsertName.text.toString(),
            weight = binding.tvharvestInsertBerat.text.toString(),
            income = binding.tvharvestInsertHasil.text.toString(),
            note = binding.tvharvestInsertCatatan.text.toString(),
            isUploaded = true,
            uploadReminderId = uploadedReminderId
        )

        authViewModel.getUserId().observe(viewLifecycleOwner){ userId->
            lifecycleScope.launch {
                binding.pgbarStatus.visibility = View.VISIBLE
                viewModel.insertUpdateHarvestResult(harvestData,userId)
                    .addOnSuccessListener {
                        showStatus("berhasil menambahkan")
                        findNavController().navigate(
                            HarvestInsertDetailFragmentDirections
                                .actionHarvestInsertDetailFragmentToHarvetResultFragment()
                        )
                        isUploaded = true
                        insertHarvestLocally()
                    }
                    .addOnFailureListener { error->
                        isUploaded = false
                        showStatus(error.toString())
                    }
            }
        }
    }

    private fun deleteHarvestRemote(){
        authViewModel.getUserId().observe(viewLifecycleOwner){ userId ->
            lifecycleScope.launch {
                viewModel.deleteHarvestResult(date,harvestId,userId)
                    .addOnSuccessListener {
                        showStatus("terhapus")
                        findNavController().navigate(
                            HarvestInsertDetailFragmentDirections
                                .actionHarvestInsertDetailFragmentToHarvetResultFragment()
                        )
                        deleteHarvest(localId)
                    }
                    .addOnFailureListener {
                        showStatus("gagal menghapus")
                    }
            }
        }
    }

    private fun insertHarvestLocally(){
        val harvestData = HarvestEntity(
            localId = 0,
            id = harvestId,
            firebaseUserId = firebaseUserId,
            date = date,
            typeOfGrain = binding.tvharvestInsertName.text.toString(),
            weight = binding.tvharvestInsertBerat.text.toString(),
            income = binding.tvharvestInsertHasil.text.toString(),
            note = binding.tvharvestInsertCatatan.text.toString(),
            isUploaded = isUploaded,
            uploadReminderId = uploadedReminderId
        )

        try {
            viewModel.insertHarvestLocally(harvestData)
        }catch (e : Exception){
            Log.d("HarvestInsert",e.toString())
        }
    }

    private fun deleteHarvest(localid : Int){
        try {
            viewModel.deleteHarvestLocal(localid)
        }catch (e : Exception){
            Log.d("HarvestInsert",e.toString())
        }
    }


    private fun showStatus(title : String){
        if (title.isNotEmpty()){
            binding.pgbarStatus.visibility = View.GONE
            binding.pgbarTitle.visibility = View.VISIBLE
        }
        binding.pgbarTitle.text = title

        Log.d(detail_harvest,"status $title")
    }

    companion object{
        const val detail_harvest = "harvest detail"
    }
}