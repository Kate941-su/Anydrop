package com.kaitokitaya.anydrop.viewmodel

import androidx.lifecycle.ViewModel
import com.kaitokitaya.anydrop.network.NetworkService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SelectScreenViewModel @Inject constructor() : ViewModel() {
    private val _userTypeState = MutableStateFlow<UserType>(UserType.CLIENT)
    val userTypeState: StateFlow<UserType> = _userTypeState.asStateFlow()

    fun onChangeUserType(userType: UserType) {
        _userTypeState.update {
            userType
        }
    }
}

enum class UserType {
    CLIENT,
    SERVER
}