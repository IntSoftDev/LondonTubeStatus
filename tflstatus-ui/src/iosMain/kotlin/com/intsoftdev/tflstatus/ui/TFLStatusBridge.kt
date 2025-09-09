package com.intsoftdev.tflstatus.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.intsoftdev.tflstatus.TflApiConfig
import com.intsoftdev.tflstatus.initTflSDK
import com.intsoftdev.tflstatus.presentation.TubeLineColors.BackgroundColors.ContainerBackground
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIViewController


/**
 * Creates a ComposeUIViewController with custom configuration.
 * Called from iOS to create the main view controller.
 *
 * @param showBackButton Whether to show the back button in the UI
 * @param onBackPressed Optional callback when back button is pressed
 * @return UIViewController containing the TFL Status screen
 */
@OptIn(ExperimentalForeignApi::class)
@Suppress("UNUSED_PARAMETER")
fun createTFLStatusViewController(
    showBackButton: Boolean = true,
    onBackPressed: (() -> Unit)? = null,
    enableLogging: Boolean = true,
    apiConfig: TflApiConfig = TflApiConfig(),
): UIViewController {
    initTflSDK(enableLogging, apiConfig)
    return ComposeUIViewController {
        if (showBackButton && onBackPressed != null) {
            TFLStatusUI(
                modifier = Modifier.fillMaxSize(),
                onBackPressed = onBackPressed
            )
        } else {
            // Use the screen content without back button
            TFLStatusScreenContent(
                modifier = Modifier.fillMaxSize(),
                onBackPressed = null,
                backgroundColor = ContainerBackground
            )
        }
    }
}