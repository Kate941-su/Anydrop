package com.kaitokitaya.anydrop.network.socket

enum class SocketState {
    OPEN,
    CLOSE
}

sealed class SocketOption {
    data object CPlane: SocketOption()
    data object DPlane: SocketOption()
}