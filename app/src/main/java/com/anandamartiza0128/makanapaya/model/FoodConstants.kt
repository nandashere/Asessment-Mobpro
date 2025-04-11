package com.anandamartiza0128.makanapaya.model

import android.content.Context
import com.anandamartiza0128.makanapaya.R

object FoodConstants {
    fun getListJenis(context: Context): List<String> {
        return context.resources.getStringArray(R.array.jenis_makanan).toList()
    }

    fun getListRasa(context: Context): List<String> {
        return context.resources.getStringArray(R.array.rasa_makanan).toList()
    }

    fun getListPedas(context: Context): List<String> {
        return context.resources.getStringArray(R.array.tingkat_pedas).toList()
    }

    fun getListTekstur(context: Context): List<String> {
        return context.resources.getStringArray(R.array.tekstur_makanan).toList()
    }
}