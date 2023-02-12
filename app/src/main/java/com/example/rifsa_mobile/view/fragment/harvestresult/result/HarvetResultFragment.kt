package com.example.rifsa_mobile.view.fragment.harvestresult.result

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
import com.example.rifsa_mobile.databinding.FragmentHarvetResultBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.helpers.utils.Utils
import com.example.rifsa_mobile.view.fragment.harvestresult.HarvestResultViewModel
import com.example.rifsa_mobile.view.fragment.harvestresult.adapter.HarvestResultRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class HarvetResultFragment : Fragment() {
    private lateinit var binding : FragmentHarvetResultBinding

    private val viewModel : HarvestResultViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    private var isConnected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHarvetResultBinding.inflate(layoutInflater)
        isConnected = Utils.internetChecker(requireContext())

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        lifecycleScope.launch {
            viewModel.readHarvestResult().observe(viewLifecycleOwner){data->
                showResult(data)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabHarvestToinsert.setOnClickListener {
            findNavController().navigate(
                HarvetResultFragmentDirections
                    .actionHarvetResultFragmentToHarvestInsertDetailFragment(
                    null
                )
            )
        }
        binding.btnHarvestBackhome.setOnClickListener {
            findNavController().navigate(
                HarvetResultFragmentDirections.actionHarvetResultFragmentToHomeFragment()
            )
        }

    }

    private fun showResult(data : List<HarvestEntity>){
        try {
            binding.pgbHasilBar.visibility = View.GONE
            val adapter = HarvestResultRecyclerViewAdapter(data)
            val recyclerView = binding.rvHarvestresult
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter.onDetailCallBack(object : HarvestResultRecyclerViewAdapter.OnDetailCallback{
                override fun onDetailCallback(data: HarvestEntity) {
                    findNavController().navigate(
                        HarvetResultFragmentDirections.actionHarvetResultFragmentToHarvestInsertDetailFragment(data)
                    )
                }
            })
        }catch (e : Exception){
            Log.d("HarvestFragment",e.message.toString())
        }
    }

    private fun showStatus(title: String){
        binding.pgbHasilTitle.text = title
        binding.pgbHasilTitle.visibility = View.VISIBLE
        if (title.isNotEmpty()){
            binding.pgbHasilBar.visibility = View.GONE
        }
    }

    private fun dataChecker(total : Int){
        if (total == 0){
            binding.pgbHasilBar.visibility = View.GONE
            binding.harvestEmptyState.emptyState.visibility = View.VISIBLE
        }
    }



}