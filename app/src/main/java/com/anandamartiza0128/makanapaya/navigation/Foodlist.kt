package com.anandamartiza0128.makanapaya.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.anandamartiza0128.makanapaya.R
import com.anandamartiza0128.makanapaya.model.Makanan
import com.anandamartiza0128.makanapaya.network.MakananApi
import com.anandamartiza0128.makanapaya.viewmodel.MainViewModel
import com.anandamartiza0128.makanapaya.viewmodel.MakananApiStatus
import androidx.compose.foundation.layout.width

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodlistScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val statusApi by viewModel.status.collectAsState()
    val makananListFromApi by viewModel.makananListFromApi.collectAsState()
    val errorMessageApi by viewModel.errorMessage.collectAsState()

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
                ),
                actions = {
                    // Semua tombol aksi yang terkait dengan switch layout atau data lokal dihapus
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (statusApi) {
                MakananApiStatus.LOADING -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        androidx.compose.material3.CircularProgressIndicator()
                        Text(text = "Memuat data makanan dari internet...")
                    }
                }

                MakananApiStatus.ERROR -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = errorMessageApi ?: "Terjadi kesalahan saat mengambil data.")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.getMakananFromApi() }) {
                            Text("Coba Lagi")
                        }
                    }
                }

                MakananApiStatus.DONE -> {
                    if (makananListFromApi.isEmpty()) {
                        Text(
                            text = "Tidak ada data makanan dari internet.",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = Color.Gray
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 160.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(makananListFromApi) { item ->
                                GridItemOnline(makanan = item)
                            }
                        }
                    }
                }
            }

            if (makananListFromApi.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        val shareText = makananListFromApi.joinToString("\n\n") { item ->
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
} // <--- KURUNG KURAWAL PENUTUP FoodlistScreen BERAKHIR DI SINI

// --- SEMUA COMPOSABLE LAINNYA HARUS DI LUAR FoodlistScreen ---

@Composable
fun GridItemOnline(
    makanan: Makanan,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, DividerDefaults.color)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(MakananApi.getMakananImageUrl(makanan.imageUri))
                        .crossfade(true)
                        .build(),
                    contentDescription = makanan.nama,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Text(
                text = makanan.nama,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            val keywordGabungan = listOfNotNull(
                makanan.jenis, makanan.rasa, makanan.tingkatPedas, makanan.tekstur
            ).joinToString(", ")

            Text(
                text = keywordGabungan,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// FoodlistItem dan GridItem (Untuk data lokal) tetap dipertahankan di luar FoodlistScreen
// jika masih digunakan di tempat lain. Jika tidak, kamu bisa menghapusnya.
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
                    Text(stringResource(R.string.edit))
                }
                TextButton(onClick = onDeleteClick) {
                    Text(stringResource(R.string.tombol_hapus), color = Color.Red)
                }
            }
        }
    }
}


@Composable
fun GridItem(
    makanan: Makanan,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, DividerDefaults.color)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }) {
                if (!makanan.imageUri.isNullOrBlank()) {
                    AsyncImage(
                        model = Uri.parse(makanan.imageUri),
                        contentDescription = makanan.nama,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = makanan.nama,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp),
                        tint = Color.Gray
                    )
                }
            }

            Text(
                text = makanan.nama,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            val keywordGabungan = listOfNotNull(
                makanan.jenis, makanan.rasa, makanan.tingkatPedas, makanan.tekstur
            ).joinToString(", ")

            Text(
                text = keywordGabungan,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(onClick = onEditClick) {
                    Text(stringResource(R.string.edit))
                }
                TextButton(onClick = onDeleteClick) {
                    Text(stringResource(R.string.tombol_hapus), color = Color.Red)
                }
            }
        }
    }
}