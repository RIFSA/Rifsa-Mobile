package com.example.rifsa_mobile.view.fragment.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentInventoryBinding
import com.example.rifsa_mobile.view.fragment.inventory.insert.InvetoryInsertFragment


class InventoryFragment : Fragment() {
    private lateinit var binding : FragmentInventoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInventoryBinding.inflate(layoutInflater)

        binding.fabInventoryAdd.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainnav_framgent, InvetoryInsertFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }


}