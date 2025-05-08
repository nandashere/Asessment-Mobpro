package com.anandamartiza0128.makanapaya.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.anandamartiza0128.makanapaya.R
import com.anandamartiza0128.makanapaya.viewmodel.FoodViewModel
import com.anandamartiza0128.makanapaya.viewmodel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodlistScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val foodList by viewModel.makananList.collectAsState()
    val context = LocalContext.current
    val cerise = Color(ContextCompat.getColor(context, R.color.cerise))
    val yellow = Color(ContextCompat.getColor(context, R.color.yellow))
    val subjectText = stringResource(R.string.foodlist_title)
    val subjectTextx = stringResource(R.string.share_foodlist_via)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.foodlist)) },
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
                    text = stringResource(R.string.no_food_added),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Gray
                )
            } else {
                foodList.forEach { item ->
                    val keywordGabungan = listOfNotNull(
                        item.jenis, item.rasa, item.tingkatPedas, item.tekstur
                    ).joinToString(", ")

                    FoodlistItem(
                        imageUri = item.imageUri.takeIf { it.isNotBlank() }?.let { Uri.parse(it) },
                        name = item.nama,
                        keywords = keywordGabungan,
                        onEditClick = {
                            navController.navigate(Screen.Detail.createRoute(item.id))
                        },
                        onDeleteClick = {
                            viewModel.deleteMakanan(item)
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val shareText = foodList.joinToString("\n\n") { item ->
                            buildString {
                                append("🍽️ ${item.nama}\n")
                                append("• Jenis: ${item.jenis}\n")
                                append("• Rasa: ${item.rasa}\n")
                                append("• Tingkat Pedas: ${item.tingkatPedas}\n")
                                append("• Tekstur: ${item.tekstur}")
                            }
                        }

                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, subjectText)
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }

                        context.startActivity(
                            Intent.createChooser(shareIntent, subjectTextx)
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = yellow,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.share_foodlist))
                }
            }
        }
    }
}

@Composable
fun FoodlistItem(
    imageUri: Uri?,
    name: String,
    keywords: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onEditClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
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

                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = keywords,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onEditClick) {
                    Text("Edit")
                }
                TextButton(onClick = onDeleteClick) {
                    Text("Hapus", color = Color.Red)
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun FoodlistScreenPreview() {
//    val navController = rememberNavController()
//
//    FoodlistScreen(navController = navController)
//}


