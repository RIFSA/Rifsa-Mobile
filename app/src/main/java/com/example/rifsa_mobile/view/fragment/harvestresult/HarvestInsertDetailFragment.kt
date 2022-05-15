package com.example.rifsa_mobile.view.fragment.harvestresult

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHarvestInsertDetailBinding
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult

class HarvestInsertDetailFragment : Fragment() {
    private lateinit var binding : FragmentHarvestInsertDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHarvestInsertDetailBinding.inflate(layoutInflater)


        try {
            val data = arguments?.getParcelable<HarvestResult>("detail_result")

            binding.apply {
                tvharvestInsertName.setText(data?.title)
                tvharvestInsertBerat.setText(data?.weight)
                tvharvestInsertCatatan.setText(data?.weight)
                tvharvestInsertHasil.setText(data?.weight)

                if (data?.title != null){
                    btnharvestInsertDelete.visibility = View.VISIBLE
                }


            }


        }catch (e : Exception){

        }

        return binding.root
    }


}