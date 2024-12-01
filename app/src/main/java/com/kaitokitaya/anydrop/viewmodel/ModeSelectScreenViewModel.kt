package com.kaitokitaya.anydrop.viewmodel

import MPLog
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaitokitaya.anydrop.network.ClientSocketManager
import com.kaitokitaya.anydrop.network.NetworkService
import com.kaitokitaya.anydrop.network.ServerSocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.ServerSocket
import javax.inject.Inject

private const val TAG = "ModelSelectScreenViewModel"

@HiltViewModel
class ModeSelectScreenViewModel @Inject constructor(
    private val networkService: NetworkService
) : ViewModel() {

    private val _ipAddressState = MutableStateFlow<String?>(null)
    val ipAddressState: StateFlow<String?> = _ipAddressState.asStateFlow()

    private val _targetFileState = MutableStateFlow<File?>(null)
    val targetFileState: StateFlow<File?> = _targetFileState.asStateFlow()

    private val _isServerOn = MutableStateFlow<Boolean>(false)
    val isServerOn: StateFlow<Boolean> = _isServerOn.asStateFlow()

    private val _dataState = MutableStateFlow("")
    val dataState: StateFlow<String> = _dataState.asStateFlow()

    private val serverSocketManager = ServerSocketManager()
    private val clientSocketManager = ClientSocketManager()

    init {
        val ipAddress = networkService.getIPAddress()
        _ipAddressState.update {
            ipAddress
        }
    }

    fun onServerOpen(context: Context) {
        viewModelScope.launch {
            _isServerOn.update {
                true
            }
            withContext(Dispatchers.IO) {
                serverSocketManager.open()
                serverSocketManager.retrievingFile(context = context) { data ->
                    _dataState.update {
                        data
                    }
                }
            }
        }
    }

    fun onServerClose() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _isServerOn.update {
                    false
                }
                serverSocketManager.close()
            }
        }
    }

    fun onSendData(serverIp: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                clientSocketManager.sendDataToServer(
                    serverIp = serverIp,
                    data = "Hello World",
                )
            }
        }
    }

    fun onPickedFile(file: File) {
        _targetFileState.update {
            file
        }
    }

    fun helloWorld() {
        val ipAddress = networkService.getIPAddress()
        MPLog(TAG).d("Ip Address: $ipAddress")
    }

}