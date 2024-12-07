package com.kaitokitaya.anydrop.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kaitokitaya.anydrop.global.VoidCallback

@Composable
fun HeaderTextButton(
    onClick: VoidCallback,
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
) {
    Box(modifier = modifier
        .background(
            color = if(isSelected) Color.Red else Color.White,
        )
        .clickable {
            onClick()
        }) {
        Text(
            title,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}