package com.example.rifsa_mobile.view.fragment.harvestresult

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHarvetResultBinding
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult
import com.example.rifsa_mobile.view.fragment.home.adapter.HarvestResultRvAdapter
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import kotlinx.coroutines.launch


class HarvetResultFragment : Fragment() {
    private lateinit var binding : FragmentHarvetResultBinding
    private lateinit var localViewModel: LocalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHarvetResultBinding.inflate(layoutInflater)
        localViewModel = ObtainViewModel(requireActivity())


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



    private fun showResult(){
        localViewModel.readHarvestLocal().observe(viewLifecycleOwner){ respon ->
            val adapter = HarvestResultRvAdapter(respon)
            val recview = binding.rvHarvestresult
            recview.adapter = adapter
            recview.layoutManager = LinearLayoutManager(requireContext())
            adapter.onDetailCallBack(object : HarvestResultRvAdapter.onDetailCallback{
                override fun onDetailCallback(data: HarvestResult) {
                    val bundle = Bundle()
                    val fragment = HarvestInsertDetailFragment()
                    bundle.putParcelable(detail_result,data)
                    bundle.putString(page_key, page_detail)
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

    companion object{
        const val page_key = "insert_key"
        const val page_detail = "detail"
        const val detail_result = "detail_result"
    }
}