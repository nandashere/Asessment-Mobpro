package com.anandamartiza0128.makanapaya.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.anandamartiza0128.makanapaya.util.SettingsDataStore
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    // Convert Flow dari datastore ke LiveData untuk konsumsi Compose
    val isDarkTheme: LiveData<Boolean> = settingsDataStore.themeFlow.asLiveData()

    // Toggle dan simpan preferensi ke datastore
    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            settingsDataStore.saveTheme(isDark)
        }
    }
}