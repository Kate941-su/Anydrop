package com.kaitokitaya.anydrop.network.socket

import com.kaitokitaya.anydrop.constant.ConstantVariable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.net.ServerSocket
import java.net.Socket

abstract class SocketManager {
    // C-plane socket
    var cSocket: Socket? = null

    // D-plane socket
    var dSocket: Socket? = null

    // C-plane state
    private val _cSocketState = MutableStateFlow<SocketState>(SocketState.CLOSE)
    val cSocketState: StateFlow<SocketState> = _cSocketState.asStateFlow()

    // D-plane state
    private val _dSocketState = MutableStateFlow<SocketState>(SocketState.CLOSE)
    val dSocketState: StateFlow<SocketState> = _dSocketState.asStateFlow()

    fun open(option: SocketOption) {
        when(option) {
            SocketOption.CPlane -> {
                _cSocketState.update {
                    SocketState.OPEN
                }
            }
            SocketOption.DPlane -> {
                _dSocketState.update {
                    SocketState.OPEN
                }
            }
        }
    }

    open fun close(option: SocketOption) {
        when(option) {
            SocketOption.CPlane -> {
                _cSocketState.update {
                    SocketState.CLOSE
                }
                cSocket?.close()
                cSocket = null
            }
            SocketOption.DPlane -> {
                _dSocketState.update {
                    SocketState.CLOSE
                }
                dSocket?.close()
                dSocket = null
            }
        }

    }
}