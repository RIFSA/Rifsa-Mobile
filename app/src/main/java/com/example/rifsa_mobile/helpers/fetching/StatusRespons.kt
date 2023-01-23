package com.example.rifsa_mobile.helpers.fetching

sealed class StatusRespons <out R> private constructor(){
    data class Success<out T>(val data: T): StatusRespons<T>()
    data class Error(val error : String): StatusRespons<Nothing>()
    object Loading : StatusRespons<Nothing>()
}