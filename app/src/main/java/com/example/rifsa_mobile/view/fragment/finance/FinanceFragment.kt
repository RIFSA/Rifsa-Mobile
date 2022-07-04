package com.example.rifsa_mobile.view.fragment.finance

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
import com.example.rifsa_mobile.databinding.FragmentFinanceBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialFirebaseEntity
import com.example.rifsa_mobile.view.fragment.finance.adapter.FinanceRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


class FinanceFragment : Fragment() {
    private lateinit var binding : FragmentFinanceBinding
    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    private var dataList = ArrayList<FinancialFirebaseEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinanceBinding.inflate(layoutInflater)

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE


        binding.fabFiannceInsert.setOnClickListener {
            findNavController().navigate(
                FinanceFragmentDirections.actionFinanceFragmentToFinanceInsertDetailFragment(null))
        }

        authViewModel.getUserId().observe(viewLifecycleOwner){ userId ->
            binding.pgbFinanceBar.visibility = View.VISIBLE
            getFinanceList(userId)
        }

        return binding.root
    }


    private fun getFinanceList(userId : String){
        lifecycleScope.launch{
            remoteViewModel.readFinancial(userId).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { child ->
                        child.children.forEach { main ->
                            binding.pgbFinanceBar.visibility = View.GONE
                            val data = main.getValue(FinancialFirebaseEntity::class.java)
                            data?.let { dataList.add(data) }
                            showFinancialList(dataList)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showStatus(error.message)
                }
            })
        }
    }

    private fun showFinancialList(data : List<FinancialFirebaseEntity>){
        val adapter = FinanceRecyclerViewAdapter(data)
        val recyclerView = binding.rvFinance
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter.onItemCallBack(object : FinanceRecyclerViewAdapter.ItemDetailCallback{
            override fun onItemCallback(data: FinancialFirebaseEntity) {
                findNavController().navigate(
                    FinanceFragmentDirections.actionFinanceFragmentToFinanceInsertDetailFragment(data))
            }
        })
    }

    private fun showStatus(title : String){
        binding.pgbKeaunganTitle.visibility = View.VISIBLE
        binding.pgbKeaunganTitle.text = title

        if (title.isNotEmpty()){
            binding.pgbFinanceBar.visibility = View.GONE
        }

        Log.d("FinanceFragment",title)
    }
}