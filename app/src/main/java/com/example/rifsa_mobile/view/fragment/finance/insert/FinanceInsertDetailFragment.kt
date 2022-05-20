package com.example.rifsa_mobile.view.fragment.finance.insert

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentFinanceInsertDetailBinding
import com.example.rifsa_mobile.model.entity.finance.Finance
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.view.fragment.finance.FinanceFragment
import com.example.rifsa_mobile.view.fragment.finance.FinanceFragment.Companion.detail_finance
import com.example.rifsa_mobile.view.fragment.finance.FinanceFragmentDirections
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class FinanceInsertDetailFragment : Fragment() {
    private lateinit var binding : FragmentFinanceInsertDetailBinding
    private lateinit var viewModel : LocalViewModel

    private var formatDate = SimpleDateFormat("dd-MMM-yyy", Locale.ENGLISH)

    private var randomId = Utils.randomId()
    private var currentDate = LocalDate.now().toString()
    private var type = ""
    private var isDetail = false
    private var detailId = ""
    private var sortId = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinanceInsertDetailBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())
        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.GONE

        try {
            val data = FinanceInsertDetailFragmentArgs.fromBundle(requireArguments()).detailFinance
            if (data != null){
                setDetail(data)
                isDetail = true
                detailId = data.id_finance
                sortId = data.id_sort
                type = data.type
            }
        }catch (e : Exception){ }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinnerfinanceInsert.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
            override fun onNothingSelected(p0: AdapterView<*>?) {
                type = "Pengeluaran"
            }

            override fun onItemSelected(view: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(view?.getItemAtPosition(p2)){
                    "Pengeluaran"-> {
                        type = "Pengeluaran"
                    }
                    "Pemasukan"->{
                        type = "Pemasukan"
                    }
                }
            }
        }

        binding.tvfinanceInsertDate.setOnClickListener {
            datePicker()
        }

        binding.btnHarvestSave.setOnClickListener {
            insertFinanceLocally()
        }

        binding.btnfinanceInsertDelete.setOnClickListener {
            lifecycleScope.launch {
                deleteFinanceLocal()
            }
        }

        binding.btnHarvestdetailBackhome.setOnClickListener {
            findNavController().navigate(
                FinanceInsertDetailFragmentDirections.actionFinanceInsertDetailFragmentToFinanceFragment()
            )
        }


    }

    private fun insertFinanceLocally(){

        if (isDetail){ randomId = detailId }

        val tempInsert = Finance(
            sortId,
            randomId,
            currentDate,
            binding.tvfinanceInsertNama.text.toString(),
            type,
            binding.tvfinanceInsertCatatan.text.toString(),
            binding.tvfinanceInsertHarga.text.toString().toInt(),
            false
        )

        try {
            viewModel.insertFinanceLocal(tempInsert)
            showToast("sukses menambahkan")
            findNavController().navigate(FinanceInsertDetailFragmentDirections.actionFinanceInsertDetailFragmentToFinanceFragment())
        }catch (e : Exception){
            showToast(e.message.toString())
        }
    }



    private fun setDetail(data : Finance){
        val amount = data.amount.toString()
        binding.apply {
            tvfinanceInsertDate.setText(data.date)
            tvfinanceInsertNama.setText(data.title)
            tvfinanceInsertHarga.setText(amount)
            tvfinanceInsertCatatan.setText(data.note)
            btnfinanceInsertDelete.visibility = View.VISIBLE
        }
    }

    private suspend fun deleteFinanceLocal(){
        try {
            viewModel.deleteFinanceLocal(detailId)
            showToast("Berhasil terhapus")
            findNavController().navigate(
                FinanceInsertDetailFragmentDirections.actionFinanceInsertDetailFragmentToFinanceFragment())
        }catch (e : Exception){
            showToast(e.message.toString())
        }
    }


    private fun datePicker(){
        val instance = Calendar.getInstance()
        val datePicker = DatePickerDialog(requireContext(),{_,year,month,dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.YEAR,year)
            val setdate = formatDate.format(calendar.time)
            binding.tvfinanceInsertDate.text = setdate.toString()
            currentDate = setdate
        },
            instance.get(Calendar.YEAR),
            instance.get(Calendar.MONTH),
            instance.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }


    private fun showToast(title : String){
        Toast.makeText(requireContext(),title,Toast.LENGTH_SHORT).show()
    }

}