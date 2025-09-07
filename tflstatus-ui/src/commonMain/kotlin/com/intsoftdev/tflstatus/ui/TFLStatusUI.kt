package com.intsoftdev.tflstatus.ui

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Main entry point for TFL Status UI that can be called from any app menu.
 * This assumes Koin is already initialized in the parent app with TFL Status modules.
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
    backgroundColor: Color = Blue600
) {
    TFLStatusScreen(
        modifier = modifier,
        onBackPressed = onBackPressed,
        showTitle = showTitle,
        title = title,
        backgroundColor = backgroundColor
    )
}

/**
 * Alternative entry point that assumes Koin is already initialized in the parent app.
 * Use this if your app already has Koin set up.
 */
@Composable
fun TFLStatusScreen(
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
 * For apps that want to customize the theme, this provides just the content
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
