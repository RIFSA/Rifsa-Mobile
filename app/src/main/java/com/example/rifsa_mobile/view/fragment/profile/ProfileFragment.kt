package com.example.rifsa_mobile.view.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.rifsa_mobile.databinding.FragmentProfileBinding
import com.example.rifsa_mobile.view.authetication.login.LoginActivity
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory


class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
    private lateinit var viewModel :LocalViewModel

    //todo 1.5 Delete local registerData when logout
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

        binding.btnprofileLogout.setOnClickListener {
            authViewModel.saveUserPrefrences(
                true,
                "",
                ""
            )
            startActivity(Intent(requireContext(),LoginActivity::class.java))
            activity?.finishAffinity()
        }

        return binding.root
    }

    private fun showSummary(){

    }


}