package com.example.rifsa_mobile.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ActivityMainBinding
import com.example.rifsa_mobile.view.fragment.disase.DisaseFragment
import com.example.rifsa_mobile.view.fragment.finance.FinanceFragment
import com.example.rifsa_mobile.view.fragment.home.HomeFragment
import com.example.rifsa_mobile.view.fragment.inventory.InventoryFragment
import com.example.rifsa_mobile.view.fragment.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding.mainBottommenu.visibility = View.VISIBLE

        val navControl = findNavController(R.id.mainnav_framgent)
        binding.mainBottommenu.apply {
            setupWithNavController(navControl)
        }


    }

}