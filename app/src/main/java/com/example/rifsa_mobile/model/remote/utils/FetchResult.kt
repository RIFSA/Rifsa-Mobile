package com.example.rifsa_mobile.model.remote.utils

sealed class FetchResult <out R> private constructor(){
    data class Success<out T>(val data: T): FetchResult<T>()
    data class Error(val error : String): FetchResult<Nothing>()
    object Loading : FetchResult<Nothing>()
}
