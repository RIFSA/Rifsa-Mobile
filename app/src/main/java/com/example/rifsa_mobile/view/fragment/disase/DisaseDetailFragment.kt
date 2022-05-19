package com.example.rifsa_mobile.view.fragment.disase

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.rifsa_mobile.databinding.FragmentDisaseDetailBinding
import com.example.rifsa_mobile.view.fragment.inventory.insert.InvetoryInsertFragment
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import java.io.File


class DisaseDetailFragment : Fragment() {
    private lateinit var binding : FragmentDisaseDetailBinding
    private lateinit var viewModel: LocalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDisaseDetailBinding.inflate(layoutInflater)
        viewModel = ObtainViewModel(requireActivity())



        try {
           showImage()
        }catch (e : Exception){

        }

        binding.btnHarvestSave.setOnClickListener {

        }


        return binding.root
    }

    private fun showImage(){
        val image = DisaseDetailFragmentArgs.fromBundle(requireArguments()).photoDisase
        Glide.with(requireContext())
            .load(image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(binding.imgDisaseDetail)
    }




}