package com.example.rifsa_mobile.view.fragment.finance

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentFinanceBinding
import com.example.rifsa_mobile.model.entity.finance.Finance
import com.example.rifsa_mobile.view.fragment.finance.adapter.FinanceRvAdapter
import com.example.rifsa_mobile.view.fragment.finance.insert.FinanceInsertDetailFragment
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel


class FinanceFragment : Fragment() {
    private lateinit var binding : FragmentFinanceBinding
    private lateinit var viewModel : LocalViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinanceBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())

        showFinanceList()

        binding.fabFiannceInsert.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainnav_framgent, FinanceInsertDetailFragment())
                .addToBackStack(null)
                .commit()
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
                    Log.d("detail finance",data.title)
                    val bundle = Bundle()
                    val fragment = FinanceInsertDetailFragment()
                    bundle.putParcelable(detail_finance,data)
                    bundle.putString(page_key, page_detail)
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.mainnav_framgent,fragment)
                        .commit()
                }
            })
        }

    }

    companion object{
        const val page_key = "insert_key"
        const val page_detail = "detail"
        const val detail_finance = "detail_finance"
    }
}