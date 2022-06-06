package com.example.rifsa_mobile.view.fragment.harvestresult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHarvetResultBinding
import com.example.rifsa_mobile.model.entity.local.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestPostBody
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestResponData
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.view.fragment.harvestresult.adapter.HarvestResultRvAdapter
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch


class HarvetResultFragment : Fragment() {
    private lateinit var binding : FragmentHarvetResultBinding
    private lateinit var localViewModel: LocalViewModel
    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }


    private var isConnected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHarvetResultBinding.inflate(layoutInflater)
        localViewModel = ObtainViewModel(requireActivity())
        isConnected = Utils.internetChecker(requireContext())


        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        showResult()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabHarvestToinsert.setOnClickListener {
            findNavController().navigate(
                HarvetResultFragmentDirections.actionHarvetResultFragmentToHarvestInsertDetailFragment(null)
            )
        }

        //TODO | test get data from remote
        binding.textView15.setOnClickListener {
            getResultFromRemote()
        }


        binding.btnHarvestBackhome.setOnClickListener {
            findNavController().navigate(
                HarvetResultFragmentDirections.actionHarvetResultFragmentToHomeFragment()
            )
        }

    }

    private fun showResult(){
        localViewModel.readHarvestLocal().observe(viewLifecycleOwner){ respon ->
            val adapter = HarvestResultRvAdapter(respon)
            val recyclerView = binding.rvHarvestresult
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            adapter.onDetailCallBack(object : HarvestResultRvAdapter.OnDetailCallback{
                override fun onDetailCallback(data: HarvestResult) {
                    findNavController().navigate(HarvetResultFragmentDirections
                        .actionHarvetResultFragmentToHarvestInsertDetailFragment(data))
                }
            })
            if (respon.isEmpty()){
                binding.harvestEmptyState.emptyState.visibility = View.VISIBLE
            }


            binding.cekUploadTest.setOnClickListener {
                //TODO | cek internet apakah tersedia
                if (isConnected){
                    remoteChecker(respon)
                }
            }

        }
    }


    //TODO | ambil data dari remote
    private fun getResultFromRemote(){
        lifecycleScope.launch {
            remoteViewModel.getHarvest().observe(viewLifecycleOwner){
                when(it){
                    is FetchResult.Success->{
                        it.data.harvestResponData.forEach { respon ->
                            insertRemoteToLocal(respon)
                        }
                    }
                    is FetchResult.Error->{
                        showToast(it.error)
                        Log.d("Read harvest Result",it.error)
                    }
                    else -> {}
                }
            }
        }
    }

    //TODO | setelah data dari remote masukan ke dalam local
    private fun insertRemoteToLocal(data : HarvestResponData){
        val tempInsert = HarvestResult(
            0,
            data.idHasil.toString(),
            data.tanggal,
            data.jenis,
            data.berat.toInt(),
            data.jual.toInt(),
            data.catatan,
            "DONE"
        )

        lifecycleScope.launch {
            localViewModel.insertHarvestlocal(tempInsert)
        }
    }


    //TODO | cek data local apakah sudah ada di remote
    private fun remoteChecker(data : List<HarvestResult>){
        data.forEach { value->
            when(value.noted){
                "POST"->{
                    Log.d("data yang belum di upload remote","detect title = " + value.title)
                    insertHarvestRemote(value)
                }
                "UPDATE"->{
                    Log.d("data yg belum di update remote","detect title = " + value.title)
                    updateHarvestRemote(value)
                }
            }
        }
    }

    private fun insertHarvestRemote(data : HarvestResult){
        val tempData = HarvestPostBody(
            data.date,
            data.title,
            data.weight.toString(),
            data.sellingPrice.toString(),
            data.noted
        )
        lifecycleScope.launch {
            remoteViewModel.postHarvest(tempData).observe(viewLifecycleOwner){
                when(it){
                    is FetchResult.Success ->{
                        Log.d("Test update", "Berhasil")
                    }

                    //TODO | setelah post data remote baru perbarui status data menjadi done
                    is FetchResult.Error ->{
                        updateHarvestLocal("DONE",data.id_sort)
                        Log.d("Test update",it.error)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun updateHarvestRemote(data : HarvestResult){
        val tempData = HarvestPostBody(
            data.date,
            data.title,
            data.weight.toString(),
            data.sellingPrice.toString(),
            data.noted
        )

        lifecycleScope.launch {
            remoteViewModel.updateHarvest(data.id_harvest.toInt(), tempData).observe(viewLifecycleOwner){
                when(it){
                    is FetchResult.Success ->{
                        Log.d("update hasil",it.data.message)
                    }
                    is FetchResult.Error ->{
                        Log.d("update hasil",it.error)
                    }
                    else -> {}
                }
            }
        }
    }

    //TODO | update id by response
    private fun updateHarvestLocal(uploadedStatus : String, idSort : Int){
        lifecycleScope.launch {
            localViewModel.updateHarvestLocal(uploadedStatus, idSort)
        }
    }


    private fun showToast(title : String){
        Toast.makeText(requireContext(),title, Toast.LENGTH_SHORT).show()
    }

}