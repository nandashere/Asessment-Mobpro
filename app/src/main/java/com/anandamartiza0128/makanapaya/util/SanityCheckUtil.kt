package com.anandamartiza0128.makanapaya.util

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.anandamartiza0128.makanapaya.R
import com.anandamartiza0128.makanapaya.model.Makanan

object SanityCheckUtil {

    fun performSanityCheck(context: Context, makananList: List<Makanan>) {
        // Check context
        requireNotNull(context) { "Context is null in sanity check" }

        // Check makananList
        Log.d("SanityCheck", "Jumlah makanan yang dimuat: ${makananList.size}")
        if (makananList.isEmpty()) {
            Log.w("SanityCheck", "Daftar makanan kosong. Mungkin database belum terisi.")
        }

        // Check warna kuning dari resource
        try {
            val yellow = ContextCompat.getColor(context, R.color.yellow)
            require(yellow != 0) { "Warna kuning gagal di-load" }
        } catch (e: Exception) {
            Log.e("SanityCheck", "Gagal memuat warna kuning dari resources", e)
        }

        // Check drawable
        try {
            val id = context.resources.getIdentifier("beomgyu", "drawable", context.packageName)
            require(id != 0) { "Drawable beomgyu tidak ditemukan di resources" }
        } catch (e: Exception) {
            Log.e("SanityCheck", "Gagal memuat drawable beomgyu", e)
        }
    }
}