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
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestFirebaseEntity
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class HarvestInsertDetailFragment : Fragment() {
    private lateinit var binding : FragmentHarvestInsertDetailBinding

    private var date = LocalDate.now().toString()
    private var detailId = UUID.randomUUID().toString()

    private var isDetail = false
    private var isConnected = false

    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext())  }
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHarvestInsertDetailBinding.inflate(layoutInflater)

        isConnected = Utils.internetChecker(requireContext())

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.GONE

        try {
            val data = HarvestInsertDetailFragmentArgs.fromBundle(requireArguments()).detailResult
            if (data != null) {
                showDetailHarvest(data)
                isDetail = true
                detailId = data.id
                date = data.date
            }
        }catch (e : Exception){ }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHarvestSave.setOnClickListener {
            insertUpdateHarvestRemote()
        }

        binding.btnharvestInsertDelete.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setTitle("Hapus data")
                setMessage("apakah anda ingin menghapus data ini ?")
                apply {
                    setPositiveButton("ya") { _, _ ->
                        deleteHarvestRemote()
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
    private fun showDetailHarvest(data : HarvestFirebaseEntity){
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
        authViewModel.getUserId().observe(viewLifecycleOwner){ userId->
            lifecycleScope.launch {

                val tempData = HarvestFirebaseEntity(
                    detailId,
                    date,
                    binding.tvharvestInsertName.text.toString(),
                    binding.tvharvestInsertBerat.text.toString(),
                    binding.tvharvestInsertHasil.text.toString(),
                    binding.tvharvestInsertCatatan.text.toString(),
                    true
                )

                binding.pgbarStatus.visibility = View.VISIBLE
                remoteViewModel.insertUpdateHarvestResult(tempData,userId)
                    .addOnSuccessListener {
                        showStatus("berhasil menambahkan")
                        findNavController().navigate(
                            HarvestInsertDetailFragmentDirections
                                .actionHarvestInsertDetailFragmentToHarvetResultFragment()
                        )
                    }
                   .addOnFailureListener {
                        showStatus(it.message.toString())
                    }
                }

        }

    }

    private fun deleteHarvestRemote(){
        authViewModel.getUserId().observe(viewLifecycleOwner){ userId ->
            lifecycleScope.launch {
                remoteViewModel.deleteHarvestResult(date,detailId,userId)
                    .addOnSuccessListener {
                        showStatus("terhapus")
                        findNavController().navigate(
                            HarvestInsertDetailFragmentDirections
                                .actionHarvestInsertDetailFragmentToHarvetResultFragment()
                        )
                    }
                    .addOnFailureListener {
                        showStatus("gagal menghapus")
                    }
            }
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