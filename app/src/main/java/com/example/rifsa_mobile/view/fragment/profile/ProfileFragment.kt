package com.example.rifsa_mobile.view.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.databinding.FragmentProfileBinding

import com.example.rifsa_mobile.view.activity.authetication.login.LoginActivity
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding

    private val viewModel : ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        viewModel.getUserName().observe(viewLifecycleOwner){
            binding.tvprofileName.text = it
        }

        showSummaryRemote()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnProfileSetting.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToSettingFragment()
            )
        }

        binding.btnprofileLogout.setOnClickListener {
            viewModel.saveUserPrefrences(
                true,
                "",
                "",
                ""
            )
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finishAffinity()
        }

        binding.btnShowFarming.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToMapsDiseaseFragment(map_key)
            )
        }
    }


    private fun showSummaryRemote(){
        var weight = arrayListOf<Int>()
        var price = arrayListOf<Int>()
        var financeResult = arrayListOf<Int>()
        lifecycleScope.launch {
            viewModel.apply {
                readHarvestResult().observe(viewLifecycleOwner){ harvest ->
                    harvest.forEach {
                        weight.add(it.weight.toInt())
                        price.add(it.income.toInt())
                        binding.apply {
                            tvsumHarvestWeight.text = weight.sum().toString()
                            tvsumHarvestAmount.text = harvest.size.toString()
                            tvsumHarvestHarga.text = price.sum().toString()
                        }
                    }
                }
                readFinancial().observe(viewLifecycleOwner){finance->
                    finance.forEach {
                        financeResult.add(it.price.toInt())
                        binding.tvsumFinanceOut.text = financeResult.sum().toString()
                    }
                }
            }

        }
    }

    companion object{
        const val map_key = "profile"
    }


}