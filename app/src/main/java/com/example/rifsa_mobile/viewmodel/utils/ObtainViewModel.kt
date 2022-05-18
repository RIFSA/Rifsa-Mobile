package com.example.rifsa_mobile.viewmodel.utils

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.rifsa_mobile.viewmodel.LocalViewModel

fun ObtainViewModel(activity : FragmentActivity): LocalViewModel {
    val factory = ViewModelFactory.getInstance(activity.application)
    return ViewModelProvider(activity,factory)[LocalViewModel::class.java]
}