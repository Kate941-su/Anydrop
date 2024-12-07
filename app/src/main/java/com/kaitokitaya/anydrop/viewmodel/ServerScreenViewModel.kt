package com.kaitokitaya.anydrop.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaitokitaya.anydrop.appError.FailedSaveError
import com.kaitokitaya.anydrop.file.FileManager
import com.kaitokitaya.anydrop.global.VoidCallback
import com.kaitokitaya.anydrop.network.NetworkService
import com.kaitokitaya.anydrop.network.ServerSocketManager
import com.kaitokitaya.anydrop.network.SocketState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import javax.inject.Inject

private const val TAG = "ServerScreenViewModel"

@HiltViewModel
class ServerScreenViewModel @Inject constructor(
    private val networkService: NetworkService,
    private val serverSocketManager: ServerSocketManager,
) : ViewModel() {

    private val _ipAddressState = MutableStateFlow<String?>(null)
    val ipAddressState: StateFlow<String?> = _ipAddressState.asStateFlow()

    private val _filePath = MutableStateFlow("")
    val filePath: StateFlow<String> = _filePath.asStateFlow()

    private val _serverState = MutableStateFlow<SocketState>(SocketState.CLOSE)
    val serverState: StateFlow<SocketState> = _serverState.asStateFlow()


    init {
        val ipAddress = networkService.getIPAddress()
        _ipAddressState.update {
            ipAddress
        }

        viewModelScope.launch {
            serverSocketManager.socketState.collect { state ->
                _serverState.update {
                    state
                }
            }
        }
    }

    fun onServerOpen(context: Context, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            var toastMessage = ""
            withContext(Dispatchers.IO) {
                serverSocketManager.open()
                var file: File? = null
                serverSocketManager.retrievingFile(context = context).onSuccess { filePath ->
                    file = File(filePath)
                    file.apply {
                        MPLog.tag(TAG).d(absolutePath.toString())
                        _filePath.update {
                            "$absolutePath / size: ${file.length()}"
                        }
                    }
                    FileManager.saveFileToDownloads(
                        context = context,
                        srcPath = file.absolutePath,
                        fileName = LocalDateTime.now().toEpochSecond(UTC).toString()
                    )
                    toastMessage = "Finish downloading👍"
                }.onFailure { e ->
                    val castedError = e as FailedSaveError
                    toastMessage = castedError.description
                }
            }
            onComplete(toastMessage)
        }
    }

    fun onServerClose() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                serverSocketManager.close()
            }
        }
    }

    fun onTapReset() {
        _filePath.update {
            ""
        }
    }
}