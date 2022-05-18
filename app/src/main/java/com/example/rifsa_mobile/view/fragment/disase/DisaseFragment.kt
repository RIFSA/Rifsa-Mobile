package com.example.rifsa_mobile.view.fragment.disase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentDisaseBinding
import com.example.rifsa_mobile.view.fragment.camera.CameraFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DisaseFragment : Fragment() {

    private lateinit var binding : FragmentDisaseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDisaseBinding.inflate(layoutInflater)

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        //todo 1.4 POST disase
        binding.fabScanDisase.setOnClickListener {
            val bundle = Bundle()
            val fragment = CameraFragment()
            bundle.putString(camera_key, camera_key_disase)
            fragment.arguments = bundle
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainnav_framgent,fragment)
                .addToBackStack(null)
                .commit()
        }


        return binding.root
    }


    companion object{
        const val invetory_camera_key = "camera_pic"
        const val camera_key = "camera"
        const val camera_key_disase = "disase"
    }

}