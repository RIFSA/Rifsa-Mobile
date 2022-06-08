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
import com.example.rifsa_mobile.view.fragment.inventory.adapter.InventoryRvAdapter
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch


class InventoryFragment : Fragment() {
    private lateinit var binding : FragmentInventoryBinding
    private lateinit var viewModel: LocalViewModel
    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }

    private lateinit var dataList : ArrayList<InventoryResultResponData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInventoryBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())
        dataList = arrayListOf()

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        binding.fabInventoryAdd.setOnClickListener {
            findNavController().navigate(
                InventoryFragmentDirections.actionInventoryFragmentToInvetoryInsertFragment(null)
            )
        }


        InventoryList()


        return binding.root
    }

    //TODO | gambar tidak tampil dari api karena tidak public
    private fun InventoryList(){
        lifecycleScope.launch {
            remoteViewModel.getInventory().observe(viewLifecycleOwner){ respon ->
                when(respon){
                    is FetchResult.Success->{
                        showInventoryList(respon.data.InventoryResultResponData)
                    }
                    is FetchResult.Error ->{
                        Log.d("iventory read",respon.error)
                    }
                }
            }
        }
    }

    private fun showInventoryList(data : List<InventoryResultResponData>) {
        val adapter = InventoryRvAdapter(data)
        val recview = binding.recviewInventory
        recview.adapter = adapter
        recview.layoutManager = LinearLayoutManager(requireContext())

        adapter.onItemDetailCallback(object : InventoryRvAdapter.OnDetailItemCallback{
            override fun onDetailCallback(data: InventoryResultResponData) {
                findNavController()
                    .navigate(
                        InventoryFragmentDirections.actionInventoryFragmentToInvetoryInsertFragment(data))
            }
        })

//        if (dataList.isEmpty()){
//            binding.inventoryEmptyState.emptyState.visibility =
//                View.VISIBLE
//        }
    }


}