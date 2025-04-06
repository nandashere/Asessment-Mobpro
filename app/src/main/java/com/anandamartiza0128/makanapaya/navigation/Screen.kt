package com.anandamartiza0128.makanapaya.navigation

// Menyimpan daftar semua screen yang ada di aplikasi

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
}