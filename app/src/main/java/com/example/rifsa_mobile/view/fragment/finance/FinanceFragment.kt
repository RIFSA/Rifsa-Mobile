package com.example.rifsa_mobile.view.fragment.finance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentFinanceBinding
import com.example.rifsa_mobile.model.entity.finance.Finance
import com.example.rifsa_mobile.view.fragment.finance.adapter.FinanceRvAdapter
import com.example.rifsa_mobile.view.fragment.finance.insert.FinanceInsertFragment


class FinanceFragment : Fragment() {
    private lateinit var binding : FragmentFinanceBinding

    private var arrayList = ArrayList<Finance>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinanceBinding.inflate(layoutInflater)

        binding.fabFiannceInsert.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainnav_framgent, FinanceInsertFragment())
                .addToBackStack(null)
                .commit()
        }

        arrayList.addAll(financeMock)

        showFinanceList()


        return binding.root
    }




    private val financeMock : ArrayList<Finance>
        get() {
            val title = resources.getStringArray(R.array.hasil_mock)
            val date = resources.getStringArray(R.array.tanggal_mock)
            val listMock = ArrayList<Finance>()
            for (i in title.indices){
                val temp = Finance(
                    i,
                    date[i],
                    title[i],
                    "test",
                    "aaaa",
                    10000
                )
                listMock.add(temp)
            }
            return listMock
        }

    private fun showFinanceList(){
        val adapter = FinanceRvAdapter(arrayList)
        val recview = binding.rvFinance
        recview.adapter = adapter
        recview.layoutManager = LinearLayoutManager(requireContext())


    }

}