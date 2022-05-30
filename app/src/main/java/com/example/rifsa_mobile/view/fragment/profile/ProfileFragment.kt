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
import com.example.rifsa_mobile.view.authetication.login.LoginActivity
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
    private lateinit var viewModel :LocalViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())

        authViewModel.getUserName().observe(viewLifecycleOwner){
            binding.tvprofileName.text = it
            binding.tvSignupName.setText(it)
            binding.tvSignupEmail.setText(it)
            binding.tvSignupPassword.setText(it)
        }

        //todo 1.5 Delete local registerData when logout
        binding.btnprofileLogout.setOnClickListener {
            authViewModel.saveUserPrefrences(
                true,
                "",
                ""
            )
            startActivity(Intent(requireContext(),LoginActivity::class.java))
            activity?.finishAffinity()
        }

        showSummary()

        return binding.root
    }

    private fun showSummary() {
        viewModel.apply {
            readHarvestLocal().observe(viewLifecycleOwner) { harvest ->
                harvest.forEach { value ->
                    binding.tvsumHarvestAmount.text = harvest.size.toString()
                    (harvest.sumOf { value.weight }
                        .toString() + "  kg").also { binding.tvsumHarvestWeight.text = it }
                    ("Rp " + harvest.sumOf { value.sellingPrice }
                        .toString()).also { binding.tvsumHarvestHarga.text = it }
                }
            }


            lifecycleScope.launch {
                calculateFinanceLocal("Pengeluaran").observe(viewLifecycleOwner) { finance ->
                        ("Rp " + finance.sumOf { it.amount }.toString()).also { binding.tvsumFinanceOut.text = it }
                }
                calculateFinanceLocal("Pemasukan").observe(viewLifecycleOwner) { finance ->
                    ("Rp " + finance.sumOf { it.amount }.toString()).also { binding.tvsumFinanceIn.text = it }
                }
            }

            this.readInventoryLocal().observe(viewLifecycleOwner){ invetory ->
                binding.tvsumInventoryAmount.text = invetory.size.toString()
            }
        }
    }

}