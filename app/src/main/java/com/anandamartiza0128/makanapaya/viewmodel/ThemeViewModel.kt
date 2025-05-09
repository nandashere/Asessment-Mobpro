package com.anandamartiza0128.makanapaya.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.anandamartiza0128.makanapaya.util.SettingsDataStore

class ThemeViewModel(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    // Convert Flow dari datastore ke LiveData untuk konsumsi Compose
    val isDarkTheme: LiveData<Boolean> = settingsDataStore.themeFlow.asLiveData()
}