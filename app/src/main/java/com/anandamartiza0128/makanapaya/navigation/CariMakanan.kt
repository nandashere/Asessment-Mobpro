package com.anandamartiza0128.makanapaya.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.anandamartiza0128.makanapaya.R
import com.anandamartiza0128.makanapaya.model.Makanan
import com.anandamartiza0128.makanapaya.viewmodel.FoodViewModel
import androidx.compose.ui.Alignment
import com.anandamartiza0128.makanapaya.model.FoodConstants


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CariMakananScreen(
    viewModel: FoodViewModel
) {
    val context = LocalContext.current
    val ceriseColor = Color(ContextCompat.getColor(context, R.color.cerise))

    val listJenis = FoodConstants.getListJenis(context)
    val listRasa = FoodConstants.getListRasa(context)
    val listPedas = FoodConstants.getListPedas(context)
    val listTekstur = FoodConstants.getListTekstur(context)

    var jenis by remember { mutableStateOf("") }
    var rasa by remember { mutableStateOf("") }
    var tingkatPedas by remember { mutableStateOf("") }
    var tekstur by remember { mutableStateOf("") }
    var hasil by remember { mutableStateOf<List<Makanan>>(emptyList()) }
    var sudahCari by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.cari_makanan))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = ceriseColor,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(id = R.string.sesuaikan_seleramu), color = Color.Red, fontWeight = FontWeight.Bold)

            // === Jenis Makanan ===
            Text(stringResource(id = R.string.jenis_makanan), fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listJenis.forEach { item ->
                    OutlinedButton(
                        onClick = { jenis = if (jenis == item) "" else item },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (jenis == item) Color(0xFFFFCDD2) else Color.Transparent
                        )
                    ) {
                        Text(item)
                    }
                }
            }

            // === Rasa ===
            Text(stringResource(id = R.string.rasa), fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listRasa.forEach { item ->
                    OutlinedButton(
                        onClick = { rasa = if (rasa == item) "" else item },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (rasa == item) Color(0xFFFFF9C4) else Color.Transparent
                        )
                    ) {
                        Text(item)
                    }
                }
            }

            // === Tingkat Pedas ===
            Text(stringResource(id = R.string.tingkat_pedas), fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listPedas.forEach { item ->
                    OutlinedButton(
                        onClick = { tingkatPedas = if (tingkatPedas == item) "" else item },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (tingkatPedas == item) Color(0xFFD1C4E9) else Color.Transparent
                        )
                    ) {
                        Text(item)
                    }
                }
            }

            // === Tekstur ===
            Text(stringResource(id = R.string.tekstur_makanan), fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listTekstur.forEach { item ->
                    OutlinedButton(
                        onClick = { tekstur = if (tekstur == item) "" else item },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (tekstur == item) Color(0xFFB2DFDB) else Color.Transparent
                        )
                    ) {
                        Text(item)
                    }
                }
            }

            // === Hasil Pencarian ===
            Spacer(modifier = Modifier.height(16.dp))

            if (sudahCari) {
                if (hasil.isNotEmpty()) {
                    Text(stringResource(id = R.string.rekomendasi), fontWeight = FontWeight.Bold)
                    hasil.forEach { makanan ->
                        FoodlistItem(
                            imageUri = makanan.imageUri,
                            name = makanan.nama,
                            keywords = "${makanan.jenis}, ${makanan.rasa}, ${makanan.tingkatPedas}, ${makanan.tekstur}",
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                } else {
                    Text(stringResource(id = R.string.tidak_ada_makanan), color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    hasil = viewModel.cariRekomendasi(jenis, rasa, tingkatPedas, tekstur)
                    sudahCari = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD54F)),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(id = R.string.btn_cari_makanan))
            }
        }
    }
}



