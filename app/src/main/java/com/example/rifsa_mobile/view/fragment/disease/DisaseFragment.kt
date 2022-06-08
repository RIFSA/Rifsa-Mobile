package com.example.rifsa_mobile.view.fragment.disease

import android.os.Bundle
import android.util.Log
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
import com.example.rifsa_mobile.view.fragment.disease.adapter.DiseaseRvAdapter
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class DisaseFragment : Fragment() {
    private lateinit var binding : FragmentDisaseBinding
    private lateinit var localviewModel: LocalViewModel
    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext())  }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisaseBinding.inflate(layoutInflater)
        localviewModel = ObtainViewModel(requireActivity())

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        diseaseList()

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

    private fun diseaseList(){
        lifecycleScope.launch {
            remoteViewModel.getDisease().observe(viewLifecycleOwner){ respon ->
                when(respon){
                    is FetchResult.Success -> {
                        showListDisease(respon.data.DiseaseResultDataResponse)
                    }
                    is FetchResult.Error ->{

                    }
                }
            }

        }
    }

    private fun showListDisease(data : List<DiseaseResultDataResponse>){
        Log.d("Ok recview",data[2].indikasi)
        val adapter = DiseaseRvAdapter(data)
        val recview = binding.recviewdisease
        recview.adapter = adapter
        recview.layoutManager = LinearLayoutManager(requireContext())


        adapter.onDiseaseDetailCallback(object : DiseaseRvAdapter.OnDetailCallback{
            override fun onDetailCallback(data: DiseaseResultDataResponse) {
//                findNavController().navigate(
//                    DisaseFragmentDirections.actionDisaseFragmentToDisaseDetailFragment(null,data)
//                )
            }
        })
    }


    companion object{
        const val camera_key = "next"
    }

}