package com.example.rifsa_mobile.view.fragment.inventory.insert

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentInvetoryInsertBinding
import com.example.rifsa_mobile.view.fragment.camera.CameraFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File


class InvetoryInsertFragment : Fragment() {
    private lateinit var binding : FragmentInvetoryInsertBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvetoryInsertBinding.inflate(layoutInflater)

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        binding.imgInventory.setOnClickListener {
            setFragmentCamera()
        }

        try {
            showCameraImage()
        }catch (e : Exception){

        }

        return binding.root
    }

    private fun showCameraImage(){
        val arrayFile = requireArguments().getSerializable(invetory_camera_key) as ArrayList<*>
        val file = arrayFile[0] as File
        val result = BitmapFactory.decodeFile(file.path)
        binding.imgInventory.setImageBitmap(result)
    }

    private fun setFragmentCamera(){
        val bundle = Bundle()
        val fragment = CameraFragment()
        bundle.putString(camera_key, camera_key_inventory)
        fragment.arguments = bundle
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainnav_framgent,fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object{
        const val invetory_camera_key = "camera_pic"
        const val camera_key_inventory = "inventory"
        const val camera_key = "camera"
    }


}