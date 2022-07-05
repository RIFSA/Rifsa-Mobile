package com.example.rifsa_mobile.viewmodel.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rifsa_mobile.injection.Injection
import com.example.rifsa_mobile.model.repository.local.LocalRepository
import com.example.rifsa_mobile.model.repository.remote.RemoteRepository
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository : LocalRepository
    ): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(UserPrefrencesViewModel::class.java) ->{
                UserPrefrencesViewModel(localRepository) as T
            }
            modelClass.isAssignableFrom(RemoteViewModel::class.java)->{
                RemoteViewModel(remoteRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object{
        @Volatile
        private var instance : ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this){
                instance ?: ViewModelFactory(
                    Injection.provideRemoteRepository(),
                    Injection.provideLocalRepository(context)
                )
            }.also { instance = it }
    }
}