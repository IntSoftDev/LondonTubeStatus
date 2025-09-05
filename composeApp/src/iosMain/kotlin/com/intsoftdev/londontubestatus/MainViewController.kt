package com.intsoftdev.londontubestatus

import androidx.compose.ui.window.ComposeUIViewController
import com.intsoftdev.tflstatus.di.tflStatusDiModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin {
            modules(tflStatusDiModule)
        }
    }
) {
    TFLApp()
}