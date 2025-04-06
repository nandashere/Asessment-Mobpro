package com.anandamartiza0128.makanapaya.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.anandamartiza0128.makanapaya.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahMakananScreen() {
    val context = LocalContext.current                                           // akses resources dari Android framework(color.xml, string.xml,dll.)
    val ceriseColor = Color(ContextCompat.getColor(context, R.color.cerise))    // pakai warna dari file colors.xml
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.tambah_makanan))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = ceriseColor,
                    titleContentColor = Color.White,
                )
            )
        }
    ) { innerPadding ->
        Text(
            text = stringResource(R.string.my_foodlist),
            modifier = Modifier.padding(innerPadding).padding(16.dp)
        )
    }
}