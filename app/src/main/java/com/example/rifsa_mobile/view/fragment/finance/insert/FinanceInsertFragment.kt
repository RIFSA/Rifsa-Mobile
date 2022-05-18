package com.example.rifsa_mobile.view.fragment.finance.insert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rifsa_mobile.databinding.FragmentFinanceInsertDetailBinding


class FinanceInsertFragment : Fragment() {
    private lateinit var binding : FragmentFinanceInsertDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinanceInsertDetailBinding.inflate(layoutInflater)
        return binding.root
    }
}