package com.example.rifsa_mobile.view.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.view.MainActivity
import com.example.rifsa_mobile.view.authetication.login.LoginActivity
import com.example.rifsa_mobile.view.onboarding.OnBoarding
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory

class SplashScreen : AppCompatActivity() {
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.statusBarColor = ContextCompat.getColor(this,R.color.green)

        Handler(Looper.getMainLooper()).postDelayed({
           sessionChecker()
            finishAffinity()
        },2000)
    }

    //cek session login dari userprefrences
    private fun sessionChecker(){
        authViewModel.getOnBoardStatus().observe(this){
            if (it){
                authViewModel.getUserName().observe(this){
                    if (it.isEmpty()){
                        startActivity(Intent(this,LoginActivity::class.java))
                        finishAffinity()
                    }else{
                        startActivity(Intent(this,MainActivity::class.java))
                        finishAffinity()
                    }
                }
            }else{
                startActivity(Intent(this,OnBoarding::class.java))
                finishAffinity()
            }
        }


    }
}