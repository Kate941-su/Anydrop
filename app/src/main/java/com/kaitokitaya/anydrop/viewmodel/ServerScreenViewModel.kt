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

private const val TAG = "ServerScreenViewModel"

@HiltViewModel
class ServerScreenViewModel @Inject constructor(
    private val networkService: NetworkService,
    private val serverSocketManager: ServerSocketManager,
) : ViewModel() {

    private val _ipAddressState = MutableStateFlow<String?>(null)
    val ipAddressState: StateFlow<String?> = _ipAddressState.asStateFlow()

    private val _isServerOn = MutableStateFlow<Boolean>(false)
    val isServerOn: StateFlow<Boolean> = _isServerOn.asStateFlow()

    private val _filePath = MutableStateFlow("")
    val filePath: StateFlow<String> = _filePath.asStateFlow()


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
                var file: File? = null
                serverSocketManager.retrievingFile(context = context) { data ->
                    file = File(data)
                    file.apply {
                        MPLog.tag(TAG).d(absolutePath.toString())
                        _filePath.update {
                            absolutePath.toString()
                        }

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

    fun onTapReset() {
        _filePath.update {
            ""
        }
    }
}