package com.example.rifsa_mobile.view.activity.authetication.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ActivitySignUpBinding
import com.example.rifsa_mobile.view.activity.MainActivity
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding

    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(this) }
    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this,R.color.main_color)

        binding.btnSignupEnter.setOnClickListener {
            registerNewUser()
        }

    }

    private fun boxChecker(): Boolean{
        val name = binding.tvSignupName.text.toString()
        val email = binding.tvSignupEmail.text.toString()
        val password = binding.tvSignupPassword.text.toString()

        when{
            email.isEmpty() -> return true
            name.isEmpty() -> return true
            password.isEmpty() -> return true
            else -> (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty())
        }
            return false
    }


    private fun registerNewUser(){
        val email = binding.tvSignupEmail.text.toString()
        val password = binding.tvSignupPassword.text.toString()

        if (!boxChecker()){
            remoteViewModel.authSignUp(email, password)
                .addOnSuccessListener {
                    saveLoginSession(
                        onBoard = true,
                        email,
                        "",
                        it.user?.uid.toString()
                    )
                }
                .addOnFailureListener {
                    showStatus(it.message.toString())
                }
        }else{
            showStatus("Oops! Box masih kosong")
        }
    }


    private fun saveLoginSession(onBoard : Boolean, name : String, pass: String, userId : String){
        authViewModel.saveUserPrefrences(onBoard,name,pass,userId)
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    private fun showStatus(title : String){
        binding.pgtitleRegister.visibility = View.VISIBLE
        binding.pgtitleRegister.text = title
    }
}