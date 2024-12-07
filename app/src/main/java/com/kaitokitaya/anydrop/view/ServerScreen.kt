package com.kaitokitaya.anydrop.view

import android.widget.Button
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaitokitaya.anydrop.network.SocketState
import com.kaitokitaya.anydrop.viewmodel.ServerScreenViewModel

private const val TAG = "ServerScreen"

@Composable
fun ServerScreen() {
    val viewModel = hiltViewModel<ServerScreenViewModel>()
    val ipAddressState = viewModel.ipAddressState.collectAsState()
    val filePath = viewModel.filePath.collectAsState()
    val serverState = viewModel.serverState.collectAsState()
    val context = LocalContext.current
    val openDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            // Handle the URI of the selected file
            if (uri != null) {
                // You can use `context.contentResolver` to read the file
                MPLog.tag(TAG).d("Selected file URI: $uri")
            }
        }
    )
    Column {
        Text("Your ip Address: ${ipAddressState.value}")
        Text("FilePath: ${filePath.value}")
        Button(onClick = {
            viewModel.onTapReset()
        }) {
            Text("Reset Data")
        }
        Button(onClick = {
            viewModel.onServerOpen(context = context) {
                Toast.makeText(context, "Download finished", Toast.LENGTH_LONG).show()
            }
        }) {
            Text("Start listening")
        }
        when (serverState.value) {
            SocketState.OPEN -> {
                Text("ON (Server)")
            }
            SocketState.CLOSE -> {
                Text("OFF (Server)")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServerScreenPreview() {
    ServerScreen()
}