package com.example.rifsa_mobile.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHomeBinding
import com.example.rifsa_mobile.model.local.entity.harvestresult.HarvestResultMock
import com.example.rifsa_mobile.view.home.adapter.HarvestResultRvAdapter


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding


    private var arrayList = ArrayList<HarvestResultMock>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.imageView2.setImageResource(R.drawable.mockprofile)
        arrayList.addAll(setHarvestMockUp)
        showResult()
        return binding.root
    }


    private val setHarvestMockUp : ArrayList<HarvestResultMock>
        get() {
            val title = resources.getStringArray(R.array.title)
            val weight = resources.getStringArray(R.array.berat_mock)
            val date = resources.getStringArray(R.array.tanggal_mock)
            val listMock = ArrayList<HarvestResultMock>()
            for (i in title.indices){
                val temp = HarvestResultMock(
                    title[i],
                    weight[i],
                    date[i]
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