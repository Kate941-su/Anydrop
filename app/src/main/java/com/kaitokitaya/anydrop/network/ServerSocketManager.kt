package com.kaitokitaya.anydrop.network

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

class ServerSocketManager {
    private val serverSocket = ServerSocket(ConstantVariable.DATA_PORT)
    private var socket: Socket? = null

    private val _socketState = MutableStateFlow<SocketState>(SocketState.CLOSE)
    val socketState: StateFlow<SocketState> = _socketState.asStateFlow()

    fun open() {
        _socketState.update {
            SocketState.OPEN
        }
    }

    fun close() {
        _socketState.update {
            SocketState.CLOSE
        }
        socket?.close()
    }

    // TODO: Actually onRetrieve will retrieve File type.
    fun retrievingFile(context: Context): Result<String> {
        try {
            while (socketState.value == SocketState.OPEN) {
                socket = serverSocket.accept()
                MPLog.tag(TAG).d("Client connected: ${socket!!.inetAddress}")
                val file = File(context.cacheDir, "temp_file")
                FileOutputStream(file).use { fileOutputStream ->
                    // Write the incoming data to the file
                    saveStreamToFile(socket!!.getInputStream(), fileOutputStream)
                }
                MPLog.tag(TAG).d("File Size: ${file.length()}")
                return Result.success(file.absolutePath)
            }
        } catch (e: Throwable) {
            MPLog.tag(TAG).d("File Size: $e")
            return Result.failure(FailedSaveError())
        } finally {
            close()
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

enum class SocketState {
    OPEN,
    CLOSE
}