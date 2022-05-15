package com.example.rifsa_mobile.view.fragment.harvestresult

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHarvetResultBinding
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult
import com.example.rifsa_mobile.view.fragment.home.adapter.HarvestResultRvAdapter


class HarvetResultFragment : Fragment() {
    private lateinit var binding : FragmentHarvetResultBinding

    private var arrayList = ArrayList<HarvestResult>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHarvetResultBinding.inflate(layoutInflater)

        arrayList.addAll(setHarvestUp)
        showResult()

        binding.fabHarvestToinsert.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.mainnav_framgent,HarvestInsertDetailFragment())
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
                    title[i],
                    weight[i],
                    date[i]
                )
                listMock.add(temp)
            }
            return listMock
        }

    private fun showResult(){
        val adapter = HarvestResultRvAdapter(arrayList)
        val recview = binding.rvHarvestresult
        recview.adapter = adapter
        recview.layoutManager = LinearLayoutManager(requireContext())
        adapter.onDetailCallBack(object : HarvestResultRvAdapter.onDetailCallback{
            override fun onDetailCallback(data: HarvestResult) {
                val bundle = Bundle()
                val fragment = HarvestInsertDetailFragment()
                bundle.putParcelable("detail_result",data)
                fragment.arguments = bundle
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.mainnav_framgent,fragment)
                    .commit()
            }
        })
    }
}