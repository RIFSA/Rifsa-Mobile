package com.example.rifsa_mobile.view.fragment.finance

import android.os.Bundle
import android.util.Log
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
import com.example.rifsa_mobile.databinding.FragmentFinanceBinding
import com.example.rifsa_mobile.model.entity.remote.finance.FinanceResponseData
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.view.fragment.finance.adapter.FinanceRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch


class FinanceFragment : Fragment() {
    private lateinit var binding : FragmentFinanceBinding
    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    private lateinit var dataList: ArrayList<FinanceResponseData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinanceBinding.inflate(layoutInflater)
//        viewModel = ObtainViewModel(requireActivity())

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        dataList = arrayListOf()


        binding.fabFiannceInsert.setOnClickListener {
            findNavController().navigate(
                FinanceFragmentDirections.actionFinanceFragmentToFinanceInsertDetailFragment(null))
        }

        authViewModel.getUserToken().observe(viewLifecycleOwner){ token ->
            showFinanceList(token)
        }

        return binding.root
    }


    private fun showFinanceList(token : String){
        lifecycleScope.launch{
            remoteViewModel.getFinanceRemote(token).observe(viewLifecycleOwner){
                when(it){
                    is FetchResult.Success->{
                        it.data.financeResponseData.forEach { respon ->
                            dataList.add(respon)

                            val adapter = FinanceRecyclerViewAdapter(dataList)
                            val recyclerView = binding.rvFinance
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(requireContext())

                            adapter.onItemCallBack(object : FinanceRecyclerViewAdapter.ItemDetailCallback{
                                override fun onItemCallback(data: FinanceResponseData) {
                                    findNavController().navigate(
                                        FinanceFragmentDirections.actionFinanceFragmentToFinanceInsertDetailFragment(data))
                                }
                            })
                            if (dataList.isEmpty()){
                                binding.financeEmptyState.emptyState.visibility =
                                    View.VISIBLE
                            }
                        }
                    }
                    is FetchResult.Error->{
                        showToast(it.error)
                        Log.d("Read Finance Result",it.error)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showToast(title : String){
        Toast.makeText(requireContext(),title, Toast.LENGTH_SHORT).show()
    }
}