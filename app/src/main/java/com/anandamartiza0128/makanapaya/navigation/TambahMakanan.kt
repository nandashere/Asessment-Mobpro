package com.anandamartiza0128.makanapaya.navigation

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.anandamartiza0128.makanapaya.R
import com.anandamartiza0128.makanapaya.components.DropDownField
import com.anandamartiza0128.makanapaya.model.Makanan
import com.anandamartiza0128.makanapaya.viewmodel.FoodViewModel
import com.anandamartiza0128.makanapaya.model.FoodConstants
import com.anandamartiza0128.makanapaya.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahMakananScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val ceriseColor = Color(ContextCompat.getColor(context, R.color.cerise))
    val yellowColor = Color(ContextCompat.getColor(context, R.color.yellow))

    var formState by remember {
        mutableStateOf(
            Makanan(
                nama = "",
                jenis = "",
                rasa = "",
                tingkatPedas = "",
                tekstur = "",
                imageUri = ""
            )
        )
    }

    var expandedJenis by remember { mutableStateOf(false) }
    var expandedRasa by remember { mutableStateOf(false) }
    var expandedPedas by remember { mutableStateOf(false) }
    var expandedTekstur by remember { mutableStateOf(false) }

    val listJenis = FoodConstants.getListJenis(context)
    val listRasa = FoodConstants.getListRasa(context)
    val listPedas = FoodConstants.getListPedas(context)
    val listTekstur = FoodConstants.getListTekstur(context)

    var showError by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            formState = formState.copy(imageUri = uri?.toString() ?: "")
        }
    )

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
                if (formState.imageUri.isNotBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(formState.imageUri),
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

            // Form Field Nama Makanan dengan Validasi
            FormField(
                label = stringResource(R.string.nama_makanan),
                value = formState.nama,
                onValueChange = {
                    formState = formState.copy(nama = it)
                    if (showError && it.isNotBlank()) showError = false
                },
                isError = showError && formState.nama.isBlank(),
                errorMessage = stringResource(R.string.kolom_kosong)
            )

            DropDownField(
                label = stringResource(R.string.jenis_makanan),
                value = formState.jenis,
                expanded = expandedJenis,
                onExpandedChange = { expandedJenis = it },
                onItemSelected = {
                    formState = formState.copy(jenis = it)
                    expandedJenis = false
                },
                items = listJenis
            )

            DropDownField(
                label = stringResource(R.string.rasa_makanan),
                value = formState.rasa,
                expanded = expandedRasa,
                onExpandedChange = { expandedRasa = it },
                onItemSelected = {
                    formState = formState.copy(rasa = it)
                    expandedRasa = false
                },
                items = listRasa
            )

            DropDownField(
                label = stringResource(R.string.tingkat_kepedasan),
                value = formState.tingkatPedas,
                expanded = expandedPedas,
                onExpandedChange = { expandedPedas = it },
                onItemSelected = {
                    formState = formState.copy(tingkatPedas = it)
                    expandedPedas = false
                },
                items = listPedas
            )

            DropDownField(
                label = stringResource(R.string.tekstur_makanan),
                value = formState.tekstur,
                expanded = expandedTekstur,
                onExpandedChange = { expandedTekstur = it },
                onItemSelected = {
                    formState = formState.copy(tekstur = it)
                    expandedTekstur = false
                },
                items = listTekstur
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (formState.nama.isBlank()) {
                        showError = true
                    } else {
                        viewModel.addMakanan(formState)
                        navController.popBackStack()
                    }
                },
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
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )
        if (isError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun TambahMakananScreenPreview() {
//    val navController = rememberNavController()
//
//    TambahMakananScreen(navController = navController)
//}
