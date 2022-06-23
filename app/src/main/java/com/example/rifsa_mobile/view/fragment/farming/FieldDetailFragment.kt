package com.example.rifsa_mobile.view.fragment.farming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentFieldDetailBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.FarmingFirebaseEntity


class FieldDetailFragment : Fragment() {
    private lateinit var binding : FragmentFieldDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFieldDetailBinding.inflate(layoutInflater)

        val data = FieldDetailFragmentArgs.fromBundle(requireArguments()).dataField

        showDetailData(data)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMapsDetailBackhome.setOnClickListener {
            findNavController().navigate(
                FieldDetailFragmentDirections.actionFieldDetailFragmentToProfileFragment()
            )
        }
    }

    private fun showDetailData(data : FarmingFirebaseEntity){
        binding.apply {
            tvfieldInsertName.setText(data.name)
            tvfieldInsertOwner.setText(data.owner)
            tvfieldInsertProduction.setText(data.owner)
            tvfieldInsertAddress.setText(data.address)
        }
    }


}