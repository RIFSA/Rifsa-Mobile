package com.example.rifsa_mobile.view.fragment.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentInventoryBinding
import com.example.rifsa_mobile.model.entity.inventory.Inventory
import com.example.rifsa_mobile.view.fragment.inventory.adapter.InventoryRvAdapter
import com.example.rifsa_mobile.view.fragment.inventory.insert.InvetoryInsertFragment
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class InventoryFragment : Fragment() {
    private lateinit var binding : FragmentInventoryBinding
    private lateinit var viewModel: LocalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInventoryBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())
        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        binding.fabInventoryAdd.setOnClickListener {
            findNavController().navigate(
                InventoryFragmentDirections.actionInventoryFragmentToInvetoryInsertFragment(null)
            )
        }


        showListInventory()


        return binding.root
    }


    private fun showListInventory(){
        viewModel.readInventoryLocal().observe(viewLifecycleOwner){responList->
            val adapter = InventoryRvAdapter(responList)
            val recview = binding.recviewInventory
            recview.adapter = adapter
            recview.layoutManager = LinearLayoutManager(requireContext())

            adapter.onItemDetailCallback(object : InventoryRvAdapter.OnDetailItemCallback{
                override fun onDetailCallback(data: Inventory) {
                    findNavController()
                        .navigate(
                            InventoryFragmentDirections.actionInventoryFragmentToInvetoryInsertFragment(data))
                }
            })

            if (responList.isEmpty()){
                binding.inventoryEmptyState.emptyState.visibility =
                    View.VISIBLE
            }
        }
    }




}