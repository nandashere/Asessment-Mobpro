package com.anandamartiza0128.makanapaya.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun copyUriToInternalStorage(context: Context, uri: Uri, fileName: String): String {
    val destinationFile = File(context.filesDir, fileName)
    try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(destinationFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return destinationFile.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
        return ""
    }
}