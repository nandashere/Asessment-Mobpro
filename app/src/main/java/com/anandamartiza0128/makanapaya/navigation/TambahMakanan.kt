package com.anandamartiza0128.makanapaya.navigation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.anandamartiza0128.makanapaya.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahMakananScreen(navController: NavController) {
    val context = LocalContext.current
    val ceriseColor = Color(ContextCompat.getColor(context, R.color.cerise))
    val yellowColor = Color(ContextCompat.getColor(context, R.color.yellow))

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    var namaMakanan by remember { mutableStateOf("") }
    var jenisMakanan by remember { mutableStateOf("") }
    var expandedJenis by remember { mutableStateOf(false) }

    var rasaMakanan by remember { mutableStateOf("") }
    var expandedRasa by remember { mutableStateOf(false) }

    var tingkatPedas by remember { mutableStateOf("") }
    var expandedPedas by remember { mutableStateOf(false) }

    var teksturMakanan by remember { mutableStateOf("") }
    var expandedTekstur by remember { mutableStateOf(false) }

    val listJenis = listOf("Makanan Berat", "Makanan Ringan")
    val listRasa = listOf("Gurih", "Manis")
    val listPedas = listOf("Pedas", "Tidak Pedas")
    val listTekstur = listOf("Berkuah", "Kering")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Makanan Saya") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = ceriseColor,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.masukkan_gambar),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            TextButton(onClick = {
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) {
                Text(stringResource(R.string.tambahkan_gambar), color = ceriseColor)
            }

            Spacer(modifier = Modifier.height(16.dp))

            FormField(label = stringResource(R.string.nama_makanan), value = namaMakanan, onValueChange = { namaMakanan = it })

            DropDownField(
                label = stringResource(R.string.jenis_makanan), value = jenisMakanan, expanded = expandedJenis,
                onExpandedChange = { expandedJenis = it },
                onItemSelected = { jenisMakanan = it; expandedJenis = false },
                items = listJenis
            )

            DropDownField(
                label = stringResource(R.string.rasa_makanan), value = rasaMakanan, expanded = expandedRasa,
                onExpandedChange = { expandedRasa = it },
                onItemSelected = { rasaMakanan = it; expandedRasa = false },
                items = listRasa
            )

            DropDownField(
                label = stringResource(R.string.tingkat_kepedasan), value = tingkatPedas, expanded = expandedPedas,
                onExpandedChange = { expandedPedas = it },
                onItemSelected = { tingkatPedas = it; expandedPedas = false },
                items = listPedas
            )

            DropDownField(
                label = stringResource(R.string.tekstur_makanan), value = teksturMakanan, expanded = expandedTekstur,
                onExpandedChange = { expandedTekstur = it },
                onItemSelected = { teksturMakanan = it; expandedTekstur = false },
                items = listTekstur
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* TODO: Tambahkan aksi simpan */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = yellowColor),
                shape = RoundedCornerShape(50)
            ) {
                Text(stringResource(R.string.tambah_makanan), color = Color.White)
            }
        }
    }
}

@Composable
fun FormField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownField(
    label: String,
    value: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onItemSelected: (String) -> Unit,
    items: List<String>
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(50)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = { onItemSelected(item) }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TambahMakananScreenPreview() {
    val navController = rememberNavController()

    TambahMakananScreen(navController = navController)
}
