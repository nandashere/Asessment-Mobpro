package com.anandamartiza0128.makanapaya.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anandamartiza0128.makanapaya.MainScreen
import com.anandamartiza0128.makanapaya.util.ViewModelFactory
import com.anandamartiza0128.makanapaya.viewmodel.FoodViewModel
import com.anandamartiza0128.makanapaya.viewmodel.MainViewModel

// Menyimpan daftar rute navigasi yang mungkin dari satu screen ke screen lainnya

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)

    val mainViewModel: MainViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }
        composable(route = Screen.Foodlist.route) {
            FoodlistScreen(navController = navController, viewModel = mainViewModel)
        }
        composable(route = Screen.TambahMakanan.route) {
            TambahMakananScreen(navController = navController, viewModel = mainViewModel)
        }
        composable(route = Screen.CariMakanan.route) {
            CariMakananScreen(navController = navController, viewModel = mainViewModel)
        }
    }
}
