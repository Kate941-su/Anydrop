package com.kaitokitaya.anydrop.file

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import com.kaitokitaya.anydrop.appError.FailedSaveError
import java.io.File
import java.io.FileInputStream

object FileManager {
    private const val TAG = "FileManager"
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

    fun saveFileToDownloads(
        context: Context,
        srcPath: String,
        fileName: String,
        mimeType: String = "application/octet-stream"
    ) {
        try {
            val resolver = context.contentResolver
            // Use MediaStore to insert a file into the Downloads collection
            val downloadsCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Files.getContentUri("external")
            }

            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, mimeType)
                // Use "Download" as relative path to put it in the Downloads folder
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Downloads.RELATIVE_PATH, "Download/")
                }
            }
            val srcFile = File(srcPath)
            val uri = resolver.insert(downloadsCollection, contentValues) ?: throw Error()
            resolver.openOutputStream(uri)?.use { outputStream ->
                FileInputStream(srcFile).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Throwable) {
            MPLog.tag(TAG).e(e.message.toString())
            throw FailedSaveError()
        }
    }
}