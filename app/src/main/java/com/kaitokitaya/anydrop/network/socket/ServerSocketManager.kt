package com.kaitokitaya.anydrop.network.socket

import MPLog
import android.content.Context
import com.kaitokitaya.anydrop.appError.FailedSaveError
import com.kaitokitaya.anydrop.constant.ConstantVariable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket

private const val TAG = "SocketServer"

class ServerSocketManager: SocketManager() {

    // C-plane socket
    private val cServerSocket = ServerSocket(ConstantVariable.COMMUNICATION_PORT)

    // D-plane socket
    private val dServerSocket = ServerSocket(ConstantVariable.DATA_PORT)


    // TODO: Actually onRetrieve will retrieve File type.
    fun retrievingFile(context: Context): Result<String> {
        try {
            open(option = SocketOption.DPlane)
            while (dSocketState.value == SocketState.OPEN) {
                dSocket = dServerSocket.accept()
                MPLog.tag(TAG).d("Client connected: ${dSocket!!.inetAddress}")
                val file = File(context.cacheDir, "temp_file")
                FileOutputStream(file).use { fileOutputStream ->
                    // Write the incoming data to the file
                    saveStreamToFile(dSocket!!.getInputStream(), fileOutputStream)
                }
                MPLog.tag(TAG).d("File Size: ${file.length()}")
                return Result.success(file.absolutePath)
            }
        } catch (e: Throwable) {
            MPLog.tag(TAG).d("File Size: $e")
            return Result.failure(FailedSaveError())
        } finally {
            close(option = SocketOption.DPlane)
        }
        return Result.failure(FailedSaveError())
    }

    // Helper function to copy the input stream to the output stream
    private fun saveStreamToFile(inputStream: InputStream, fileOutputStream: FileOutputStream) {
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            fileOutputStream.write(buffer, 0, bytesRead)
        }
        fileOutputStream.flush()
    }
}
