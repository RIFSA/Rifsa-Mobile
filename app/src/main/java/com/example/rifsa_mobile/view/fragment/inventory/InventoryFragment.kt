package com.example.rifsa_mobile.view.fragment.inventory

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
import com.example.rifsa_mobile.databinding.FragmentInventoryBinding
import com.example.rifsa_mobile.model.entity.remote.inventory.InventoryResultResponData
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.view.fragment.inventory.adapter.InventoryRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch


class InventoryFragment : Fragment() {
    private lateinit var binding : FragmentInventoryBinding


    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    private lateinit var dataList : ArrayList<InventoryResultResponData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInventoryBinding.inflate(layoutInflater)
        dataList = arrayListOf()

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        binding.fabInventoryAdd.setOnClickListener {
            findNavController().navigate(
                InventoryFragmentDirections.actionInventoryFragmentToInvetoryInsertFragment(null)
            )
        }

        authViewModel.getUserToken().observe(viewLifecycleOwner){ token->
            inventoryList(token)
        }


        return binding.root
    }

    private fun inventoryList(token : String){
        lifecycleScope.launch {
            remoteViewModel.getInventoryRemote(token).observe(viewLifecycleOwner){ respon ->
                when(respon){
                    is FetchResult.Loading ->{
                        binding.pgbInventoryBar.visibility = View.VISIBLE
                    }
                    is FetchResult.Success->{
                        binding.pgbInventoryBar.visibility = View.GONE
                        showInventoryList(respon.data.InventoryResultResponData)
                    }
                    is FetchResult.Error ->{
                        showStatus(respon.error)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showInventoryList(data : List<InventoryResultResponData>) {
        val adapter = InventoryRecyclerViewAdapter(data)
        val recyclerView = binding.recviewInventory
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter.onItemDetailCallback(object : InventoryRecyclerViewAdapter.OnDetailItemCallback{
            override fun onDetailCallback(data: InventoryResultResponData) {
                findNavController()
                    .navigate(
                        InventoryFragmentDirections.actionInventoryFragmentToInvetoryInsertFragment(data))
            }
        })
    }

    private fun showStatus(title : String){
        binding.pgbInventoryStatus.text = title
        binding.pgbInventoryStatus.visibility = View.VISIBLE

        if (title.isNotEmpty()){
            binding.pgbInventoryBar.visibility = View.GONE
        }

        Log.d("InventoryFragment",title)
    }

}