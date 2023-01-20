package com.example.rifsa_mobile.view.fragment.inventory.inventoryfragment

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
import com.example.rifsa_mobile.databinding.FragmentInventoryBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.InventoryEntity
import com.example.rifsa_mobile.view.fragment.inventory.adapter.InventoryRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class InventoryFragment : Fragment() {
    private lateinit var binding : FragmentInventoryBinding

    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

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
                InventoryFragmentDirections.actionInventoryFragmentToInvetoryInsertFragment(null)
            )
        }
        authViewModel.getUserId().observe(viewLifecycleOwner){ token->
            inventoryList(token)
        }
        return binding.root
    }

    private fun inventoryList(token : String){
        remoteViewModel.readInventory(token).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    snapshot.children.forEach { child ->
                        child.children.forEach { main ->
                            binding.pgbInventoryBar.visibility = View.GONE
                            val data = main.getValue(InventoryEntity::class.java)
                            data?.let { dataList.add(data) }
                            showInventoryList(dataList)
                            dataChecker(dataList.size)
                        }
                    }
                }else{
                    dataChecker(0)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                showStatus(error.message)
            }
        })
    }

    private fun showInventoryList(data : List<InventoryEntity>) {
        try {
            binding.pgbInventoryBar.visibility = View.GONE
            val adapter = InventoryRecyclerViewAdapter(data)
            val recyclerView = binding.recviewInventory
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            adapter.onItemDetailCallback(object : InventoryRecyclerViewAdapter.OnDetailItemCallback{
                override fun onDetailCallback(data: InventoryEntity) {
                    findNavController().navigate(
                        InventoryFragmentDirections.actionInventoryFragmentToInvetoryInsertFragment(
                            data
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