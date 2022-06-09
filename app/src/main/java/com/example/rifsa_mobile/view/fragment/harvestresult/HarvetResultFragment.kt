package com.example.rifsa_mobile.view.fragment.harvestresult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHarvetResultBinding
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestResponData
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.view.fragment.harvestresult.adapter.HarvestResultRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch


class HarvetResultFragment : Fragment() {
    private lateinit var binding : FragmentHarvetResultBinding

    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    private var isConnected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHarvetResultBinding.inflate(layoutInflater)
        isConnected = Utils.internetChecker(requireContext())


        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabHarvestToinsert.setOnClickListener {
            findNavController().navigate(
                HarvetResultFragmentDirections.actionHarvetResultFragmentToHarvestInsertDetailFragment(null)
            )
        }

        authViewModel.getUserToken().observe(viewLifecycleOwner){
            getResultFromRemote("Bearer $it")
        }



        binding.btnHarvestBackhome.setOnClickListener {
            findNavController().navigate(
                HarvetResultFragmentDirections.actionHarvetResultFragmentToHomeFragment()
            )
        }

    }



    //TODO | ambil data dari remote
    private fun getResultFromRemote(token : String){
        lifecycleScope.launch {
            remoteViewModel.getHarvestRemote(token).observe(viewLifecycleOwner){
                when(it){
                    is FetchResult.Loading->{

                    }
                    is FetchResult.Success->{
                        showResult(it.data.harvestResponData)
                    }
                    is FetchResult.Error ->{

                    }
                }
            }
        }
    }

    private fun showResult(data : List<HarvestResponData>){
        val adapter = HarvestResultRecyclerViewAdapter(data)
        val recyclerView = binding.rvHarvestresult
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.onDetailCallBack(object : HarvestResultRecyclerViewAdapter.OnDetailCallback{
            override fun onDetailCallback(data: HarvestResponData) {
                findNavController().navigate(HarvetResultFragmentDirections
                    .actionHarvetResultFragmentToHarvestInsertDetailFragment(data))
            }
        })
    }



    private fun showToast(title : String){
        Toast.makeText(requireContext(),title, Toast.LENGTH_SHORT).show()
    }



}