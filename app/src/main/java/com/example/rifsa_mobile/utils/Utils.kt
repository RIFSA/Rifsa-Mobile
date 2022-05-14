package com.example.rifsa_mobile.utils

import android.util.Patterns

object Utils {
    fun CharSequence.validEmailChecker() =
        Patterns
            .EMAIL_ADDRESS.matcher(this)
            .matches()
}