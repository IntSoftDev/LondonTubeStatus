package com.intsoftdev.londontubestatus

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.intsoftdev.tflstatus.ui.mocks.tflStatusSample
import com.intsoftdev.tflstatus.ui.tflStatusUI
import org.jetbrains.compose.ui.tooling.preview.Preview

private val TflDarkScheme =
    darkColorScheme(
        primary = Color(0xFF003366),
        secondary = Color(0xFF03DAC6),
        background = Color(0xFF121212),
        surface = Color(0xFF003366),
        surfaceContainer = Color(0xFF003366),
        surfaceContainerHigh = Color(0xFF003366),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color(0xFFE6E6E6),
        onSurface = Color.White,
    )

@Suppress("ComposableNaming")
@Composable
fun tfLStatusTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) TflDarkScheme else lightColorScheme(),
        typography = androidx.compose.material3.Typography(),
        content = content,
    )
}

@Suppress("ComposableNaming")
@Composable
fun tflStatusApp() {
    tfLStatusTheme(darkTheme = true) {
        tflStatusUI(
            showTitle = true,
            title = "London Tube Status",
            onBackPressed = null,
        )
    }
}

@Suppress("ComposableNaming")
@Preview
@Composable
fun tflPreview() {
    tflStatusSample()
}
