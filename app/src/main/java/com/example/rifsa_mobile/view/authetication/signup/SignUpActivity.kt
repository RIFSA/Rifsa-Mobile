package com.example.rifsa_mobile.view.authetication.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ActivitySignUpBinding
import com.example.rifsa_mobile.model.remote.response.login.LoginBody
import com.example.rifsa_mobile.model.remote.response.signup.RegisterBody
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.view.MainActivity
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import kotlinx.coroutines.launch

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
            lifecycleScope.launch {
                postRegister()
            }
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
            else ->
                (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty())
        }
            return false
    }

    //todo Sign up main with authentication | done
    private suspend fun postRegister(){
        val name = binding.tvSignupName.text.toString()
        val email = binding.tvSignupEmail.text.toString()
        val password = binding.tvSignupPassword.text.toString()


        val tempRegister = RegisterBody(
            name,
            email,
            password,
            password
        )

        if (!boxChecker()){
            remoteViewModel.postRegister(tempRegister).observe(this){
                when(it){
                    is FetchResult.Loading ->{
                        binding.pgbarRegister.visibility = View.VISIBLE
                    }
                    is FetchResult.Success ->{
                        showStatus("berhasil")
                        Log.d("Register page",it.data.message)
                        lifecycleScope.launch {
                            loginAccount(email, password)
                        }
                    }
                    is FetchResult.Error ->{
                        binding.pgbarRegister.visibility = View.GONE
                    }
                }
            }
        }else{
            showStatus("box masih kosong")
        }
    }

    private suspend fun loginAccount(email : String, password : String){
        val tempLogin = LoginBody(email, password)

        remoteViewModel.postLogin(tempLogin).observe(this){
            when(it){
                is FetchResult.Loading ->{
                    binding.pgbarRegister.visibility = View.VISIBLE
                }
                is FetchResult.Success ->{
                    binding.pgbarRegister.visibility = View.VISIBLE
                    authViewModel.saveUserPrefrences(true,email,it.data.token)
                    startActivity(Intent(this,MainActivity::class.java))
                }
                is FetchResult.Error ->{
                    binding.pgbarRegister.visibility = View.GONE
                    showStatus(it.error)
                }
            }
        }
    }

    private fun showStatus(title : String){
        binding.pgtitleRegister.visibility = View.VISIBLE
        binding.pgtitleRegister.text = title
    }
}