package com.intsoftdev.tflstatus

import com.intsoftdev.tflstatus.di.tflStatusDiModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

private var sdkInitialised = false

/**
 * Initialises the TFL Status SDK for iOS applications.
 *
 * This function sets up the necessary dependencies and configuration required for the SDK to work properly.
 * It should be called once during application startup, typically in your main view controller or app delegate.
 *
 * The function includes built-in protection against multiple initializations - subsequent calls will be ignored
 * with a warning log message.
 *
 * @param enableLogging Whether to enable debug logging for the SDK. Default is true.
 *                     When enabled, the SDK will output debug information using Napier logger.
 * @param apiConfig Configuration for TFL API credentials and settings. Use [TflApiConfig] to specify
 *                 your TFL API key and app ID if you have them. Default uses no credentials (may have rate limits).
 *
 * @see TflApiConfig for API configuration options
 *
 * Example usage:
 * ```kotlin
 * // In your iOS main view controller
 * fun MainViewController(): UIViewController {
 *     // Basic initialization
 *     initTflSDK()
 *
 *     // With custom API configuration
 *     val apiConfig = TflApiConfig(
 *         appId = "your-app-id",
 *         appKey = "your-api-key"
 *     )
 *     initTflSDK(apiConfig = apiConfig)
 *
 *     // With logging disabled
 *     initTflSDK(enableLogging = false)
 *
 *     return ComposeUIViewController { App() }
 * }
 * ```
 *
 * @since 0.0.1
 */
fun initTflSDK(
    enableLogging: Boolean = true,
    apiConfig: TflApiConfig = TflApiConfig()
) {
    if (!sdkInitialised) {
        if (enableLogging) {
            Napier.base(DebugAntilog())
        }
        startKoin {
            properties(apiConfig.toKoinProperties())
            modules(tflStatusDiModule)
        }
        sdkInitialised = true
    } else {
        Napier.w("Tfl SDK already initialised")
    }
}