package com.kaitokitaya.anydrop.network.socket

import com.kaitokitaya.anydrop.constant.ConstantVariable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.net.ServerSocket
import java.net.Socket
class ClientSocketManager: SocketManager() {

    fun sendDataToServer(serverIp: String, filePath: String) {
        open(option = SocketOption.DPlane)
        val file = File(filePath)
        dSocket = Socket(serverIp, ConstantVariable.DATA_PORT) // Connect to server on port 8080
        file.inputStream().use { fileInputStream ->
            dSocket?.getOutputStream().use { outputStream ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream?.write(buffer, 0, bytesRead)
                }
                outputStream?.flush()
            }
        }
        close(option = SocketOption.CPlane)
    }
}