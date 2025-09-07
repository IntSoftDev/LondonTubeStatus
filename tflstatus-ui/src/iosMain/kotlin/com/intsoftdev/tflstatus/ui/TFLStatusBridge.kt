package com.intsoftdev.tflstatus.ui

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.context.startKoin
import com.intsoftdev.tflstatus.di.tflStatusDiModule
import platform.UIKit.UIViewController

private var koinStarted = false

/**
 * Call this once on iOS to ensure DI is available.
 * This is safe to call multiple times - it will only initialize once.
 */
fun ensureTflKoinStarted() {
    if (!koinStarted) {
        try {
            startKoin {
                modules(tflStatusDiModule)
            }
            koinStarted = true
        } catch (e: Exception) {
            // Koin may already be started by the host app, which is fine
            println("Koin initialization: ${e.message}")
            koinStarted = true
        }
    }
}

/**
 * Checks if we've attempted Koin initialization.
 * @return true if initialization was attempted, false otherwise
 */
fun isKoinInitialized(): Boolean {
    return koinStarted
}

/**
 * Creates a ComposeUIViewController with TFL Status UI.
 * Automatically initializes Koin if not already done.
 *
 * @param onBackPressed Optional callback when back button is pressed
 * @return UIViewController containing the TFL Status screen
 */
@OptIn(ExperimentalForeignApi::class)
fun createTFLStatusViewController(
    onBackPressed: (() -> Unit)? = null
): UIViewController {
    ensureTflKoinStarted()

    return ComposeUIViewController {
        TFLStatusUI(
            modifier = Modifier.fillMaxSize(),
            onBackPressed = onBackPressed
        )
    }
}

/**
 * Creates a ComposeUIViewController with TFL Status UI without automatic Koin initialization.
 * Use this if you want to handle Koin initialization yourself.
 * Will throw an exception if Koin initialization hasn't been attempted.
 *
 * @param onBackPressed Optional callback when back button is pressed
 * @return UIViewController containing the TFL Status screen
 * @throws IllegalStateException if Koin initialization hasn't been attempted
 */
@OptIn(ExperimentalForeignApi::class)
fun createTFLStatusViewControllerUnsafe(
    onBackPressed: (() -> Unit)? = null
): UIViewController {
    if (!isKoinInitialized()) {
        throw IllegalStateException("Koin initialization hasn't been attempted. Call ensureTflKoinStarted() first or use createTFLStatusViewController() instead.")
    }

    return ComposeUIViewController {
        TFLStatusUI(
            modifier = Modifier.fillMaxSize(),
            onBackPressed = onBackPressed
        )
    }
}

/**
 * Creates a ComposeUIViewController with custom configuration.
 *
 * @param showBackButton Whether to show the back button in the UI
 * @param onBackPressed Optional callback when back button is pressed
 * @return UIViewController containing the TFL Status screen
 */
@OptIn(ExperimentalForeignApi::class)
fun createTFLStatusViewControllerWithConfig(
    showBackButton: Boolean = true,
    onBackPressed: (() -> Unit)? = null
): UIViewController {
    ensureTflKoinStarted()

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
                onBackPressed = null
            )
        }
    }
}