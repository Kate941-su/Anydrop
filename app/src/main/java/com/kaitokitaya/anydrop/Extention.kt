package com.kaitokitaya.anydrop

import java.net.Socket

val Socket.isOpened: Boolean
    get() {
        return !this.isClosed
    }