package com.example.rifsa_mobile.view.authetication.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ActivityLoginBinding
import com.example.rifsa_mobile.view.MainActivity
import com.example.rifsa_mobile.view.authetication.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this,R.color.main_color)

        binding.btnLoginSignup.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        binding.btnLoginEnter.setOnClickListener {
            login()
        }

    }

    //todo 1.2 Login checker
    private fun login(){
        if (binding.tvLoginEmail.text!!.isNotEmpty() && binding.tvLoginPassword.text!!.isNotEmpty()){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}