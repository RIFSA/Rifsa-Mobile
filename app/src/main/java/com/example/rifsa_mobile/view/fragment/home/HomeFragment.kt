package com.example.rifsa_mobile.view.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHomeBinding
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult
import com.example.rifsa_mobile.view.fragment.finance.FinanceFragment.Companion.page_key
import com.example.rifsa_mobile.view.fragment.harvestresult.HarvetResultFragment
import com.example.rifsa_mobile.view.fragment.harvestresult.adapter.HarvestResultRvAdapter
import com.example.rifsa_mobile.view.fragment.harvestresult.insert.HarvestInsertDetailFragment
import com.example.rifsa_mobile.view.fragment.home.HomeFragment.Companion.detail_result
import com.example.rifsa_mobile.view.fragment.home.HomeFragment.Companion.page_key
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel : LocalViewModel
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())

        binding.imageView2.setImageResource(R.drawable.mockprofile)

        showResult()

        authViewModel.getUserName().observe(viewLifecycleOwner){
            binding.tvhomeName.text = it
        }

        binding.btnHomeHasil.setOnClickListener {

            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToHarvetResultFragment())
        }

        return binding.root
    }


    private fun showResult(){
        viewModel.readHarvestLocal().observe(viewLifecycleOwner){ responList ->
            val adapter = HarvestResultRvAdapter(responList)
            val recview = binding.rvHomeHarvest
            recview.adapter = adapter
            recview.layoutManager = LinearLayoutManager(requireContext())
            adapter.onDetailCallBack(object : HarvestResultRvAdapter.OnDetailCallback{
                override fun onDetailCallback(data: HarvestResult) {
                    val bundle = Bundle()
                    val fragment = HarvestInsertDetailFragment()
                    bundle.putParcelable(detail_result,data)
                    bundle.putString(
                        page_key,
                       page_detail
                    )
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