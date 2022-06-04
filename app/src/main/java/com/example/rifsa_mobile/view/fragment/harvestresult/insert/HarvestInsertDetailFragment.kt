package com.example.rifsa_mobile.view.fragment.harvestresult.insert

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
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


    private var isUploaded = false


    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext())  }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHarvestInsertDetailBinding.inflate(layoutInflater)

        localViewModel = ObtainViewModel(requireActivity())
        isUploaded = Utils.internetChecker(requireContext())

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.GONE

        try {
            val data = HarvestInsertDetailFragmentArgs.fromBundle(requireArguments()).detailResult
            if (data != null) {
                showDetail(data)
                isDetail = true
                detailId = data.id_harvest
                sortId = data.id_sort
            }
        }catch (e : Exception){ }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnHarvestSave.setOnClickListener {
            if (isUploaded){
                insertHarvestRemote()
                insertHarvestLocally()
            }else{
                insertHarvestLocally()
            }
        }

        binding.btnharvestInsertDelete.setOnClickListener {
            lifecycleScope.launch {
                deleteHarvestLocal()
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
        val date = LocalDate.now().toString()

        val tempData = HarvestPostBody(
            date,
            binding.tvharvestInsertName.text.toString(),
            binding.tvharvestInsertBerat.text.toString(),
            binding.tvharvestInsertHasil.text.toString(),
            binding.tvharvestInsertCatatan.text.toString(),
        )

        lifecycleScope.launch {
            remoteViewModel.postHarvest(tempData).observe(viewLifecycleOwner){
                when(it){
                    is FetchResult.Success ->{
                        showToast(it.data.message)
                    }
                    is FetchResult.Error ->{
                        showToast(it.error)
                        Log.d("insert hasil",it.error)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun insertHarvestLocally(){
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
            isUploaded
        )

        try {
            localViewModel.insertHarvestlocal(tempInsert)
            showToast("Berhasil menambahkan")
            findNavController()
                .navigate(HarvestInsertDetailFragmentDirections.actionHarvestInsertDetailFragmentToHarvetResultFragment())
        }catch (e : Exception){
            showToast(e.message.toString())
            Log.d(detail_harvest,e.message.toString())
        }
    }

    private suspend fun deleteHarvestLocal(){
        try {
            localViewModel.deleteHarvestLocal(detailId)
            showToast("terhapus")
            findNavController()
                .navigate(HarvestInsertDetailFragmentDirections.actionHarvestInsertDetailFragmentToHarvetResultFragment())
        }catch (e : Exception){
            showToast(e.message.toString())
        }
    }

    private fun showToast(title : String){
        Toast.makeText(requireContext(),title,Toast.LENGTH_SHORT).show()
    }



    companion object{
        const val detail_harvest = "harvest detail"
    }

}