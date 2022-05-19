package com.example.rifsa_mobile.view.fragment.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentInventoryBinding
import com.example.rifsa_mobile.model.entity.inventory.Inventory
import com.example.rifsa_mobile.view.fragment.inventory.adapter.InventoryRvAdapter
import com.example.rifsa_mobile.view.fragment.inventory.insert.InvetoryInsertFragment
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel


class InventoryFragment : Fragment() {
    private lateinit var binding : FragmentInventoryBinding
    private lateinit var viewModel: LocalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInventoryBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())

        binding.fabInventoryAdd.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainnav_framgent, InvetoryInsertFragment())
                .addToBackStack(null)
                .commit()
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
                    val bundle = Bundle()
                    val fragment = InvetoryInsertFragment()

                    bundle.putParcelable(detail_inventory,data)
                    bundle.putString(page_key, page_detail_invet)

                    fragment.arguments = bundle

                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainnav_framgent, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            })
        }
    }

    companion object{
        const val page_key = "insert_key"
        const val page_detail_invet = "detail"
        const val detail_inventory = "detail_invetory"
    }


}