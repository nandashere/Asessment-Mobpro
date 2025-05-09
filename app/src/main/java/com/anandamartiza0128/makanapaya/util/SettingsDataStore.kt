package com.anandamartiza0128.makanapaya.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension Context untuk DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings_preferences"
)

class SettingsDataStore(private val context: Context) {

    companion object {
        private val IS_LIST = booleanPreferencesKey("is_list")
        private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme") // ✅ Tambahan
    }

    // Flow untuk layout list/grid
    val layoutFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LIST] ?: true
    }

    suspend fun saveLayout(isList: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LIST] = isList
        }
    }

    // ✅ Flow untuk tema gelap/terang
    val themeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_DARK_THEME] ?: false // default: light theme
    }

    // ✅ Simpan preferensi tema
    suspend fun saveTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDark
        }
    }
}