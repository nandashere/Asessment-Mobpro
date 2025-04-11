package com.anandamartiza0128.makanapaya.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.anandamartiza0128.makanapaya.model.Makanan

// di package viewmodel
class FoodViewModel : ViewModel() {

    // Ini list makanan yang akan diamati oleh Compose
    private val _foodList = mutableStateListOf<Makanan>()
    val foodList: List<Makanan> = _foodList

    fun addMakanan(makanan: Makanan) {
        _foodList.add(makanan)
    }

    fun cariRekomendasi(jenis: String, rasa: String, tingkatPedas: String, tekstur: String): List<Makanan> {
        return foodList
            .map { makanan ->
                // Hitung jumlah kecocokan
                var skor = 0
                if (makanan.jenis.equals(jenis, ignoreCase = true)) skor++
                if (makanan.rasa.equals(rasa, ignoreCase = true)) skor++
                if (makanan.tingkatPedas.equals(tingkatPedas, ignoreCase = true)) skor++
                if (makanan.tekstur.equals(tekstur, ignoreCase = true)) skor++

                // Pair makanan dengan skornya
                Pair(makanan, skor)
            }
            .filter { it.second > 0 } // Ambil yang punya skor > 0 (ada kecocokan)
            .sortedByDescending { it.second } // Urutkan dari skor tertinggi
            .map { it.first } // Ambil objek makanannya aja
    }

}


