package com.anandamartiza0128.makanapaya.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "makanan")
data class Makanan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String = "",
    val jenis: String = "",
    val rasa: String = "",
    val tingkatPedas: String = "",
    val tekstur: String = "",
    @Json(name = "full_image_url")
    val imageUri: String? = null
)

data class MakananApiResponse(
    val message: String? = null,
    val data: List<Makanan>
)
