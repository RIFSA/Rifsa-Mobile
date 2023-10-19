package com.example.rifsa_mobile.view.fragment.disease.diseasefragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentDisaseBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.view.fragment.disease.adapter.DiseasePagedAdapter
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


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
           fabScanDisase.setOnClickListener {
                findNavController().navigate(
                    DisaseFragmentDirections.actionDisaseFragmentToCameraFragment(
                        camera_key
                    )
                )
            }
            btnMapsDisease.setOnClickListener {
                findNavController().navigate(
                    DisaseFragmentDirections.actionDisaseFragmentToMapsDiseaseFragment(
                        map_key
                    )
                )
            }
            btnSearchDisease.setOnClickListener {
                findNavController().navigate(
                    DisaseFragmentDirections.actionDisaseFragmentToDiseaseBookFragment()
                )
            }
            spinDiseaseSort.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener{
                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) { }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (parent != null){
                        when(position){
                            1 ->{
                                viewModel.readDiseaseSortNameAsc().observe(
                                    viewLifecycleOwner
                                ){data->
                                    showListDisease(data)
                                }
                            }
                            2 ->{
                                viewModel.readDiseaseSortNameDesc().observe(
                                    viewLifecycleOwner
                                ){data->
                                    showListDisease(data)
                                }
                            }
                            3 ->{
                                viewModel.readDiseaseSortDateAsc().observe(
                                    viewLifecycleOwner
                                ){data->
                                    showListDisease(data)
                                }
                            }
                            4 ->{
                                viewModel.readDiseaseSortDateDesc().observe(
                                    viewLifecycleOwner
                                ){data->
                                    showListDisease(data)
                                }
                            }
                            else->{
                                viewModel.readDiseaseSortNameAsc().observe(
                                    viewLifecycleOwner
                                ){data->
                                    showListDisease(data)
                                }
                            }
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }


    private fun showListDisease(data : PagedList<DiseaseEntity>){
        try {
            binding.pgStatusBar.visibility = View.GONE
            val adapter = DiseasePagedAdapter()
            adapter.submitList(data)
            val recyclerView = binding.recviewdisease
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            adapter.onItemCallBack(object : DiseasePagedAdapter.ItemDetailCallback{

                override fun onItemCallback(data: DiseaseEntity) {
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