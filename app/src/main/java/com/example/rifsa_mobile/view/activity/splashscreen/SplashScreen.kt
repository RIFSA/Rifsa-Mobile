package com.example.rifsa_mobile.view.activity.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.view.activity.MainActivity
import com.example.rifsa_mobile.view.activity.authetication.login.LoginActivity
import com.example.rifsa_mobile.view.activity.onboarding.onboarding.OnBoarding
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.statusBarColor = ContextCompat.getColor(this,R.color.main_color)

        Handler(Looper.getMainLooper()).postDelayed({
           sessionChecker()
            finishAffinity()
        },1000)
    }

    private fun sessionChecker(){
        authViewModel.getOnBoardStatus().observe(this){
            if (it){
                authViewModel.getUserName().observe(this){ respon ->
                    if (respon.isEmpty()){
                        startActivity(Intent(this, LoginActivity::class.java))
                        finishAffinity()
                    }else{
                        startActivity(Intent(this, MainActivity::class.java))
                        finishAffinity()
                    }
                }
            }else{
                startActivity(Intent(this, OnBoarding::class.java))
                finishAffinity()
            }
        }


    }
}