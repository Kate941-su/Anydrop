package com.kaitokitaya.anydrop.viewmodel

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
import javax.inject.Inject

private const val TAG = "ClientScreenViewModel"

@HiltViewModel
class ClientScreenViewModel @Inject constructor(
    private val networkService: NetworkService,
    private val clientSocketManager: ClientSocketManager,
) : ViewModel() {

    private val _ipAddressState = MutableStateFlow<String?>(null)
    val ipAddressState: StateFlow<String?> = _ipAddressState.asStateFlow()

    private val _targetFilePathState = MutableStateFlow<String?>(null)
    val targetFilePathState: StateFlow<String?> = _targetFilePathState.asStateFlow()

    init {
        val ipAddress = networkService.getIPAddress()
        _ipAddressState.update {
            ipAddress
        }
    }

    fun onSendData(serverIp: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                targetFilePathState.value?.let { filePath ->
                    clientSocketManager.sendDataToServer(
                        serverIp = serverIp,
                        filePath = filePath,
                    )
                }
            }
        }
    }

    fun onPickedFile(file: File) {
        _targetFilePathState.update {
            file.absolutePath
        }
    }

}