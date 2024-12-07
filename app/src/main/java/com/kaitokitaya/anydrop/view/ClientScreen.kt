package com.kaitokitaya.anydrop.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaitokitaya.anydrop.R
import com.kaitokitaya.anydrop.file.FileManger.getFileFromUri
import com.kaitokitaya.anydrop.file.FileManger.getFileNameFromUri
import com.kaitokitaya.anydrop.viewmodel.ClientScreenViewModel

@Composable
fun ClientScreen() {
    var tempFilePath by remember { mutableStateOf<String?>("No file selected") }
    val viewModel = hiltViewModel<ClientScreenViewModel>()
    val context = LocalContext.current

    val ipAddressState = viewModel.ipAddressState.collectAsState()
    val fileState = viewModel.targetFilePathState.collectAsState()

    val filePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                // Handle the selected file URI
                val fileName = getFileNameFromUri(uri = uri, context = context)
                fileName?.let {
                    val file = getFileFromUri(context = context, uri = uri, fileName = fileName)
                    viewModel.onPickedFile(file = file)
                }
            } ?: run {
                tempFilePath = "No file selected"
            }
        }
    Column {
        Text("Your ip Address: ${ipAddressState.value}")
        Text(stringResource(R.string.select_mode))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                filePickerLauncher.launch(arrayOf("*/*"))
            }) {
                Text("Open file picker")
            }

        }
        Text("File path: ${fileState.value}")
        Button(onClick = {
            viewModel.onSendData(serverIp = "192.168.0.221")
        }) {
            Text("Send to 192.168.0.221 (client)")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientScreenPreview() {
    ClientScreen()
}