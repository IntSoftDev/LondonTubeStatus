package com.intsoftdev.tflstatus.ui

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.intsoftdev.tflstatus.presentation.TubeLineColors.BackgroundColors.ContainerBackground

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
fun TFLStatusUI(
    modifier: Modifier = Modifier,
    onBackPressed: (() -> Unit)? = null,
    showTitle: Boolean = false,
    title: String = "London Tube Status",
    backgroundColor: Color = ContainerBackground
) {
    TFLStatusScreen(
        modifier = modifier,
        onBackPressed = onBackPressed,
        showTitle = showTitle,
        title = title,
        backgroundColor = backgroundColor
    )
}

@Composable
private fun TFLStatusScreen(
    modifier: Modifier = Modifier,
    onBackPressed: (() -> Unit)? = null,
    showTitle: Boolean = true,
    title: String = "London Tube Status",
    backgroundColor: Color
) {
    TFLStatusScreenContent(
        modifier = modifier,
        onBackPressed = onBackPressed,
        showTitle = showTitle,
        title = title,
        backgroundColor = backgroundColor
    )
}

/**
 * For apps that want to customise the theme, this provides just the content
 * without MaterialTheme wrapper.
 */
@Composable
fun TFLStatusScreenContent(
    modifier: Modifier = Modifier,
    onBackPressed: (() -> Unit)? = null,
    showTitle: Boolean = true,
    title: String = "London Tube Status",
    backgroundColor: Color
) {
    TFLStatusScreenInternal(
        modifier = modifier,
        showBackButton = onBackPressed != null,
        onBackPressed = onBackPressed ?: {},
        showTitle = showTitle,
        title = title,
        backgroundColor = backgroundColor
    )
}
