package com.example.rifsa_mobile.view.splashscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.view.onboarding.OnBoarding

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.statusBarColor = ContextCompat.getColor(this,R.color.main_color)

        Handler(Looper.getMainLooper()).postDelayed({
           sessionChecker()
        },2000)
    }

    private fun sessionChecker(){
        //todo 1.0 session checker login onboard
        startActivity(Intent(this,OnBoarding::class.java))
    }
}