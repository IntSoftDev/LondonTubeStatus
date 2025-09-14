package com.intsoftdev.tflstatus.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Helper functions for TFL Status UI integration with different DI approaches.
 */

/**
 * Creates a TFL Status UI with manual dependency injection.
 * Use this when you want to provide dependencies explicitly without Koin.
 *
 * @param getTFLStatusUseCase The use case for fetching TFL status data
 * @param onBackPressed Callback when back button is pressed
 */
@Composable
fun tflStatusUIWithDependencies(
    modifier: Modifier = Modifier,
    onBackPressed: (() -> Unit)? = null,
    // You would need to add the specific dependencies here
    // getTFLStatusUseCase: GetTFLStatusUseCase
) {
    // This would create a ViewModel manually with provided dependencies
    // For now, falls back to the standard approach
    tflStatusUI(
        modifier = modifier,
        onBackPressed = onBackPressed,
    )
}

/**
 * Extension function to help with integration setup.
 */
object TFLStatusUIHelper {
    /**
     * Instructions for setting up TFL Status UI in your app.
     */
    const val SETUP_INSTRUCTIONS = """
        To use TFL Status UI in your app:
        
        1. Add dependency injection in your Application class:
        ```kotlin
        class YourApplication : Application() {
            override fun onCreate() {
                super.onCreate()
                startKoin {
                    androidContext(this@YourApplication)
                    modules(tflStatusDiModule) // Import: com.intsoftdev.tflstatus.di.tflStatusDiModule
                }
            }
        }
        ```
        
        2. Use in your Composable:
        ```kotlin
        TFLStatusUI(
            onBackPressed = { navController.popBackStack() }
        )
        ```
    """
}
