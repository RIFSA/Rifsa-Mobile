package com.example.rifsa_mobile.view.fragment.disase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentDisaseBinding
import com.example.rifsa_mobile.model.entity.disase.Disease
import com.example.rifsa_mobile.view.fragment.disase.adapter.DiseaseRvAdapter
import com.example.rifsa_mobile.view.fragment.disase.detail.DisaseDetailFragmentDirections
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class DisaseFragment : Fragment() {
    private lateinit var binding : FragmentDisaseBinding
    private lateinit var viewModel: LocalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisaseBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE


        showListDisease()

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

    private fun showListDisease(){
        viewModel.readDiseaseLocal().observe(viewLifecycleOwner){ respon ->
            val adapter = DiseaseRvAdapter(respon)
            val recview = binding.recviewdisease
            recview.adapter = adapter
            recview.layoutManager = LinearLayoutManager(requireContext())

            if (respon.isEmpty()){
                binding.diseaseEmptyState.emptyState.visibility =
                    View.VISIBLE
            }

            adapter.onDiseaseDetailCallback(object : DiseaseRvAdapter.OnDetailCallback{
                override fun onDetailCallback(data: Disease) {

                }
            })
        }
    }


    companion object{
        const val camera_key = "next"
    }

}