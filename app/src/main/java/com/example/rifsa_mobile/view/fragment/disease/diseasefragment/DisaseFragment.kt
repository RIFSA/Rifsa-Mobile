package com.example.rifsa_mobile.view.fragment.disease.diseasefragment

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
import com.example.rifsa_mobile.databinding.FragmentDisaseBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseFirebaseEntity
import com.example.rifsa_mobile.view.fragment.disease.adapter.DiseaseRecyclerViewAdapter
import com.example.rifsa_mobile.view.fragment.disease.detail.DiseaseDetailFragmentDirections
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DisaseFragment : Fragment() {
    private lateinit var binding : FragmentDisaseBinding

    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext())  }
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    private var dataList = ArrayList<DiseaseFirebaseEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisaseBinding.inflate(layoutInflater)

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        authViewModel.getUserId().observe(viewLifecycleOwner){ token ->
            diseaseList(token)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabScanDisase.setOnClickListener {
            findNavController().navigate(
                DisaseFragmentDirections.actionDisaseFragmentToCameraFragment(
                    camera_key
                )
            )
        }
        binding.btnMapsDisease.setOnClickListener {
            findNavController().navigate(
                DisaseFragmentDirections.actionDisaseFragmentToMapsDiseaseFragment(
                    map_key
                )
            )
        }
        binding.btnSearchDisease.setOnClickListener {
            findNavController().navigate(
                DisaseFragmentDirections.actionDisaseFragmentToDiseaseBookFragment()
            )
        }
    }

    private fun diseaseList(token : String){
        remoteViewModel.readDiseaseList(token).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    snapshot.children.forEach { child ->
                        child.children.forEach { main ->
                            val data = main.getValue(DiseaseFirebaseEntity::class.java)
                            data?.let { dataList.add(it) }
                            showListDisease(dataList)
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

    private fun showListDisease(data : List<DiseaseFirebaseEntity>){
        try {
            binding.pgStatusBar.visibility = View.GONE
            val adapter = DiseaseRecyclerViewAdapter(data)
            val recyclerView = binding.recviewdisease
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            adapter.onDiseaseDetailCallback(object : DiseaseRecyclerViewAdapter.OnDetailCallback{
                override fun onDetailCallback(data: DiseaseFirebaseEntity) {
                    findNavController().navigate(
                        DisaseFragmentDirections.actionDisaseFragmentToDisaseDetailFragment(
                            null,
                            data,
                            null,
                        )
                    )
                }
            })

        }catch (e : Exception){
            Log.d("DiseaseFragment",e.message.toString())
        }
    }

    private fun showStatus(title : String){
        binding.pgStatusTitle.text = title
        binding.pgStatusTitle.visibility = View.VISIBLE
        if (title.isNotEmpty()){
            binding.pgStatusBar.visibility = View.GONE
        }
    }

    private fun dataChecker(total : Int){
        if (total == 0){
            binding.pgStatusBar.visibility = View.GONE
            binding.diseaseEmptyState.emptyState.visibility = View.VISIBLE
        }
    }

    companion object{
        const val camera_key = "next"
        const val map_key = "disease"
    }

}