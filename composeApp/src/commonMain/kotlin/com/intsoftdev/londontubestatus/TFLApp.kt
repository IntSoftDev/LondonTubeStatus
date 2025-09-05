package com.intsoftdev.londontubestatus

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.intsoftdev.londontubestatus.presentation.TFLStatusScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun TFLApp() {
    MaterialTheme {
        TFLStatusScreen()
    }
}