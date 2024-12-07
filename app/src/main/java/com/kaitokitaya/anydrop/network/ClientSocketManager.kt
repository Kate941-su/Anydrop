package com.kaitokitaya.anydrop.network

import com.kaitokitaya.anydrop.constant.ConstantVariable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.OutputStreamWriter
import java.net.Socket

class ClientSocketManager {
    private var socket: Socket? = null

    private val _clientState = MutableStateFlow<SocketState>(SocketState.CLOSE)
    val clientState: StateFlow<SocketState> = _clientState.asStateFlow()

    fun closeSocket() {
        socket?.close()
        _clientState.update {
            SocketState.CLOSE
        }
    }

    fun sendDataToServer(serverIp: String, filePath: String) {
        _clientState.update {
            SocketState.OPEN
        }
        val file = File(filePath)
        socket = Socket(serverIp, ConstantVariable.DATA_PORT) // Connect to server on port 8080
        file.inputStream().use { fileInputStream ->
            socket?.getOutputStream().use { outputStream ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream?.write(buffer, 0, bytesRead)
                }
                outputStream?.flush()
            }
        }
        closeSocket()
    }
}