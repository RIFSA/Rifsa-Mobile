package com.example.rifsa_mobile.view.fragment.finance.financefragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentFinanceBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.view.componen.SortSpinnerAdapter
import com.example.rifsa_mobile.view.fragment.finance.FinanceViewModel
import com.example.rifsa_mobile.view.fragment.finance.FinancialInsertViewModel
import com.example.rifsa_mobile.view.fragment.finance.adapter.FinanceRecyclerViewAdapter
import com.example.rifsa_mobile.view.fragment.finance.adapter.FinancialPagedAdapter
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class FinanceFragment : Fragment() {
    private lateinit var binding : FragmentFinanceBinding

    private val viewModel : FinancialInsertViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    private var dataList = ArrayList<FinancialEntity>()
    private var popupWindow = PopupWindow()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinanceBinding.inflate(layoutInflater)

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        binding.apply {
            fabFiannceInsert.setOnClickListener {
                findNavController().navigate(
                    FinanceFragmentDirections.actionFinanceFragmentToFinanceInsertDetailFragment(
                        null
                    )
                )
            }
            btnSort.setOnClickListener {
                val spinner = spSort
                if(spinner.visibility == View.VISIBLE){
                    spinner.visibility = View.GONE
                }else{
                    spinner.visibility = View.VISIBLE
                }
            }
        }


        binding.spSort.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                when(position){
                    1 ->{
                        viewModel.readFinancialByNameAsc().observe(viewLifecycleOwner){ data->
                            showFinancialList(data)
                        }
                    }
                    2 ->{
                        viewModel.readFinancialByNameDesc().observe(viewLifecycleOwner) { data ->
                            showFinancialList(data)
                        }
                    }
                    3->{
                        viewModel.readFinancialByPriceAsc().observe(viewLifecycleOwner) { data ->
                            showFinancialList(data)
                        }
                    }
                    4->{
                        viewModel.readFinancialByPriceDesc().observe(viewLifecycleOwner) { data ->
                            showFinancialList(data)
                        }
                    }
                    5->{
                        viewModel.readFinancialByDateDesc().observe(viewLifecycleOwner) { data ->
                            showFinancialList(data)
                        }
                    }
                    6->{
                        viewModel.readFinancialByDateAsc().observe(viewLifecycleOwner) { data ->
                            showFinancialList(data)
                        }
                    }
                    //when nothing sleected
                    else->{
                        viewModel.readFinancialByDateDesc().observe(viewLifecycleOwner) { data ->
                            showFinancialList(data)
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return binding.root
    }


    private fun showFinancialList(data : PagedList<FinancialEntity>){
        try {
            binding.pgbFinanceBar.visibility = View.GONE
            val adapter = FinancialPagedAdapter()
            binding.rvFinance.layoutManager = LinearLayoutManager(context)
            binding.rvFinance.adapter = adapter
            adapter.submitList(data)
        }catch (e : Exception){
            Log.d("FinanceFragment",e.message.toString())
        }
    }

    private fun showStatus(title : String){
        binding.pgbKeaunganTitle.visibility = View.VISIBLE
        binding.pgbKeaunganTitle.text = title
        if (title.isNotEmpty()){
            binding.pgbFinanceBar.visibility = View.GONE
        }
        Log.d("FinanceFragment",title)
    }

    private fun dataChecker(total : Int){
        if (total == 0){
            binding.pgbFinanceBar.visibility = View.GONE
            binding.financeEmptyState.emptyState.visibility = View.VISIBLE
        }
    }
}