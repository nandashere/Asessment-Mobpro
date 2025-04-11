package com.anandamartiza0128.makanapaya.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anandamartiza0128.makanapaya.MainScreen
import com.anandamartiza0128.makanapaya.viewmodel.FoodViewModel

// Menyimpan daftar rute navigasi yang mungkin dari satu screen ke screen lainnya

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    val foodViewModel: FoodViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }
        composable(route = Screen.Foodlist.route) {
            FoodlistScreen(navController = navController, viewModel = foodViewModel)
        }
        composable(route = Screen.TambahMakanan.route) {
            TambahMakananScreen(navController = navController, viewModel = foodViewModel)
        }
        composable(route = Screen.CariMakanan.route) {
            CariMakananScreen(navController = navController, viewModel = foodViewModel)
        }
    }
}
