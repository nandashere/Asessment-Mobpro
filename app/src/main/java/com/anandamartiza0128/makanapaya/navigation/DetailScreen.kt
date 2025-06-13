package com.anandamartiza0128.makanapaya.navigation

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.anandamartiza0128.makanapaya.R
import com.anandamartiza0128.makanapaya.components.DropDownField
import com.anandamartiza0128.makanapaya.model.FoodConstants
import com.anandamartiza0128.makanapaya.model.Makanan
import com.anandamartiza0128.makanapaya.network.MakananApi
import com.anandamartiza0128.makanapaya.util.ViewModelFactory
import com.anandamartiza0128.makanapaya.util.copyUriToInternalStorage // Import fungsi ini jika diperlukan
import com.anandamartiza0128.makanapaya.viewmodel.MainViewModel
import java.io.File // Import File jika digunakan untuk image upload

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: Int,
    navController: NavController,
    // Gunakan MainViewModel di sini
    viewModel: MainViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val context = LocalContext.current
    val ceriseColor = Color(ContextCompat.getColor(context, R.color.cerise))
    val yellowColor = Color(ContextCompat.getColor(context, R.color.yellow))

    // Ambil daftar makanan dari API. Kita akan mencari makanan yang sesuai dengan ID dari daftar ini.
    val makananListFromApi by viewModel.makananListFromApi.collectAsState()

    // State lokal untuk form makanan yang akan diedit/ditampilkan.
    // Inisialisasi dengan Makanan kosong atau Makanan yang ditemukan.
    var currentMakananFormState by remember { mutableStateOf(Makanan()) }

    var expandedJenis by remember { mutableStateOf(false) }
    var expandedRasa by remember { mutableStateOf(false) }
    var expandedPedas by remember { mutableStateOf(false) }
    var expandedTekstur by remember { mutableStateOf(false) }

    val listJenis = FoodConstants.getListJenis(context)
    val listRasa = FoodConstants.getListRasa(context)
    val listPedas = FoodConstants.getListPedas(context)
    val listTekstur = FoodConstants.getListTekstur(context)

    // State untuk menyimpan file gambar yang dipilih (jika ada perubahan)
    var selectedImageFileForUpload: File? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                // Saat gambar baru dipilih, simpan URI lokalnya untuk preview.
                // Jika kamu ingin mengupload gambar ini ke server, kamu perlu
                // menyimpannya sebagai File dan mengirimkannya dalam Multipart request
                // saat tombol "Simpan" diklik.
                val copiedPath = copyUriToInternalStorage(
                    context,
                    uri,
                    "img_edit_${currentMakananFormState.id}_${System.currentTimeMillis()}.jpg"
                )
                currentMakananFormState = currentMakananFormState.copy(imageUri = copiedPath)
                selectedImageFileForUpload = File(copiedPath) // Simpan file untuk diupload nanti
            }
        }
    )

    // LaunchedEffect untuk memuat data makanan saat ID berubah atau data API tersedia
    LaunchedEffect(id, makananListFromApi) {
        if (id != 0) {
            val foundMakanan = makananListFromApi.find { it.id == id }
            currentMakananFormState = foundMakanan ?: Makanan()
            // Set selectedImageFileForUpload ke null setiap kali makanan baru dimuat
            // agar tidak mengirim gambar lama secara tidak sengaja
            selectedImageFileForUpload = null
        } else {
            // Ini mungkin tidak relevan jika DetailScreen hanya untuk edit
            // Jika untuk "Tambah Makanan" juga, harus disesuaikan rutenya.
            currentMakananFormState = Makanan()
            selectedImageFileForUpload = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.foodlist)) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = ceriseColor,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.icon_back),
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
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
                if (currentMakananFormState.imageUri.isNotBlank()) {
                    // Cek apakah imageUri adalah URL lengkap (dari server) atau path lokal
                    val imageUrl = if (currentMakananFormState.imageUri.startsWith("http")) {
                        currentMakananFormState.imageUri
                    } else {
                        // Jika bukan URL lengkap, asumsikan itu adalah path relatif dari server
                        // atau path lokal yang disimpan.
                        // Jika ini adalah path lokal yang di-copy, tampilkan langsung dari File.
                        // Jika ini adalah path relatif dari server (misal: "makanan_images/file.jpg"),
                        // maka gunakan MakananApi.getMakananImageUrl().
                        // Kamu perlu memastikan logika ini sesuai dengan bagaimana imageUri disimpan
                        // setelah diambil dari API.
                        MakananApi.getMakananImageUrl(currentMakananFormState.imageUri)
                    }

                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
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

            // Form Field Nama Makanan
            FormField(
                label = stringResource(R.string.nama_makanan),
                value = currentMakananFormState.nama,
                onValueChange = {
                    currentMakananFormState = currentMakananFormState.copy(nama = it)
                }
            )

            DropDownField(
                label = stringResource(R.string.jenis_makanan),
                value = currentMakananFormState.jenis,
                expanded = expandedJenis,
                onExpandedChange = { expandedJenis = it },
                onItemSelected = {
                    currentMakananFormState = currentMakananFormState.copy(jenis = it)
                    expandedJenis = false
                },
                items = listJenis
            )

            DropDownField(
                label = stringResource(R.string.rasa_makanan),
                value = currentMakananFormState.rasa,
                expanded = expandedRasa,
                onExpandedChange = { expandedRasa = it },
                onItemSelected = {
                    currentMakananFormState = currentMakananFormState.copy(rasa = it)
                    expandedRasa = false
                },
                items = listRasa
            )

            DropDownField(
                label = stringResource(R.string.tingkat_kepedasan),
                value = currentMakananFormState.tingkatPedas,
                expanded = expandedPedas,
                onExpandedChange = { expandedPedas = it },
                onItemSelected = {
                    currentMakananFormState = currentMakananFormState.copy(tingkatPedas = it)
                    expandedPedas = false
                },
                items = listPedas
            )

            DropDownField(
                label = stringResource(R.string.tekstur_makanan),
                value = currentMakananFormState.tekstur,
                expanded = expandedTekstur,
                onExpandedChange = { expandedTekstur = it },
                onItemSelected = {
                    currentMakananFormState = currentMakananFormState.copy(tekstur = it)
                    expandedTekstur = false
                },
                items = listTekstur
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Panggil fungsi updateMakananInApi dari MainViewModel
                    // Jika ada gambar baru yang dipilih, kamu juga perlu mengirimnya.
                    // Saat ini, updateMakananInApi tidak menangani file gambar, hanya data Makanan.
                    // Kamu perlu menambahkan parameter File? imageFile di updateMakananInApi
                    // jika kamu ingin mengizinkan perubahan gambar saat update.
                    viewModel.updateMakananInApi(currentMakananFormState)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = yellowColor),
                shape = RoundedCornerShape(50)
            ) {
                Text(stringResource(R.string.simpan_makanan), color = Color.White)
            }
        }
    }
}