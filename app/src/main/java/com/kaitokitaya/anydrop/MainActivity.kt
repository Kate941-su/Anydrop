package com.kaitokitaya.anydrop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kaitokitaya.anydrop.ui.theme.AnydropTheme
import com.kaitokitaya.anydrop.view.ModeSelectScreen
import com.kaitokitaya.anydrop.view.SelectScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnydropTheme {
                SelectScreen()
            }
        }
    }
}   

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AnydropTheme {

    }
}