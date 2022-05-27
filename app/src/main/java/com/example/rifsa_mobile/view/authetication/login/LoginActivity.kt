package com.example.rifsa_mobile.view.authetication.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ActivityLoginBinding
import com.example.rifsa_mobile.model.remote.response.login.LoginBody
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.view.MainActivity
import com.example.rifsa_mobile.view.authetication.signup.SignUpActivity
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(this) }
    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(this) }

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

    //todo 1.2 Login main with authentication
    private fun login(){
        if (binding.tvLoginEmail.text!!.isNotEmpty() && binding.tvLoginPassword.text!!.isNotEmpty()){
            lifecycleScope.launch {
                postLogin()
            }
        }
    }

    private suspend fun postLogin(){
        val tempForm = LoginBody(
            binding.tvLoginEmail.text.toString(),
            binding.tvLoginPassword.text.toString()
        )
        remoteViewModel.postLogin(tempForm).observe(this){
            when(it){
                is FetchResult.Loading->{
                    binding.pgbarLogin.visibility = View.VISIBLE
                }
                is FetchResult.Success->{
                    binding.pgbarLogin.visibility = View.GONE
                    saveLoginSession(
                        true,
                        binding.tvLoginEmail.text.toString(),
                        it.data.token
                    )
                    showToast("Selamat datang")
                    startActivity(Intent(this,MainActivity::class.java))
                    finishAffinity()
                }
                is FetchResult.Error->{
                    binding.pgbarLogin.visibility = View.GONE
                    showToast(it.error)
                }
            }
        }

    }

    private fun showToast(title : String){
        Toast.makeText(this,title,Toast.LENGTH_SHORT).show()
    }

    private fun saveLoginSession(onBoard : Boolean,name : String,token : String){
        authViewModel.saveUserPrefrences(onBoard,name,token)
    }
}