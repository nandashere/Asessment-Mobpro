package com.anandamartiza0128.makanapaya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anandamartiza0128.makanapaya.database.MakananDao
import com.anandamartiza0128.makanapaya.model.Makanan
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(dao: MakananDao) : ViewModel() {

    // Mengambil semua data makanan dari DAO, dikonversi jadi StateFlow
    val makananList: StateFlow<List<Makanan>> = dao.getMakanan()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Fungsi cari rekomendasi seperti sebelumnya, tapi pakai makananList dari DB
    fun cariRekomendasi(
        jenis: String,
        rasa: String,
        tingkatPedas: String,
        tekstur: String
    ): List<Makanan> {
        return makananList.value
            .map { makanan ->
                var skor = 0
                if (makanan.jenis.equals(jenis, ignoreCase = true)) skor++
                if (makanan.rasa.equals(rasa, ignoreCase = true)) skor++
                if (makanan.tingkatPedas.equals(tingkatPedas, ignoreCase = true)) skor++
                if (makanan.tekstur.equals(tekstur, ignoreCase = true)) skor++
                Pair(makanan, skor)
            }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
            .map { it.first }
    }

    // Untuk akses satu makanan berdasarkan ID
    fun getMakananById(id: Long): Makanan? {
        return makananList.value.find { it.id.toLong() == id }
    }
}