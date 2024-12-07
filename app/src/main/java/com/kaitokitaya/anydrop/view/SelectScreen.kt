package com.kaitokitaya.anydrop.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaitokitaya.anydrop.view.component.HeaderTextButton
import com.kaitokitaya.anydrop.viewmodel.SelectScreenViewModel
import com.kaitokitaya.anydrop.viewmodel.UserType
import MPLog


private const val TAG = "SelectScreen"
@Composable
fun SelectScreen() {
    val viewModel = hiltViewModel<SelectScreenViewModel>()
    val userTypeState = viewModel.userTypeState.collectAsState()
    MPLog.tag(TAG).d("${UserType.SERVER == userTypeState.value}")
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                HeaderTextButton(
                    onClick = {
                        viewModel.onChangeUserType(UserType.CLIENT)

                    },
                    modifier = Modifier.weight(1F),
                    title = "Client",
                    isSelected = UserType.CLIENT == userTypeState.value
                )
                HeaderTextButton(
                    onClick = {
                        viewModel.onChangeUserType(UserType.SERVER)
                    },
                    modifier = Modifier.weight(1F),
                    title = "Server",
                    isSelected = UserType.SERVER == userTypeState.value
                )
            }
            when (userTypeState.value) {
                UserType.CLIENT -> {
                    ClientScreen()
                }

                UserType.SERVER -> {
                    ServerScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectScreenPreview() {
    SelectScreen()
}