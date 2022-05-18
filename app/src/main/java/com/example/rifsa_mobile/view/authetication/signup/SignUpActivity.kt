package com.example.rifsa_mobile.view.authetication.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.rifsa_mobile.R

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        window.statusBarColor = ContextCompat.getColor(this,R.color.main_color)

    }


    //todo 1.3 Sign up main
    private fun signup(){

    }
}