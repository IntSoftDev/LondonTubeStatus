package com.intsoftdev.tflstatus.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.intsoftdev.tflstatus.ui.constants.TubeLineColors.BackgroundColors.ContainerBackground

/**
 * Main entry point for TFL Status UI that can be called from any app menu.
 * This assumes Koin is already initialised in the parent app with TFL Status modules.
 *
 * Usage:
 * ```
 * // In your Application class:
 * startKoin {
 *     modules(tflStatusDiModule) // or your app's modules that include TFL Status
 * }
 *
 * // In your Composable:
 * TFLStatusUI(onBackPressed = { ... })
 * ```
 */
@Composable
fun tflStatusUI(
    modifier: Modifier = Modifier,
    onBackPressed: (() -> Unit)? = null,
    showTitle: Boolean = false,
    title: String = "London Tube Status",
    backgroundColor: Color = ContainerBackground,
) {
    tflStatusScreen(
        modifier = modifier,
        onBackPressed = onBackPressed,
        showTitle = showTitle,
        title = title,
        backgroundColor = backgroundColor,
    )
}

@Composable
private fun tflStatusScreen(
    modifier: Modifier = Modifier,
    onBackPressed: (() -> Unit)? = null,
    showTitle: Boolean = true,
    title: String = "London Tube Status",
    backgroundColor: Color,
) {
    tflStatusScreenInternal(
        modifier = modifier,
        showBackButton = onBackPressed != null,
        onBackPressed = onBackPressed ?: {},
        showTitle = showTitle,
        title = title,
        backgroundColor = backgroundColor,
    )
}
