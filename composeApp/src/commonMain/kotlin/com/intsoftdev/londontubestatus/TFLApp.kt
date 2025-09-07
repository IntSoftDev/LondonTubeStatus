package com.intsoftdev.londontubestatus

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.intsoftdev.tflstatus.ui.TFLStatusSample
import com.intsoftdev.tflstatus.ui.TFLStatusUI
import com.intsoftdev.tflstatus.ui.TubeLineCardSample
import org.jetbrains.compose.ui.tooling.preview.Preview

private val TflDarkScheme = darkColorScheme(
    primary = Color(0xFF0078D4),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFFE6E6E6),
    onSurface = Color(0xFFE6E6E6)
)

@Composable
fun TFLTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) TflDarkScheme else lightColorScheme(),
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}

@Composable
fun TFLApp() {
    TFLTheme(darkTheme = true)  {
        TFLStatusUI(
            showTitle = true,
            title = "London Tube Status",
            onBackPressed = null // No back button needed for main screen
        )
    }
}

@Preview
@Composable
fun MyFullTFLPreview() {
    TFLStatusSample()
}

@Preview
@Composable
fun MyTFLPreview() {
    // Use the sample composables from the library
    TubeLineCardSample()
}