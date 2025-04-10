package com.anandamartiza0128.makanapaya.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.anandamartiza0128.makanapaya.model.Makanan

// di package viewmodel
class FoodViewModel : ViewModel() {
    private val _foodList = mutableStateListOf<Makanan>()
    val foodList: List<Makanan> get() = _foodList

    fun addMakanan(makanan: Makanan) {
        _foodList.add(makanan)
    }
}
