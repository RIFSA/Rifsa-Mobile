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
import com.example.rifsa_mobile.model.entity.local.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestPostBody
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.time.LocalDate

class HarvestInsertDetailFragment : Fragment() {
    private lateinit var binding : FragmentHarvestInsertDetailBinding
    private lateinit var localViewModel: LocalViewModel

    private var randomId = Utils.randomId()
    private var isDetail = false
    private var detailId = ""
    private var sortId = 0

    private var isConnected = false
    private var valueStatus = ""

    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext())  }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHarvestInsertDetailBinding.inflate(layoutInflater)


        localViewModel = ObtainViewModel(requireActivity())
        isConnected = Utils.internetChecker(requireContext())

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.GONE

        try {
            val data = HarvestInsertDetailFragmentArgs.fromBundle(requireArguments()).detailResult
            if (data != null) {
                showDetail(data)
                isDetail = true
                detailId = data.id_harvest
                sortId = data.id_sort
                valueStatus = data.valueStatus
            }
        }catch (e : Exception){ }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnHarvestSave.setOnClickListener {
            if (isDetail){
                updateHarvestRemote() //TODO | memperbarui data
            }else{
                insertHarvestRemote() //TODO | menyimpan data
            }
        }


        binding.btnharvestInsertDelete.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setTitle("Hapus data")
                setMessage("apakah anda ingin menghapus data ini ?")
                apply {
                    setPositiveButton("ya") { _, _ ->
                        deleteHarvestRemote()  //TODO | menghapus data
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
            findNavController()
                .navigate(
                    HarvestInsertDetailFragmentDirections.actionHarvestInsertDetailFragmentToHarvetResultFragment()
                )
        }
    }


    //showing delete button
    private fun showDetail(data : HarvestResult){
        binding.apply {
            tvharvestInsertName.setText(data.title)
            tvharvestInsertBerat.setText(data.weight.toString())
            tvharvestInsertCatatan.setText(data.noted)
            tvharvestInsertHasil.setText(data.sellingPrice.toString())
            btnharvestInsertDelete.visibility = View.VISIBLE
            "Detail RegisterData".also { tvHarvestresultInsertdetail.text = it }
        }
    }

    private fun insertHarvestRemote(){
        lifecycleScope.launch {
            val date = LocalDate.now().toString()

            val tempData = HarvestPostBody(
                date,
                binding.tvharvestInsertName.text.toString(),
                binding.tvharvestInsertBerat.text.toString(),
                binding.tvharvestInsertHasil.text.toString(),
                binding.tvharvestInsertCatatan.text.toString(),
            )

            remoteViewModel.postHarvestRemote(tempData).observe(viewLifecycleOwner){
                when(it){
                    is FetchResult.Success ->{
                        valueStatus = "DONE" //TODO | set status telah selesai
                        showStatus(it.data.message)
                        insertUpdateHarvestLocally()
                    }
                    is FetchResult.Error ->{
                        valueStatus = "POST" //TODO | set status perlu di post pada remote checker
                        showStatus(it.error)
                        insertUpdateHarvestLocally()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun deleteHarvestRemote(){
        lifecycleScope.launch {
            if(valueStatus == "DONE"){
                remoteViewModel.deleteHarvestRemote(detailId.toInt()).observe(viewLifecycleOwner){
                    when(it){
                        is FetchResult.Success ->{
                            showStatus(it.data.message)
                            deleteHarvestLocal()
                            findNavController()
                                .navigate(HarvestInsertDetailFragmentDirections.actionHarvestInsertDetailFragmentToHarvetResultFragment())
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

    private fun updateHarvestRemote(){
        val date = LocalDate.now().toString()

        val tempData = HarvestPostBody(
            date,
            binding.tvharvestInsertName.text.toString(),
            binding.tvharvestInsertBerat.text.toString(),
            binding.tvharvestInsertHasil.text.toString(),
            binding.tvharvestInsertCatatan.text.toString(),
        )

        lifecycleScope.launch {
            remoteViewModel.updateHarvestRemote(detailId.toInt(), tempData).observe(viewLifecycleOwner){
                when(it){
                    is FetchResult.Loading ->{
                        binding.pgbarStatus.visibility = View.VISIBLE
                    }
                    is FetchResult.Success ->{
                        valueStatus = "DONE" //TODO | set status telah terupdate remote
                        showStatus(it.data.message)
                        insertUpdateHarvestLocally()
                    }
                    is FetchResult.Error ->{
                        valueStatus = "UPDATE" //TODO | set status perlu di update pada remote
                        showStatus(it.error)
                        insertUpdateHarvestLocally()
                    }
                    else -> {}
                }
            }
        }
    }


    private fun insertUpdateHarvestLocally(){
        val date = LocalDate.now().toString()

        if (isDetail){
            randomId = detailId
        }

        val tempInsert = HarvestResult(
            sortId,
            randomId,
            date,
            binding.tvharvestInsertName.text.toString(),
            binding.tvharvestInsertBerat.text.toString().toInt(),
            binding.tvharvestInsertHasil.text.toString().toInt(),
            binding.tvharvestInsertCatatan.text.toString(),
            valueStatus //TODO | berdasarkan status remote apakah berhasil
        )

        try {
            localViewModel.insertHarvestlocal(tempInsert)
            showStatus("Tersimpan")
            findNavController()
                .navigate(HarvestInsertDetailFragmentDirections.actionHarvestInsertDetailFragmentToHarvetResultFragment())
        }catch (e : Exception){
            showStatus(e.message.toString())
        }
    }

    private fun deleteHarvestLocal(){
            try {
                localViewModel.deleteHarvestLocal(detailId)
                showStatus("Terhapus")
                findNavController()
                    .navigate(HarvestInsertDetailFragmentDirections.actionHarvestInsertDetailFragmentToHarvetResultFragment())
            }catch (e : Exception){
                showStatus("Gagal")
                Log.d(detail_harvest,e.message.toString())
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