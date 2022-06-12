package com.example.rifsa_mobile.view.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.rifsa_mobile.databinding.FragmentProfileBinding
import com.example.rifsa_mobile.model.entity.remote.finance.FinanceResponseData
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestResponData
import com.example.rifsa_mobile.model.entity.remote.inventory.InventoryResultResponData
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.view.authetication.login.LoginActivity
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding

    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    private val remoteViewModel : RemoteViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        authViewModel.getUserName().observe(viewLifecycleOwner){
            binding.tvprofileName.text = it
            binding.tvSignupEmail.setText(it)
        }

        authViewModel.getPassword().observe(viewLifecycleOwner){
            binding.tvSignupPassword.setText(it)
        }


        binding.btnprofileLogout.setOnClickListener {
            authViewModel.saveUserPrefrences(
                true,
                "",
                "",
                ""
            )
            startActivity(Intent(requireContext(),LoginActivity::class.java))
            activity?.finishAffinity()
        }

        showSummaryRemote()

        return binding.root
    }



    private fun showSummaryRemote(){
        authViewModel.getUserToken().observe(viewLifecycleOwner){token->
            lifecycleScope.launch {
                remoteViewModel.getHarvestRemote(token).observe(viewLifecycleOwner){
                    when(it){
                        is FetchResult.Success->{
                            summaryHarvest(it.data.harvestResponData)
                        }
                        is FetchResult.Error->{

                        }
                        else -> {}
                    }
                }
                remoteViewModel.getFinanceRemote(token).observe(viewLifecycleOwner){
                    when(it){
                        is FetchResult.Success->{
                            summaryFinance(it.data.financeResponseData)
                        }
                        is FetchResult.Error->{

                        }
                        else -> {}
                    }
                }
                remoteViewModel.getInventoryRemote(token).observe(viewLifecycleOwner){
                    when(it){
                        is FetchResult.Success->{
                            summaryInventory(it.data.InventoryResultResponData)
                        }
                        is FetchResult.Error->{

                        }
                        else -> {}
                    }
                }
            }
        }
    }


    private fun summaryHarvest(harvest : List<HarvestResponData>){
        harvest.forEach { value->
            binding.tvsumHarvestAmount.text = harvest.size.toString()
            (harvest.sumOf { value.berat.toInt() }.toString() + "  kg").also { binding.tvsumHarvestWeight.text = it }
            ("Rp " + harvest.sumOf { value.jual.toInt() }.toString()).also { binding.tvsumHarvestHarga.text = it }
        }
    }

    private fun summaryFinance(finance : List<FinanceResponseData>){
        finance.forEach { _ ->
            ("Rp " + finance.sumOf { it.jumlah.toInt() }.toString()).also { binding.tvsumFinanceOut.text = it }
        }
    }

    private fun summaryInventory(data : List<InventoryResultResponData>){
        binding.tvsumInventoryAmount.text = data.size.toString()
    }




}