package com.example.rifsa_mobile.view.fragment.disease

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentDisaseBinding
import com.example.rifsa_mobile.model.entity.remote.disease.DiseaseResultDataResponse
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.view.fragment.disease.adapter.DiseaseRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class DisaseFragment : Fragment() {
    private lateinit var binding : FragmentDisaseBinding

    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext())  }
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisaseBinding.inflate(layoutInflater)

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        authViewModel.getUserToken().observe(viewLifecycleOwner){ token ->
            diseaseList(token)
        }


        //todo 1.2 POST disease
        binding.fabScanDisase.setOnClickListener {
           findNavController().navigate(
               DisaseFragmentDirections.actionDisaseFragmentToCameraFragment(camera_key)
           )
        }

        binding.btnMapsDisease.setOnClickListener {
            findNavController().navigate(
                DisaseFragmentDirections.actionDisaseFragmentToMapsDiseaseFragment()
            )
        }


        return binding.root
    }

    private fun diseaseList(token : String){
        lifecycleScope.launch {
            remoteViewModel.getDiseaseRemote(token).observe(viewLifecycleOwner){ respon ->
                when(respon){
                    is FetchResult.Success -> {
                        showListDisease(respon.data.DiseaseResultDataResponse)
                    }
                    is FetchResult.Error ->{

                    }
                    else -> {}
                }
            }

        }
    }

    private fun showListDisease(data : List<DiseaseResultDataResponse>){
        val adapter = DiseaseRecyclerViewAdapter(data)
        val recyclerView = binding.recviewdisease
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        adapter.onDiseaseDetailCallback(object : DiseaseRecyclerViewAdapter.OnDetailCallback{
            override fun onDetailCallback(data: DiseaseResultDataResponse) {
                findNavController().navigate(
                    DisaseFragmentDirections
                        .actionDisaseFragmentToDisaseDetailFragment(null,data)
                )
            }
        })
    }


    companion object{
        const val camera_key = "next"
    }

}