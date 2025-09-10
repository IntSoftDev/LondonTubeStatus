package com.intsoftdev.londontubestatus

import androidx.compose.ui.window.ComposeUIViewController
import com.intsoftdev.tflstatus.initTflSDK

fun MainViewController() = ComposeUIViewController(
    configure = {
        initTflSDK(enableLogging = true)
    }
) {
    TFLApp()
}