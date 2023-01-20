package com.example.rifsa_mobile.view.fragment.farming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.databinding.FragmentFieldDetailBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.FieldDetailEntity
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import java.util.*


class FieldDetailFragment : Fragment() {
    private lateinit var binding : FragmentFieldDetailBinding

    private val remoteViewModel : RemoteViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private val authViewModel : UserPrefrencesViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val detailId = UUID.randomUUID().toString()
    private var isDetail = false
    private var currLongitude = 0.0
    private var currLatitude = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFieldDetailBinding.inflate(layoutInflater)

        try {
            val data = FieldDetailFragmentArgs.fromBundle(requireArguments()).dataField
            val longitude = FieldDetailFragmentArgs.fromBundle(requireArguments()).currLongitude
            val latitude = FieldDetailFragmentArgs.fromBundle(requireArguments()).currLatitude
            showDetailData(data)
            isDetail = true
            if (longitude != null && latitude != null) {
                currLongitude = longitude.toDouble()
                currLatitude = latitude.toDouble()
            }
        }catch (e : Exception){}


        binding.btnFieldSave.setOnClickListener {
            insertUpdateField()
        }


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

    private fun showDetailData(data : FieldDetailEntity){
        binding.apply {
            tvfieldInsertName.setText(data.name)
            tvfieldInsertOwner.setText(data.owner)
            tvfieldInsertProduction.setText(data.owner)
            tvfieldInsertAddress.setText(data.address)
        }
    }

    private fun insertUpdateField(){
        val requestBody = FieldDetailEntity(
            detailId,
            binding.tvfieldInsertName.text.toString(),
            binding.tvfieldInsertOwner.text.toString(),
            binding.tvfieldInsertAddress.text.toString(),
            binding.tvfieldInsertOwner.text.toString(),
            currLatitude,
            currLongitude
        )

        authViewModel.getUserId().observe(viewLifecycleOwner){
            binding.btnFieldSave.visibility = View.GONE
            remoteViewModel.insertUpdateFieldData(requestBody,it)
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
        }

    }


}