package com.anandamartiza0128.makanapaya

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.anandamartiza0128.makanapaya.navigation.Screen
import com.anandamartiza0128.makanapaya.navigation.SetupNavGraph
import com.anandamartiza0128.makanapaya.ui.theme.MakanApaYaTheme
import com.anandamartiza0128.makanapaya.util.ViewModelFactory
import com.anandamartiza0128.makanapaya.viewmodel.MainViewModel
import com.anandamartiza0128.makanapaya.util.SanityCheckUtil


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sanity check: pastikan context tidak null
        requireNotNull(this) { "Context (MainActivity) is null!" }

        Log.d("SanityCheck", "MainActivity initialized.")

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
    val factory = ViewModelFactory(context)
    val viewModel: MainViewModel = viewModel(factory = factory)

    // Observasi data dari database
    val makananList by viewModel.makananList.collectAsState()

    LaunchedEffect(makananList) {
        SanityCheckUtil.performSanityCheck(context, makananList)
    }

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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Tambahkan gambar di atas tombol
            Image(
                painter = painterResource(id = R.drawable.beomgyu),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .size(120.dp) // ubah ukuran sesuai kebutuhan
            )

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

            // Optional: tampilkan jumlah data makanan (untuk debug/indikasi Room jalan)
            Text(
                text = "Jumlah makanan: ${makananList.size}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 16.dp)
            )
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