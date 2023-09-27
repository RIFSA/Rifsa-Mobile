package com.example.rifsa_mobile.view.fragment.harvestresult.result

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHarvetResultBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.helpers.utils.Utils
import com.example.rifsa_mobile.view.fragment.finance.adapter.FinancialPagedAdapter
import com.example.rifsa_mobile.view.fragment.harvestresult.adapter.HarvestResultPagedAdapter
import com.example.rifsa_mobile.view.fragment.harvestresult.viewmodel.HarvestResultViewModel
import com.example.rifsa_mobile.view.fragment.harvestresult.adapter.HarvestResultRecyclerViewAdapter
import com.example.rifsa_mobile.view.fragment.harvestresult.viewmodel.HarvestInsertViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class HarvetResultFragment : Fragment() {
    private lateinit var binding : FragmentHarvetResultBinding

    private val viewModel : HarvestInsertViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }

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
        binding.apply {
            fabHarvestToinsert.setOnClickListener {
                findNavController().navigate(
                    HarvetResultFragmentDirections
                        .actionHarvetResultFragmentToHarvestInsertDetailFragment(
                            null
                        )
                )
            }
            btnHarvestBackhome.setOnClickListener {
                findNavController().navigate(
                    HarvetResultFragmentDirections.actionHarvetResultFragmentToHomeFragment()
                )
            }

            spSort2.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                   if(parent != null ){
                       when(position){
                           1->{
                               viewModel.readHarvestByNameAsc().observe(viewLifecycleOwner){data->
                                   showResult(data)
                               }
                           }
                           2->{
                               viewModel.readHarvestByNameDesc().observe(viewLifecycleOwner){data->
                                   showResult(data)
                               }
                           }
                           3->{
                               viewModel.readHarvestByPriceAsc().observe(viewLifecycleOwner){data->
                                   showResult(data)
                               }
                           }
                           4->{
                               viewModel.readHarvestByPriceDesc().observe(viewLifecycleOwner){data->
                                   showResult(data)
                               }
                           }
                           5->{
                               viewModel.readHarvestByDateAsc().observe(viewLifecycleOwner){data->
                                   showResult(data)
                               }
                           }
                           6->{
                               viewModel.readHarvestByDateDesc().observe(viewLifecycleOwner){data->
                                   showResult(data)
                               }
                           }
                           7->{
                               viewModel.readHarvestByWeightDesc().observe(viewLifecycleOwner){data->
                                   showResult(data)
                               }
                           }
                           8->{
                               viewModel.readHarvestByWeightAsc().observe(viewLifecycleOwner){data->
                                   showResult(data)
                               }
                           }
                           else->{
                               viewModel.readHarvestByNameAsc().observe(viewLifecycleOwner){data->
                                   showResult(data)
                               }
                           }
                       }
                   }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
        }
    }

    private fun showResult(data : PagedList<HarvestEntity>){
        try {
            binding.pgbHasilBar.visibility = View.GONE
            val adapter = HarvestResultPagedAdapter()
            binding.rvHarvestresult.layoutManager = LinearLayoutManager(context)
            binding.rvHarvestresult.adapter = adapter
            adapter.submitList(data)
            adapter.onItemCallBack(object : HarvestResultPagedAdapter.ItemDetailCallback{
                override fun onItemCallback(data: HarvestEntity) {
                    findNavController().navigate(
                        HarvetResultFragmentDirections.actionHarvetResultFragmentToHarvestInsertDetailFragment(data)
                    )
                }
            })
        }catch (e : Exception){
            Log.d("HarvestFragment",e.message.toString())
        }
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
            binding.pgbHasilBar.visibility = View.GONE
            binding.harvestEmptyState.emptyState.visibility = View.VISIBLE
        }
    }
}