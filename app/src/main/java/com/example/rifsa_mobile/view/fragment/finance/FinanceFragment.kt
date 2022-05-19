package com.example.rifsa_mobile.view.fragment.finance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentFinanceBinding
import com.example.rifsa_mobile.model.entity.finance.Finance
import com.example.rifsa_mobile.view.fragment.finance.adapter.FinanceRvAdapter
import com.example.rifsa_mobile.view.fragment.finance.insert.FinanceInsertDetailFragment
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class FinanceFragment : Fragment() {
    private lateinit var binding : FragmentFinanceBinding
    private lateinit var viewModel : LocalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinanceBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())
        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        showFinanceList()

        binding.fabFiannceInsert.setOnClickListener {
            findNavController().navigate(
                FinanceFragmentDirections.actionFinanceFragmentToFinanceInsertDetailFragment(null))
        }

        return binding.root
    }


    private fun showFinanceList(){
        viewModel.readFinanceLocal().observe(viewLifecycleOwner){ responList ->
            val adapter = FinanceRvAdapter(responList)
            val recview = binding.rvFinance
            recview.adapter = adapter
            recview.layoutManager = LinearLayoutManager(requireContext())

            adapter.onItemCallBack(object : FinanceRvAdapter.ItemDetailCallback{
                override fun onItemCallback(data: Finance) {
                    findNavController().navigate(
                        FinanceFragmentDirections.actionFinanceFragmentToFinanceInsertDetailFragment(data))
                }
            })
        }
    }

    companion object{
        const val page_key = "insert_key"
        const val page_detail_finance = "detail"
        const val detail_finance = "detail_finance"
    }
}