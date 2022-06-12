package com.example.rifsa_mobile.view.fragment.finance.insert

import android.app.AlertDialog
import android.app.DatePickerDialog
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
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentFinanceInsertDetailBinding
import com.example.rifsa_mobile.model.entity.remote.finance.FinancePostBody
import com.example.rifsa_mobile.model.entity.remote.finance.FinanceResponseData
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class FinanceInsertDetailFragment : Fragment() {
    private lateinit var binding : FragmentFinanceInsertDetailBinding


    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    private var formatDate = SimpleDateFormat("yyy-MM-dd", Locale.ENGLISH)

    private var currentDate = LocalDate.now().toString()
    private var type = ""
    private var isDetail = false
    private var detailId = 0
    private var isConnected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinanceInsertDetailBinding.inflate(layoutInflater)


        isConnected = Utils.internetChecker(requireContext())

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.GONE

        try {
            val data = FinanceInsertDetailFragmentArgs.fromBundle(requireArguments()).detailFinance
            if (data != null){
                setDetail(data)
                isDetail = true
                detailId = data.idKeuangan
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

        binding.btnFinanceSave.setOnClickListener {
            insertUpdateFinanceRemote()
        }

        binding.btnfinanceInsertDelete.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setTitle("Hapus data")
                setMessage("apakah anda ingin menghapus data ini ?")
                apply {
                    setPositiveButton("ya") { _, _ ->
                        deleteFinanceRemote()
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

    private fun insertUpdateFinanceRemote(){
        authViewModel.getUserToken().observe(viewLifecycleOwner){ token ->
            lifecycleScope.launch {
                val tempData = FinancePostBody(
                    currentDate,
                    binding.tvfinanceInsertNama.text.toString(),
                    type,
                    binding.tvfinanceInsertHarga.text.toString(),
                    binding.tvfinanceInsertCatatan.text.toString()
                )

                if (!isDetail){
                    remoteViewModel.postFinanceRemote(tempData,token).observe(viewLifecycleOwner){
                        when(it){
                            is FetchResult.Loading ->{

                            }
                            is FetchResult.Success ->{
                                showStatus(it.data.message)
                                findNavController()
                                    .navigate(FinanceInsertDetailFragmentDirections.actionFinanceInsertDetailFragmentToFinanceFragment())
                            }
                            is FetchResult.Error ->{
                                showStatus(it.error)
                            }
                            else -> {}
                        }
                    }
                }else{
                    remoteViewModel.updateFinanceRemote(detailId, tempData,token).observe(viewLifecycleOwner){
                        when(it){
                            is FetchResult.Loading ->{

                            }
                            is FetchResult.Success ->{
                                showStatus(it.data.message)
                                findNavController()
                                    .navigate(FinanceInsertDetailFragmentDirections.actionFinanceInsertDetailFragmentToFinanceFragment())
                            }
                            is FetchResult.Error ->{
                                showStatus(it.error)
                            }
                            else -> {}
                        }
                    }
                }
            }

        }
    }

    private fun deleteFinanceRemote(){
        authViewModel.getUserToken().observe(viewLifecycleOwner){ token ->
            lifecycleScope.launch {
                remoteViewModel.deleteFinanceRemote(detailId,token).observe(viewLifecycleOwner){
                    when(it) {
                        is FetchResult.Success -> {
                            showStatus(it.data.message)
                            findNavController()
                                .navigate(FinanceInsertDetailFragmentDirections.actionFinanceInsertDetailFragmentToFinanceFragment())
                        }

                        is FetchResult.Error -> {
                            showStatus(it.error)
                        }
                        else -> {}
                    }
                }
            }
        }

    }


    private fun setDetail(data : FinanceResponseData){
        val amount = data.jumlah
        binding.apply {
            tvfinanceInsertDate.text = data.tanggal
            tvfinanceInsertNama.setText(data.kegiatan)
            tvfinanceInsertHarga.setText(amount)
            tvfinanceInsertCatatan.setText(data.catatan)
            btnfinanceInsertDelete.visibility = View.VISIBLE
            "Detail Data".also { tvFinanceInsertdetail.text = it }
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


    private fun showStatus(title : String){
        if(title.isNotEmpty()){
            binding.pgbFinanceStatus.visibility = View.GONE
            binding.pgbFinanceTitle.visibility = View.VISIBLE
        }
        binding.pgbFinanceTitle.text = title
        Log.d(finance_key,"status $title")
    }

    companion object{
        const val finance_key = "FinanceInsertUpdateDetail"
    }

}