package com.example.rifsa_mobile.view.componen

import com.example.rifsa_mobile.R

enum class SortFinance(
    val title : String,
    val icon : Int
) {
    NameDescending("Nama A-Z", R.drawable.baseline_keyboard_arrow_down_24),
    NameAscending("Nama Z-A",R.drawable.baseline_keyboard_arrow_up_24)
}