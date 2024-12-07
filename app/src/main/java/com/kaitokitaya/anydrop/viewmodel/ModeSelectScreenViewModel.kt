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

