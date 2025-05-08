package com.anandamartiza0128.makanapaya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anandamartiza0128.makanapaya.database.MakananDao
import com.anandamartiza0128.makanapaya.model.Makanan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val dao: MakananDao) : ViewModel() {

    private val _uiState = MutableStateFlow(Makanan())
    val uiState: StateFlow<Makanan> = _uiState.asStateFlow()

    fun loadMakananById(id: Long) {
        viewModelScope.launch {
            val makanan = dao.getMakananById(id) // tambahkan fungsi ini di DAO
            _uiState.value = makanan ?: Makanan()
        }
    }

    fun updateField(
        jenis: String,
        rasa: String,
        tingkatPedas: String,
        tekstur: String,
        nama: String,
        imageUri: String
    ) {
        _uiState.value = _uiState.value.copy(
            jenis = jenis,
            rasa = rasa,
            tingkatPedas = tingkatPedas,
            tekstur = tekstur,
            nama = nama,
            imageUri = imageUri
        )
    }


    fun saveMakanan() {
        viewModelScope.launch {
            if (_uiState.value.id.toLong() == 0L) {
                dao.insert(_uiState.value)
            } else {
                dao.update(_uiState.value)
            }
        }
    }

    fun deleteMakanan() {
        viewModelScope.launch {
            dao.delete(_uiState.value)
        }
    }
}