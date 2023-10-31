package com.example.rifsa_mobile.view.fragment.inventory.inventoryfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentInventoryBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.InventoryEntity
import com.example.rifsa_mobile.view.fragment.inventory.InventoryViewModel
import com.example.rifsa_mobile.view.fragment.inventory.adapter.InventoryPagedAdapter
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch


class InventoryFragment : Fragment() {
    private lateinit var binding : FragmentInventoryBinding

    private val remoteViewModel : RemoteViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private val authViewModel : UserPrefrencesViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val inventoryViewModel : InventoryViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    private var dataList = ArrayList<InventoryEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInventoryBinding.inflate(layoutInflater)

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        binding.fabInventoryAdd.setOnClickListener {
            findNavController().navigate(
                InventoryFragmentDirections.actionInventoryFragmentToInvetoryInsertFragment(null,false)
            )
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            spinnerSortData.onItemSelectedListener = object : AdapterView.OnItemClickListener,
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
                   if(parent != null){
                       when(position){
                           1 ->{
                               inventoryViewModel.readInventorySortNameAsc().observe(
                                   viewLifecycleOwner
                               ){ data ->
                                   lifecycleScope.launch {
                                       showInventoryList(data)
                                   }
                               }
                           }
                           2 ->{
                               inventoryViewModel.readInventorySortNameDesc().observe(
                                   viewLifecycleOwner
                               ){ data ->
                                   lifecycleScope.launch {
                                       showInventoryList(data)
                                   }
                               }
                           }
                           3 ->{
                               inventoryViewModel.readInventorySortDateAsc().observe(
                                   viewLifecycleOwner
                               ){ data ->
                                   lifecycleScope.launch {
                                       showInventoryList(data)
                                   }
                               }
                           }
                           4 ->{
                               inventoryViewModel.readInventorySortDateDesc().observe(
                                   viewLifecycleOwner
                               ){ data ->
                                   lifecycleScope.launch {
                                       showInventoryList(data)
                                   }
                               }
                           }
                       }
                   }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {  }
            }
        }
    }

    private suspend fun showInventoryList(data : PagingData<InventoryEntity>) {
        try {
            binding.pgbInventoryBar.visibility = View.GONE
            val adapter = InventoryPagedAdapter()
            val recyclerView = binding.recviewInventory
            adapter.submitData(data)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            adapter.onItemCallBack(object : InventoryPagedAdapter.ItemDetailCallback{
                override fun onItemCallback(data: InventoryEntity) {
                    findNavController().navigate(
                        InventoryFragmentDirections.actionInventoryFragmentToInvetoryInsertFragment(
                            data,
                            true
                        )
                    )
                }
            })
        }catch (e : Exception){
            Log.d("InventoryFragment",e.message.toString())
        }
    }

    private fun showStatus(title : String){
        binding.pgbInventoryStatus.text = title
        binding.pgbInventoryStatus.visibility = View.VISIBLE
        if (title.isNotEmpty()){
            binding.pgbInventoryBar.visibility = View.GONE
        }
        Log.d("InventoryFragment",title)
    }

    private fun dataChecker(total : Int){
        if (total == 0){
            binding.pgbInventoryBar.visibility = View.GONE
            binding.inventoryEmptyState.emptyState.visibility = View.VISIBLE
        }
    }

}