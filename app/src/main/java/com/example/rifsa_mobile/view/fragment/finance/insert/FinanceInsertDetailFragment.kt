package com.example.rifsa_mobile.view.fragment.finance.insert

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentFinanceInsertDetailBinding
import com.example.rifsa_mobile.model.entity.local.finance.Finance
import com.example.rifsa_mobile.model.entity.remote.finance.FinancePostBody
import com.example.rifsa_mobile.model.entity.remote.finance.FinanceResponseData
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class FinanceInsertDetailFragment : Fragment() {
    private lateinit var binding : FragmentFinanceInsertDetailBinding
    private lateinit var viewModel : LocalViewModel
    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }

    private var formatDate = SimpleDateFormat("dd-MMM-yyy", Locale.ENGLISH)

    private var randomId = Utils.randomId()
    private var currentDate = LocalDate.now().toString()
    private var type = ""
    private var isDetail = false
    private var detailId = 0
    private var sortId = 0

    private var isConnected = false
    private var status = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinanceInsertDetailBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())

        isConnected = Utils.internetChecker(requireContext())

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.GONE

        try {
            val data = FinanceInsertDetailFragmentArgs.fromBundle(requireArguments()).detailFinance
            if (data != null){
                setDetail(data)
                isDetail = true
                detailId = data.idKeuangan
//                sortId = data.id_sort
//                type = data.type
            }
        }catch (e : Exception){

        }


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

        binding.btnFinanceSave.setOnClickListener {
            postFinance()
        }

        binding.btnfinanceInsertDelete.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setTitle("Hapus data")
                setMessage("apakah anda ingin menghapus data ini ?")
                apply {
                    setPositiveButton("ya") { _, _ ->
                        deleteFinance()
                    }
                    setNegativeButton("tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                }
                create()
                show()
            }
        }

        binding.btnFinanceBackhome.setOnClickListener {
            findNavController().navigate(
                FinanceInsertDetailFragmentDirections.actionFinanceInsertDetailFragmentToFinanceFragment()
            )
        }
    }

    private fun postFinance(){
        val tempData = FinancePostBody(
            currentDate,
            binding.tvfinanceInsertNama.text.toString(),
            type,
            binding.tvfinanceInsertHarga.text.toString(),
            binding.tvfinanceInsertCatatan.text.toString()
        )

        lifecycleScope.launch {
            remoteViewModel.postFinance(tempData).observe(viewLifecycleOwner){
                when(it){
                    is FetchResult.Success ->{
                        status = "Data tersimpan"
                        showToast("Sukses Menambahkan")
                        findNavController().navigate(FinanceInsertDetailFragmentDirections.actionFinanceInsertDetailFragmentToFinanceFragment())
                    }
                    is FetchResult.Error ->{
                        status = "data lokal"
                        Log.d("Insert finance",it.error)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun deleteFinance(){
        lifecycleScope.launch {
            remoteViewModel.deleteFinance(detailId).observe(viewLifecycleOwner){
                when(it) {
                    is FetchResult.Success -> {
                        status = it.data.message
                        showToast("Berhasil Terhapus")
                        Log.d("Test delete", "Berhasil")
                        findNavController().navigate(FinanceInsertDetailFragmentDirections.actionFinanceInsertDetailFragmentToFinanceFragment())
                    }

                    is FetchResult.Error -> {
                        showToast("Gagal Menghapus")
                        status = it.error
                        Log.d("Test delete", it.error)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun insertFinanceLocally(){

        if (isDetail){ randomId = detailId.toString()
        }

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

    @SuppressLint("SetTextI18n")
    private fun setDetail(data : FinanceResponseData){
        val amount = data.jumlah.toString()
        binding.apply {
            tvfinanceInsertDate.text = data.tanggal
            tvfinanceInsertNama.setText(data.kegiatan)
            tvfinanceInsertHarga.setText(amount)
            tvfinanceInsertCatatan.setText(data.catatan)
            btnfinanceInsertDelete.visibility = View.VISIBLE
            tvFinanceInsertdetail.text = "Detail Data"
        }
    }

    private fun deleteFinanceLocal(){
        try {
            viewModel.deleteFinanceLocal(detailId.toString())
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