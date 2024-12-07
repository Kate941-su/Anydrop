package com.kaitokitaya.anydrop.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaitokitaya.anydrop.viewmodel.ServerScreenViewModel

@Composable
fun ServerScreen() {
    val viewModel = hiltViewModel<ServerScreenViewModel>()
    val ipAddressState = viewModel.ipAddressState.collectAsState()
    val filePath = viewModel.filePath.collectAsState()
    val isServerOn = viewModel.isServerOn.collectAsState()
    val context = LocalContext.current

    Column {
        Text("Your ip Address: ${ipAddressState.value}")
        Text("FilePath: ${filePath.value}")
        Button(onClick = {
            viewModel.onTapReset()
        }) {
            Text("Reset Data")
        }
        Button(onClick = {
            if (isServerOn.value) {
                viewModel.onServerClose()
            } else {
                viewModel.onServerOpen(context = context)
            }
        }) {
            if (isServerOn.value) {
                Text("OFF (Server)")
            } else {
                Text("ON (Server)")
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ServerScreenPreview() {
    ServerScreen()
}