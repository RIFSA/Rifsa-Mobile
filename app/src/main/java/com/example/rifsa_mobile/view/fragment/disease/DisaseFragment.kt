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
import com.example.rifsa_mobile.model.entity.remote.disease.restapivm.NewDiseaseResultResponItem
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
                    is FetchResult.Loading ->{
                        binding.pgStatusBar.visibility = View.VISIBLE
                    }
                    is FetchResult.Success -> {
                        binding.pgStatusBar.visibility = View.GONE
                        showListDisease(respon.data)
                    }
                    is FetchResult.Error ->{
                        showStatus(respon.error)
                    }
                    else -> {}
                }
            }

        }
    }

    private fun showListDisease(data : List<NewDiseaseResultResponItem>){
        val adapter = DiseaseRecyclerViewAdapter(data)
        val recyclerView = binding.recviewdisease
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        adapter.onDiseaseDetailCallback(object : DiseaseRecyclerViewAdapter.OnDetailCallback{
            override fun onDetailCallback(data: NewDiseaseResultResponItem) {
                findNavController().navigate(
                    DisaseFragmentDirections
                        .actionDisaseFragmentToDisaseDetailFragment(null,data)
                )
            }
        })
    }

    private fun showStatus(title : String){
        binding.pgStatusTitle.text = title
        binding.pgStatusTitle.visibility = View.VISIBLE

        if (title.isNotEmpty()){
            binding.pgStatusBar.visibility = View.GONE
        }
    }

    companion object{
        const val camera_key = "next"
    }

}