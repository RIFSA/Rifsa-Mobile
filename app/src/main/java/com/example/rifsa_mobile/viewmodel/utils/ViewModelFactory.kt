package com.example.rifsa_mobile.viewmodel.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rifsa_mobile.model.repository.Injection
import com.example.rifsa_mobile.model.repository.MainRepository
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val repository: MainRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(UserPrefrencesViewModel::class.java) ->{
                UserPrefrencesViewModel(repository) as T
            }



            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object{
        @Volatile
        private var instance : ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this){
                instance ?: ViewModelFactory(Injection.provideRepostiory(context))
            }.also { instance = it }
    }
}