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
import androidx.lifecycle.lifecycleScope
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHarvestInsertDetailBinding
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.view.fragment.harvestresult.HarvetResultFragment
import com.example.rifsa_mobile.view.fragment.harvestresult.HarvetResultFragment.Companion.detail_result
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

class HarvestInsertDetailFragment : Fragment() {
    private lateinit var binding : FragmentHarvestInsertDetailBinding
    private lateinit var localViewModel: LocalViewModel

    private var randomId = Utils.randomId()
    private var isDetail = false
    private var detailId = ""
    private var sortId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHarvestInsertDetailBinding.inflate(layoutInflater)
        localViewModel = ObtainViewModel(requireActivity())

        try {
            val data = arguments?.getParcelable<HarvestResult>(detail_result)
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
            lifecycleScope.launch {
                insertHarvestLocally()
            }
        }

        binding.btnharvestInsertDelete.setOnClickListener {
            lifecycleScope.launch {
                deleteHarvestLocal()
            }
        }
    }


    //jika termasuk detail maka tombol delete akan ditampilkan
    private fun showDetail(data : HarvestResult){
        binding.apply {
            tvharvestInsertName.setText(data.title)
            tvharvestInsertBerat.setText(data.weight.toString())
            tvharvestInsertCatatan.setText(data.noted)
            tvharvestInsertHasil.setText(data.sellingPrice.toString())

            btnharvestInsertDelete.visibility = View.VISIBLE
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun insertHarvestLocally(){
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
            false
        )

        try {
            localViewModel.insertHarvestlocal(tempInsert)
            showToast("Berhasil menambahkan")
            setFragment(HarvetResultFragment())
        }catch (e : Exception){
            showToast(e.message.toString())
            Log.d(detail_harvest,e.message.toString())
        }
    }

    private suspend fun deleteHarvestLocal(){
        try {
            localViewModel.deleteHarvestLocal(detailId)
            showToast("terhapus")
            setFragment(HarvetResultFragment())
        }catch (e : Exception){
            showToast(e.message.toString())
        }
    }

    private fun showToast(title : String){
        Toast.makeText(requireContext(),title,Toast.LENGTH_SHORT).show()
    }

    private fun setFragment(fragment : Fragment){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainnav_framgent,fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object{
        const val detail_harvest = "harvest detail"
    }

}