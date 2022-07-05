package com.example.rifsa_mobile.view.fragment.harvestresult.harvestresultfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHarvetResultBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestFirebaseEntity
import com.example.rifsa_mobile.helpers.utils.Utils
import com.example.rifsa_mobile.view.fragment.harvestresult.adapter.HarvestResultRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class HarvetResultFragment : Fragment() {
    private lateinit var binding : FragmentHarvetResultBinding

    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    private var isConnected = false
    private val dataList = ArrayList<HarvestFirebaseEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHarvetResultBinding.inflate(layoutInflater)
        isConnected = Utils.internetChecker(requireContext())


        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        authViewModel.getUserId().observe(viewLifecycleOwner){ token->
            binding.pgbHasilBar.visibility = View.VISIBLE
            remoteViewModel.readHarvestResult(token).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { child ->
                        child.children.forEach { main ->
                            binding.pgbHasilBar.visibility = View.GONE
                            val data = main.getValue(HarvestFirebaseEntity::class.java)
                            data?.let { dataList.add(data) }
                            showResult(dataList)
                            dataChecker(dataList.size)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    binding.pgbHasilBar.visibility = View.GONE
                    showStatus(error.message)
                }
            })
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabHarvestToinsert.setOnClickListener {
            findNavController().navigate(
                HarvetResultFragmentDirections.actionHarvetResultFragmentToHarvestInsertDetailFragment(
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

    private fun showResult(data : List<HarvestFirebaseEntity>){
        val adapter = HarvestResultRecyclerViewAdapter(data)
        val recyclerView = binding.rvHarvestresult
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.onDetailCallBack(object : HarvestResultRecyclerViewAdapter.OnDetailCallback{
            override fun onDetailCallback(data: HarvestFirebaseEntity) {
                findNavController().navigate(
                    HarvetResultFragmentDirections.actionHarvetResultFragmentToHarvestInsertDetailFragment(
                        data
                    )
                )
            }
        })
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
            binding.harvestEmptyState.emptyState.visibility = View.VISIBLE
        }
    }



}