package com.example.rifsa_mobile.view.fragment.disease.diseasefragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentDisaseBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.view.fragment.disease.viewmodel.DiseaseViewModel
import com.example.rifsa_mobile.view.fragment.disease.adapter.DiseaseRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class DisaseFragment : Fragment() {
    private lateinit var binding : FragmentDisaseBinding

    private val viewModel : DiseaseViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisaseBinding.inflate(layoutInflater)

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        viewModel.readDiseaseLocal().observe(viewLifecycleOwner){ respons->
            try {
                dataChecker(respons.size)
                showListDisease(respons)
            }catch (e : Exception){
                Log.d("disease",e.message.toString())
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabScanDisase.setOnClickListener {
            findNavController().navigate(
                DisaseFragmentDirections.actionDisaseFragmentToCameraFragment(
                    camera_key
                )
            )
        }
        binding.btnMapsDisease.setOnClickListener {
            findNavController().navigate(
                DisaseFragmentDirections.actionDisaseFragmentToMapsDiseaseFragment(
                    map_key
                )
            )
        }
        binding.btnSearchDisease.setOnClickListener {
            findNavController().navigate(
                DisaseFragmentDirections.actionDisaseFragmentToDiseaseBookFragment()
            )
        }
    }


    private fun showListDisease(data : List<DiseaseEntity>){
        try {
            binding.pgStatusBar.visibility = View.GONE
            val adapter = DiseaseRecyclerViewAdapter(data)
            val recyclerView = binding.recviewdisease
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            adapter.onDiseaseDetailCallback(object : DiseaseRecyclerViewAdapter.OnDetailCallback{
                override fun onDetailCallback(data: DiseaseEntity) {
                    findNavController().navigate(
                        DisaseFragmentDirections
                            .actionDisaseFragmentToDisaseDetailFragment(data)
                    )
                    Log.d("diseaseId",data.diseaseId)
                }
            })

        }catch (e : Exception){
            Log.d("DiseaseFragment",e.message.toString())
        }
    }

    private fun dataChecker(total : Int){
        if (total == 0){
            binding.pgStatusBar.visibility = View.GONE
            binding.diseaseEmptyState.emptyState.visibility = View.VISIBLE
        }
    }

    companion object{
        const val camera_key = "next"
        const val map_key = "disease"
    }

}