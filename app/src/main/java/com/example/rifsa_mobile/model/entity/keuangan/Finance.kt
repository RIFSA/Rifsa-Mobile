package com.example.rifsa_mobile.model.entity.keuangan

data class Finance(
    val id_finance : Int,
    val date : String,
    val title : String,
    val type : String,
    val note : String,
    val amount : Int
)
