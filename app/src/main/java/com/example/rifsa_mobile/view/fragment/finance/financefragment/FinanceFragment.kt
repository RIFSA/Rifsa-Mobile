package com.example.rifsa_mobile.view.fragment.finance.financefragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentFinanceBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.view.fragment.finance.FinanceViewModel
import com.example.rifsa_mobile.view.fragment.finance.FinancialInsertViewModel
import com.example.rifsa_mobile.view.fragment.finance.adapter.FinanceRecyclerViewAdapter
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

        sortData()

        return binding.root
    }

    private fun sortData(){
        showDefaultListData()
        binding.spSort.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {  }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    when(parent.getItemAtPosition(position)){
                        "Nama A-Z" ->{
                            viewModel.readFinancialByNameAsc().observe(viewLifecycleOwner){ data ->
                                showFinancialList(data)
                                dataChecker(data.size)
                            }
                        }
                        "Nama Z-A" ->{
                            viewModel.readFinancialByNameDesc().observe(viewLifecycleOwner){ data ->
                                showFinancialList(data)
                                dataChecker(data.size)
                            }
                        }
                        "Price A-Z"->{
                            viewModel.readFinancialByPriceAsc().observe(viewLifecycleOwner){ data ->
                                showFinancialList(data)
                                dataChecker(data.size)
                            }
                        }
                        "Price Z-A"->{
                            viewModel.readFinancialByPriceDesc().observe(viewLifecycleOwner){ data ->
                                showFinancialList(data)
                                dataChecker(data.size)
                            }
                        }
                        "Date Old"->{
                            viewModel.readFinancialByDateAsc().observe(viewLifecycleOwner){ data ->
                                showFinancialList(data)
                                dataChecker(data.size)
                            }
                        }
                        "Date New"->{
                            viewModel.readFinancialByDateDesc().observe(viewLifecycleOwner){ data ->
                                showFinancialList(data)
                                dataChecker(data.size)
                            }
                        }
                        else ->{
                            showDefaultListData()
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showDefaultListData()
            }
        }
    }


    private fun showDefaultListData(){
        lifecycleScope.launch{
            try {
                viewModel.readFinancialLocal().observe(viewLifecycleOwner){ data ->
                    showFinancialList(data)
                    dataChecker(data.size)
                }
            }catch (e : Exception){
                Log.d("FinanceFragment",e.toString())
            }
        }
    }

    private fun showFinancialList(data : List<FinancialEntity>){
        try {
            binding.pgbFinanceBar.visibility = View.GONE
            val adapter = FinanceRecyclerViewAdapter(data)
            val recyclerView = binding.rvFinance
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter.onItemCallBack(object : FinanceRecyclerViewAdapter.ItemDetailCallback{
                override fun onItemCallback(data: FinancialEntity) {
                    findNavController().navigate(
                        FinanceFragmentDirections.actionFinanceFragmentToFinanceInsertDetailFragment(data)
                    )
                }
            })
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