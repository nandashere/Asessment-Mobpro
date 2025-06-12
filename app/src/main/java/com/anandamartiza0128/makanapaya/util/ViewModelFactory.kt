package com.anandamartiza0128.makanapaya.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anandamartiza0128.makanapaya.database.MakananDb
import com.anandamartiza0128.makanapaya.viewmodel.DetailViewModel
import com.anandamartiza0128.makanapaya.viewmodel.MainViewModel
import com.anandamartiza0128.makanapaya.viewmodel.ThemeViewModel
import com.anandamartiza0128.makanapaya.network.UserDataStore // Import ini

class ViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = MakananDb.getInstance(context).dao
        val settingsDataStore = SettingsDataStore(context)
        val userDataStore = UserDataStore(context)

        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(dao, userDataStore) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(dao) as T
            }
            modelClass.isAssignableFrom(ThemeViewModel::class.java) -> {
                ThemeViewModel(settingsDataStore) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}