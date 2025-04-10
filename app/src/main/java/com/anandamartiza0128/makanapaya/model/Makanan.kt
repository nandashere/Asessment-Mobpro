package com.anandamartiza0128.makanapaya.model

import android.net.Uri

data class Makanan(
    val nama: String = "",
    val jenis: String = "",
    val rasa: String = "",
    val tingkatPedas: String = "",
    val tekstur: String = "",
    val imageUri: Uri? = null
)
