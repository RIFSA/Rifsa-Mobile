package com.example.rifsa_mobile.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHomeBinding
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestResponData
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.view.fragment.harvestresult.adapter.HarvestResultRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel : LocalViewModel

    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())
        binding.mainHomeLayout.fullScroll(ScrollView.FOCUS_UP)

        binding.imageView2.setImageResource(R.drawable.mockprofile)


        diseaseCount()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.apply {
            getUserName().observe(viewLifecycleOwner){ name ->
                binding.tvhomeName.text = name
            }
            getUserToken().observe(viewLifecycleOwner){ token ->
                getHarvestRemote(token)
            }
        }

        binding.btnHomeHasil.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToHarvetResultFragment()
            )
        }
    }


    private fun getHarvestRemote(token : String){
        lifecycleScope.launch {
            remoteViewModel.getHarvestRemote(token).observe(viewLifecycleOwner){
                when(it){
                    is FetchResult.Success->{
                        showHarvestList(it.data.harvestResponData)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showHarvestList(data : List<HarvestResponData>){
        val adapter = HarvestResultRecyclerViewAdapter(data)
        val recyclerView = binding.rvHomeHarvest
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.onDetailCallBack(object : HarvestResultRecyclerViewAdapter.OnDetailCallback{
            override fun onDetailCallback(data: HarvestResponData) {
                findNavController().navigate(HomeFragmentDirections
                    .actionHomeFragmentToHarvestInsertDetailFragment(data))
            }
        })
    }

    private fun diseaseCount(){
        authViewModel.getUserToken().observe(viewLifecycleOwner){token->
            lifecycleScope.launch {
                remoteViewModel.getDiseaseRemote(token).observe(viewLifecycleOwner){
                    when(it){
                        is FetchResult.Success->{
                            binding.tvhomeDisasecount.text = it.data.size.toString()
                            binding.cardViewTwo.visibility = View.VISIBLE
                            binding.cardViewOne.visibility = View.VISIBLE
                            binding.tvhomeDisasecount.setOnClickListener {
                                findNavController().navigate(
                                    HomeFragmentDirections.actionHomeFragmentToDisaseFragment()
                                )
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}