package com.anandamartiza0128.makanapaya.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "makanan")
data class Makanan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String = "",
    val jenis: String = "",
    val rasa: String = "",
    val tingkatPedas: String = "",
    val tekstur: String = "",
    val imageUri: String = ""
)
