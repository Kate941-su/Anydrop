package com.kaitokitaya.anydrop.network

import com.kaitokitaya.anydrop.constant.ConstantVariable
import java.io.OutputStreamWriter
import java.net.Socket

class ClientSocketManager {
    private var socket: Socket? = null

    fun closeSocket() {
        socket?.close()
    }

    fun sendDataToServer(serverIp: String, data: String) {
        socket = Socket(serverIp, ConstantVariable.DATA_PORT) // Connect to server on port 8080
        socket?.getOutputStream().use { outputStream ->
            OutputStreamWriter(outputStream).use { writer ->
                writer.write(data)
                writer.flush()
            }
        }
        closeSocket()
    }
}