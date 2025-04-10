package com.anandamartiza0128.makanapaya.navigation

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.anandamartiza0128.makanapaya.R
import com.anandamartiza0128.makanapaya.model.Makanan
import com.anandamartiza0128.makanapaya.viewmodel.FoodViewModel
import androidx.lifecycle.viewmodel.compose.viewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodlistScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: FoodViewModel = viewModel()
) {
    val foodList = viewModel.foodList
    val context = LocalContext.current
    val cerise = Color(ContextCompat.getColor(context, R.color.cerise))
    val yellow = Color(ContextCompat.getColor(context, R.color.yellow))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.foodlist))
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.icon_back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = cerise,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (foodList.isEmpty()) {
                Text(
                    text = "Belum ada makanan ditambahkan.",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Gray
                )
            } else {
                foodList.forEach { item ->
                    FoodlistItem(
                        imageUri = item.imageUri,
                        name = item.nama
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Contoh dummy: nambahin makanan baru
                    val makananBaru = Makanan(
                        nama = "Makanan Baru",
                        imageUri = null // atau Uri.parse("...") kalau dari galeri
                    )
                    viewModel.addMakanan(makananBaru)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = yellow,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(200.dp)
            ) {
                Text(text = "Tambah Makanan")
            }
        }
    }
}

@Composable
fun FoodlistItem(
    imageUri: Uri?,
    name: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = name,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = name,
                    modifier = Modifier.size(80.dp),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun FoodlistScreenPreview() {
    val navController = rememberNavController()

    FoodlistScreen(navController = navController)
}


