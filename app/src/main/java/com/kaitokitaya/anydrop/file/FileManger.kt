package com.kaitokitaya.anydrop.file

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

object FileManger {
    fun getFileNameFromUri(uri: Uri, context: Context): String? {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) it.getString(nameIndex) else null
            } else null
        }
    }

    private fun copyUriToFile(context: Context, uri: Uri, destinationFile: File): File {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            destinationFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return destinationFile
    }

    fun getFileFromUri(context: Context, uri: Uri, fileName: String): File {
        val tempFile = File(context.cacheDir, fileName)
        return copyUriToFile(context, uri, tempFile)
    }
}