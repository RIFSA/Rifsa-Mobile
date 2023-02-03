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
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.helpers.utils.Utils
import com.example.rifsa_mobile.view.fragment.finance.FinancialInsertViewModel
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class FinanceInsertDetailFragment : Fragment() {
    private lateinit var binding : FragmentFinanceInsertDetailBinding

    private val viewModel : FinancialInsertViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private val authViewModel : UserPrefrencesViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var formatDate = SimpleDateFormat("yyy-MM-dd", Locale.ENGLISH)

    private var type = ""
    private var firebaseId = ""
    private var currentDate = LocalDate.now().toString()
    private var detailId = UUID.randomUUID().toString()
    private val uploadReminderId = (1..1000).random()
    private var isDetail = false
    private var isConnected = false
    private var isUploaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinanceInsertDetailBinding.inflate(layoutInflater)
        isConnected = Utils.internetChecker(requireContext())
        authViewModel.getUserId().observe(viewLifecycleOwner){ firebaseId = it}

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.GONE

        try {
            val data = FinanceInsertDetailFragmentArgs.fromBundle(requireArguments()).detailFinance
            if (data != null){
                setDetail(data)
                isDetail = true
                detailId = data.idFinance
                currentDate = data.date
            }
        }catch (e : Exception){
            Log.d("financeDetailFragment",e.toString())
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
            if(!isConnected){
                insertFinancialLocally()
            }else{
                insertUpdateFinanceRemote()
            }
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

    private fun setDetail(data : FinancialEntity){
        val amount = data.price
        binding.apply {
            tvfinanceInsertDate.text = data.date
            tvfinanceInsertNama.setText(data.name)
            tvfinanceInsertHarga.setText(amount)
            tvfinanceInsertCatatan.setText(data.noted)
            btnfinanceInsertDelete.visibility = View.VISIBLE
            "Detail Data".also { tvFinanceInsertdetail.text = it }
        }
    }

    private fun insertUpdateFinanceRemote(){
        val tempData = FinancialEntity(
            localId = 0,
            firebaseUserId = "",
            idFinance = detailId,
            date = currentDate,
            name = binding.tvfinanceInsertNama.text.toString(),
            price = binding.tvfinanceInsertHarga.text.toString(),
            type = type,
            noted = binding.tvfinanceInsertCatatan.text.toString(),
            isUploaded = true,
            uploadReminderId = uploadReminderId
        )
        authViewModel.getUserId().observe(viewLifecycleOwner){ userId ->
            lifecycleScope.launch {
                viewModel.insertUpdateFinancial(tempData,userId)
                    .addOnSuccessListener {
                        showStatus("berhasil menambahkan")
                        isUploaded = true
                        insertFinancialLocally()
                    }
                    .addOnFailureListener {
                        isUploaded = false
                        showStatus("gagal menambahkan")
                    }
            }

        }
    }

    private fun deleteFinanceRemote(){
        authViewModel.getUserId().observe(viewLifecycleOwner){ userId ->
            viewModel.deleteFinancial(currentDate,detailId,userId)
                .addOnSuccessListener {
                    showStatus("data terhapus")
                    findNavController().navigate(
                        FinanceInsertDetailFragmentDirections
                            .actionFinanceInsertDetailFragmentToFinanceFragment())
                }
                .addOnFailureListener {
                    showStatus("gagal terhapus")
                }
            Log.d("delete finance","$userId/$currentDate/$detailId")
        }

    }

    private fun datePicker(){
        val instance = Calendar.getInstance()
        val datePicker = DatePickerDialog(requireContext(),{_,year,month,dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.YEAR,year)
            val setDate = formatDate.format(calendar.time)
            binding.tvfinanceInsertDate.text = setDate.toString()
            currentDate = setDate
        },
            instance.get(Calendar.YEAR),
            instance.get(Calendar.MONTH),
            instance.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun insertFinancialLocally(){
        val data = FinancialEntity(
            localId = 0,
            firebaseUserId = firebaseId,
            idFinance = detailId,
            date = currentDate,
            name = binding.tvfinanceInsertNama.text.toString(),
            price = binding.tvfinanceInsertHarga.text.toString(),
            type = type,
            noted = binding.tvfinanceInsertCatatan.text.toString(),
            isUploaded = isUploaded,
            uploadReminderId = uploadReminderId
        )
        lifecycleScope.launch {
            delay(2000)
            try {
                viewModel.insertFinanceLocally(data)
                findNavController().navigate(
                    FinanceInsertDetailFragmentDirections
                        .actionFinanceInsertDetailFragmentToFinanceFragment()
                )
            }catch (e : Exception){
                Log.d("financeDetail",e.toString())
            }
        }
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