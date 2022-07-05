package com.example.rifsa_mobile.view.activity.onboarding.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ActivityOnBoardingBinding
import com.example.rifsa_mobile.view.activity.authetication.login.LoginActivity
import com.example.rifsa_mobile.view.activity.onboarding.adapter.OnBoardAdapter
import com.example.rifsa_mobile.model.entity.local.onboarding.OnBoardingPreference


class OnBoarding : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    private var listOnBoarding = ArrayList<OnBoardingPreference>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.also { window.decorView.systemUiVisibility = it }

        listOnBoarding.addAll(getOnBoarding)
        showOnBoarding()
    }

    private val getOnBoarding: ArrayList<OnBoardingPreference>
        @SuppressLint("Recycle")
        get(){
            val photo = resources.obtainTypedArray(R.array.title_pic)
            val title = resources.getStringArray(R.array.title)
            val subTitle = resources.getStringArray(R.array.subTitle)
            val listOnBoard = ArrayList<OnBoardingPreference>()
            for (i in title.indices){
                val onBoard = OnBoardingPreference(
                    photo.getResourceId(i,-1),
                    title[i],
                    subTitle[i]
                )
                listOnBoard.add(onBoard)
            }
            return listOnBoard
        }

    private fun showOnBoarding(){
        val adapter = OnBoardAdapter(listOnBoarding)
        val recview = binding.rvOnboarding
        recview.adapter = adapter
        recview.layoutManager =  LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        binding.btnOnboardNext.setOnClickListener {
            val lastItem = (recview.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            binding.rvOnboarding.smoothScrollToPosition(lastItem + 1)
            if (lastItem == 2){
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }

        }

        binding.btnOnboardSkip.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }

        binding.btnOnboardBack.setOnClickListener {
            val lastItem = (recview.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            binding.rvOnboarding.smoothScrollToPosition(lastItem - 1)
        }
    }

}