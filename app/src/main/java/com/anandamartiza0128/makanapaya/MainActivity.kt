package com.anandamartiza0128.makanapaya

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.anandamartiza0128.makanapaya.navigation.Screen
import com.anandamartiza0128.makanapaya.navigation.SetupNavGraph
import com.anandamartiza0128.makanapaya.ui.theme.MakanApaYaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MakanApaYaTheme {
                SetupNavGraph()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current                                           // akses resources dari Android framework(color.xml, string.xml,dll.)
    val ceriseColor = Color(ContextCompat.getColor(context, R.color.cerise))    // pakai warna dari file colors.xml
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = ceriseColor,
                    titleContentColor = Color.White,
                ),
            )
        }
    ) { innerPadding ->
        ScreenContent(
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val yellowColor = Color(ContextCompat.getColor(context, R.color.yellow))

    // Menempatkan tombol-tombol di tengah layar
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp) // jarak antar tombol
        ) {
            // Tombol Tambah Makanan
            Button(
                onClick = {
                    navController.navigate(Screen.TambahMakanan.route)
                },
                colors = buttonColors(
                    containerColor = yellowColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier.width(165.dp)
            ) {
                Text(text = stringResource(R.string.tombol_tambah_makanan))
            }

            // Tombol Cari Makanan
            Button(
                onClick = {
                    navController.navigate(Screen.CariMakanan.route)
                },
                colors = buttonColors(
                    containerColor = yellowColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier.width(165.dp)
            ) {
                Text(text = stringResource(R.string.tombol_cari_makanan))
            }

            // Tombol Foodlist Saya
            Button(
                onClick = {
                    navController.navigate(Screen.Foodlist.route)
                },
                colors = buttonColors(
                    containerColor = yellowColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier.width(165.dp)
            ) {
                Text(text = stringResource(R.string.tombol_foodlist))
            }
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode =  Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    MakanApaYaTheme {
        MainScreen(rememberNavController())
    }
}