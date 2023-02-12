package com.example.rifsa_mobile.view.activity.authetication.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ActivityLoginBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.view.activity.MainActivity
import com.example.rifsa_mobile.view.activity.authetication.signup.SignUpActivity
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val dataList = ArrayList<HarvestEntity>()

    private val viewModel : LoginViewModel by viewModels{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this,R.color.main_color)

        binding.btnLoginSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.btnLoginEnter.setOnClickListener {
            lifecycleScope.launch {
                postLogin()
            }
        }

    }


    private fun boxChecker(): Boolean{
        val email = binding.tvLoginEmail.text.toString()
        val password = binding.tvLoginPassword.text.toString()

        when{
            email.isEmpty() -> return true
            password.isEmpty() -> return true
            else -> (email.isNotEmpty() && password.isNotEmpty())
        }
        return false
    }

    private fun postLogin(){

        val email =  binding.tvLoginEmail.text.toString()
        val password = binding.tvLoginPassword.text.toString()

        if (!boxChecker()){
            viewModel.authLogin(email, password)
                .addOnSuccessListener {
                    saveLoginSession(
                        onBoard = true,
                        email,
                        "",
                        it.user?.uid.toString()
                    )
                    retriveHarvestData(it.user?.uid.toString())

                    //todo retrive data from firebase
                }
                .addOnFailureListener {
                    showStatus(it.message.toString())
                }
        }else{
            showStatus("Oops! Box masih kosong")
        }
    }

    private fun showStatus(title : String){
        binding.apply {
            pgtitleLogin.visibility = View.VISIBLE
            pgtitleLogin.text = title
        }
    }

    private fun saveLoginSession(
        onBoard : Boolean,
        name : String,
        pass: String,
        userId : String
    ){
        lifecycleScope.launch {
            viewModel.saveUserPreferences(onBoard,name,pass,userId)
        }

    }

    private fun retriveHarvestData(userId: String){
        viewModel.readHarvestResult(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        child.children.forEach { main ->
                            val data = main.getValue(HarvestEntity::class.java)
                            if (data != null) { dataList.add(data) }
                            Log.d("login_acitivty",dataList[0].typeOfGrain)
                            dataList.forEach { value ->
                                viewModel.insertHarvestLocal(value)
                            }
                        }
                    }
                } else {
                    Log.d("login_acitivty","empty data")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("login_acitivty",error.message.toString())
            }
        })
    }

    private fun retriveFinancialData(userId: String){

    }
}