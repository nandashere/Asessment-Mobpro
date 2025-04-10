package com.anandamartiza0128.makanapaya.model

import com.anandamartiza0128.makanapaya.R

data class FoodItem(
    val imageResId: Int,
    val nameResId: Int
)

val foodItems = listOf(
    FoodItem(R.drawable.ayam_geprek, R.string.makanan_ayam_geprek),
    FoodItem(R.drawable.gacoan, R.string.makanan_gacoan),
    FoodItem(R.drawable.ayam_penyet, R.string.makanan_ayam_penyet)
)
