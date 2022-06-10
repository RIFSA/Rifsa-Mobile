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
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestPostBody
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestResponData
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.time.LocalDate

class HarvestInsertDetailFragment : Fragment() {
    private lateinit var binding : FragmentHarvestInsertDetailBinding
    private lateinit var localViewModel: LocalViewModel

    private var isDetail = false
    private var detailId = 0

    private var isConnected = false

    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext())  }
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

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
                showDetailHarvest(data)
                isDetail = true
                detailId = data.idHasil
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
            findNavController().navigate(
                HarvestInsertDetailFragmentDirections.actionHarvestInsertDetailFragmentToHarvetResultFragment()
            )
        }
    }


    //showing delete button
    private fun showDetailHarvest(data : HarvestResponData){
        binding.apply {
            tvharvestInsertName.setText(data.jenis)
            tvharvestInsertBerat.setText(data.berat)
            tvharvestInsertCatatan.setText(data.catatan)
            tvharvestInsertHasil.setText(data.jual)
            btnharvestInsertDelete.visibility = View.VISIBLE
            "Detail RegisterData".also { tvHarvestresultInsertdetail.text = it }
        }
    }

    private fun insertUpdateHarvestRemote(){
        authViewModel.getUserToken().observe(viewLifecycleOwner){ token->
            lifecycleScope.launch {
                val date = LocalDate.now().toString()

                val tempData = HarvestPostBody(
                    date,
                    binding.tvharvestInsertName.text.toString(),
                    binding.tvharvestInsertBerat.text.toString(),
                    binding.tvharvestInsertHasil.text.toString(),
                    binding.tvharvestInsertCatatan.text.toString(),
                )

                if (!isDetail){
                    remoteViewModel.postHarvestRemote(tempData,token).observe(viewLifecycleOwner){
                        when(it){
                            is FetchResult.Loading ->{
                                binding.pgbarStatus.visibility = View.VISIBLE
                            }
                            is FetchResult.Success ->{
                                showStatus(it.data.message)
                                findNavController()
                                    .navigate(HarvestInsertDetailFragmentDirections.actionHarvestInsertDetailFragmentToHarvetResultFragment())
                            }
                            is FetchResult.Error ->{
                                showStatus(it.error)
                            }
                            else -> {}
                        }
                    }
                }else{
                    remoteViewModel.updateHarvestRemote(detailId, tempData,token).observe(viewLifecycleOwner){
                        when(it){
                            is FetchResult.Loading ->{
                                binding.pgbarStatus.visibility = View.VISIBLE
                            }
                            is FetchResult.Success ->{
                                showStatus(it.data.message)
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

    }

    private fun deleteHarvestRemote(){
        authViewModel.getUserToken().observe(viewLifecycleOwner){ token ->
            lifecycleScope.launch {
                remoteViewModel.deleteHarvestRemote(detailId,token).observe(viewLifecycleOwner){
                    when(it){
                        is FetchResult.Success ->{
                            showStatus(it.data.message)
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