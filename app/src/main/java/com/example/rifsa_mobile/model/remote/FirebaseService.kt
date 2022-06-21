package com.example.rifsa_mobile.model.remote

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestFirebaseEntity
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FirebaseService {

    val response = MutableLiveData<HarvestFirebaseEntity>()
    fun authLogin(email : String,password : String): Task<AuthResult>{
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
    }

    fun insertHarvestResult(data : HarvestFirebaseEntity,userId : String): Task<Void>{
        return FirebaseDatabase.getInstance()
                .getReference("data")
                .child(userId)
                .child("hasil")
                .child(data.date)
                .child(data.id)
                .setValue(data)
    }

    fun queryHarvestResult(userId: String): DatabaseReference{
       return FirebaseDatabase.getInstance()
            .getReference("data")
            .child(userId)
            .child("hasil")
//            .addValueEventListener(object : ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot){
//                    snapshot.children.forEach { child ->
//                        child.children.forEach { main ->
//                            val data = main.getValue(HarvestFirebaseEntity::class.java)
//                            data?.let {
//                                response.value = it
//                            }
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                }
//            })

    }

}