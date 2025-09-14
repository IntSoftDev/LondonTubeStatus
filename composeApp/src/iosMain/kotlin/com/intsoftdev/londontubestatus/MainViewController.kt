package com.intsoftdev.londontubestatus

import androidx.compose.ui.window.ComposeUIViewController
import com.intsoftdev.tflstatus.initTflSDK

/**
 * Creates the main view controller for iOS integration.
 *
 * This function serves as the entry point for the Compose Multiplatform UI
 * when integrated into an iOS app. It initializes the TFL SDK with logging
 * enabled and returns a UIViewController that hosts the TFL status application.
 *
 * @return UIViewController containing the TFL status Compose UI
 */
@Suppress("unused")
fun mainViewController() =
    ComposeUIViewController(
        configure = {
            initTflSDK(enableLogging = true)
        },
    ) {
        tflApp()
    }
