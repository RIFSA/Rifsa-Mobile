package com.example.rifsa_mobile.view.fragment.disase

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentDisaseDetailBinding
import com.example.rifsa_mobile.view.fragment.inventory.insert.InvetoryInsertFragment
import java.io.File


class DisaseDetailFragment : Fragment() {
    private lateinit var binding : FragmentDisaseDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDisaseDetailBinding.inflate(layoutInflater)



        try {
            showImageResult()
        }catch (e : Exception){

        }


        return binding.root
    }


    private fun showImageResult(){
        val arrayFile = requireArguments().getSerializable(InvetoryInsertFragment.invetory_camera_key) as ArrayList<*>
        val file = arrayFile[0] as File
        val result = BitmapFactory.decodeFile(file.path)
        binding.imgDisaseDetail.setImageBitmap(result)
    }

}