package com.example.rifsa_mobile.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHomeBinding
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult
import com.example.rifsa_mobile.view.fragment.harvestresult.adapter.HarvestResultRvAdapter
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
        binding.mainHomeLayout.fullScroll(ScrollView.FOCUS_UP)

        binding.imageView2.setImageResource(R.drawable.mockprofile)


        showResult()

        diseaseCount()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.getUserName().observe(viewLifecycleOwner){
            binding.tvhomeName.text = it
        }

        binding.btnHomeHasil.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToHarvetResultFragment()
            )
        }
    }


    private fun showResult(){
        viewModel.readHarvestLocal().observe(viewLifecycleOwner){ responList ->
            val adapter = HarvestResultRvAdapter(responList)
            val recview = binding.rvHomeHarvest
            recview.adapter = adapter
            recview.layoutManager = LinearLayoutManager(requireContext())
            adapter.onDetailCallBack(object : HarvestResultRvAdapter.OnDetailCallback{
                override fun onDetailCallback(data: HarvestResult) {
                    findNavController().navigate(HomeFragmentDirections
                        .actionHomeFragmentToHarvestInsertDetailFragment(data))
                }
            })
        }
    }

    private fun diseaseCount(){
        viewModel.readDiseaseLocal().observe(viewLifecycleOwner){ respon ->
            if (respon.isNotEmpty()){
                binding.cardViewTwo.visibility = View.VISIBLE

                val count = respon.size.toString()
                binding.tvhomeDisasecount.text = count

                binding.tvhomeDisasecount.setOnClickListener {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToDisaseFragment()
                    )
                }
            }
        }
    }
}