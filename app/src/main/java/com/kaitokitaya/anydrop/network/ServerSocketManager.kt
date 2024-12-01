package com.kaitokitaya.anydrop.network

import MPLog
import android.content.Context
import com.kaitokitaya.anydrop.constant.ConstantVariable
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket
import java.time.Clock
import java.time.ZoneId

private const val TAG = "SocketServer"

class ServerSocketManager {
    private val serverSocket = ServerSocket(ConstantVariable.DATA_PORT)
    private var socket: Socket? = null
    private var isSocketOpened = false

    fun open() {
        isSocketOpened = true
    }

    fun close() {
        isSocketOpened = false
        socket?.close()
    }

    // TODO: Actually onRetrieve will retrieve File type.
    fun retrievingFile(context: Context, onRetrieved: (String) -> Unit) {
        while (isSocketOpened) {
            socket = serverSocket.accept()
            MPLog.tag(TAG).d("Client connected: ${socket!!.inetAddress}")
            val file =
                File(context.cacheDir, "temp_file_${Clock.system(ZoneId.systemDefault()).millis()}")
            FileOutputStream(file).use { fileOutputStream ->
                // Write the incoming data to the file
                saveStreamToFile(socket!!.getInputStream(), fileOutputStream)
            }

            // TODO: Just testing
            file.useLines { lines ->
                lines.firstOrNull()?.let {
                    onRetrieved(it)
                }
            }

        }
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