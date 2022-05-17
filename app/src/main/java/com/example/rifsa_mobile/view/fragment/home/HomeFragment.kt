package com.example.rifsa_mobile.view.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHomeBinding
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult
import com.example.rifsa_mobile.view.fragment.harvestresult.HarvetResultFragment
import com.example.rifsa_mobile.view.fragment.home.adapter.HarvestResultRvAdapter
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    private var arrayList = ArrayList<HarvestResult>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.imageView2.setImageResource(R.drawable.mockprofile)
        arrayList.addAll(setHarvestUp)
        showResult()

        authViewModel.getUserName().observe(viewLifecycleOwner){
            binding.tvhomeName.text = it
        }

        binding.btnHomeHasil.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.mainnav_framgent,HarvetResultFragment())
                .commit()
        }

        return binding.root
    }


    private val setHarvestUp : ArrayList<HarvestResult>
        get() {
            val title = resources.getStringArray(R.array.hasil_mock)
            val weight = resources.getStringArray(R.array.berat_mock)
            val date = resources.getStringArray(R.array.tanggal_mock)
            val listMock = ArrayList<HarvestResult>()
            for (i in title.indices){
                val temp = HarvestResult(
                    i,
                    date[i],
                    title[i],
                    weight[i],
                    100,
                    "adsad"
                )
                listMock.add(temp)
            }
            return listMock
        }


    private fun showResult(){
        val recview = binding.rvHomeHarvest
        recview.adapter = HarvestResultRvAdapter(arrayList)
        recview.layoutManager = LinearLayoutManager(requireContext())
    }

}