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
 * Called from iOS to create the main view controller for displaying TFL Status information.
 *
 * @param showBackButton Whether to show the back button in the UI (default: true)
 * @param onBackPressed Optional callback function invoked when back button is pressed
 * @param enableLogging Whether to enable logging for the TFL SDK (default: true)
 * @param apiConfig Configuration object for the TFL API (default: TflApiConfig())
 * @return UIViewController containing the TFL Status screen with Compose UI
 *
 * @sample
 * ```swift
 * // From iOS Swift code:
 * let viewController = TFLStatusBridgeKt.createTFLStatusViewController(
 *     showBackButton: true,
 *     onBackPressed: {
 *         // Handle back button press
 *     },
 *     enableLogging: true,
 *     apiConfig: TflApiConfig()
 * )
 * ```
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