package com.kaitokitaya.anydrop.view

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
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
import com.kaitokitaya.anydrop.ui.theme.AnydropTheme
import com.kaitokitaya.anydrop.viewmodel.ModeSelectScreenViewModel
import java.io.File

@Composable
fun ModeSelectScreen() {
    val viewModel = hiltViewModel<ModeSelectScreenViewModel>()
    val ipAddressState = viewModel.ipAddressState.collectAsState()
    val fileState = viewModel.targetFileState.collectAsState()
    val isServerOn = viewModel.isServerOn.collectAsState()
    val dataState = viewModel.dataState.collectAsState()

    val context = LocalContext.current

    var tempFilePath by remember { mutableStateOf<String?>("No file selected") }


    val filePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                // Handle the selected file URI
                val fileName = getFileNameFromUri(uri = uri, context = context)
                fileName?.let {
                    val file = getFileFromUri(context = context, uri = uri,fileName = fileName )
                    viewModel.onPickedFile(file = file)
                }
            } ?: run {
                tempFilePath = "No file selected"
            }
        }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(stringResource(R.string.select_mode))
            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    filePickerLauncher.launch(arrayOf("*/*"))
                }) {
                    Text("Open file picker")
                }

            }
            Text("Your ip Address: ${ipAddressState.value} / File path: ${fileState.value?.absolutePath}")
            HorizontalDivider()
            Button(onClick = {
                viewModel.onSendData(serverIp = "192.168.0.7")
            }) {
                Text("Send (client)")
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
            Text("Receive Data: ${dataState.value}")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    ModeSelectScreen()
}