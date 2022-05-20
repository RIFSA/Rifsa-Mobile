package com.example.rifsa_mobile.view.fragment.harvestresult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHarvetResultBinding
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult
import com.example.rifsa_mobile.view.fragment.harvestresult.adapter.HarvestResultRvAdapter
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class HarvetResultFragment : Fragment() {
    private lateinit var binding : FragmentHarvetResultBinding
    private lateinit var localViewModel: LocalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHarvetResultBinding.inflate(layoutInflater)
        localViewModel = ObtainViewModel(requireActivity())
        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        showResult()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabHarvestToinsert.setOnClickListener {
            findNavController()
                .navigate(HarvetResultFragmentDirections.actionHarvetResultFragmentToHarvestInsertDetailFragment(null))
        }

        binding.btnHarvestBackhome.setOnClickListener {
            findNavController()
                .navigate(
                    HarvetResultFragmentDirections.actionHarvetResultFragmentToHomeFragment()
                )
        }
    }

    private fun showResult(){
        localViewModel.readHarvestLocal().observe(viewLifecycleOwner){ respon ->
            val adapter = HarvestResultRvAdapter(respon)
            val recview = binding.rvHarvestresult
            recview.adapter = adapter
            recview.layoutManager = LinearLayoutManager(requireContext())
            adapter.onDetailCallBack(object : HarvestResultRvAdapter.OnDetailCallback{
                override fun onDetailCallback(data: HarvestResult) {
                    findNavController().navigate(HarvetResultFragmentDirections
                        .actionHarvetResultFragmentToHarvestInsertDetailFragment(data))
                }
            })
            if (respon.isEmpty()){
                binding.harvestEmptyState.emptyState.visibility = View.VISIBLE
            }
        }
    }


}