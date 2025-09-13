package com.intsoftdev.tflstatus

import com.intsoftdev.tflstatus.TflApiConfig.Companion.APP_ID_KEY
import com.intsoftdev.tflstatus.TflApiConfig.Companion.APP_KEY
import com.intsoftdev.tflstatus.di.tflStatusDiModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

/**
 * Initialises the TFL Status SDK for Android applications.
 *
 * This function sets up the necessary dependencies and configuration required for the SDK to work properly.
 * It should be called once during application startup, typically in your Application class.
 *
 * @param context The Android context, typically the application context
 * @param koinApplication Optional existing Koin application instance. If null, a new Koin instance will be started.
 *                       Use this parameter if you already have Koin configured in your app and want to add the SDK modules to it.
 * @param enableLogging Whether to enable debug logging for the SDK. Default is true.
 *                     When enabled, the SDK will output debug information using Napier logger.
 * @param apiConfig Configuration for TFL API credentials and settings. Use [TflApiConfig] to specify
 *                 your TFL API key and app ID if you have them. Default uses no credentials (may have rate limits).
 *
 * @throws IllegalStateException if Koin is already started and koinApplication parameter is null
 *
 * Example usage:
 * ```kotlin
 * class MyApplication : Application() {
 *     override fun onCreate() {
 *         super.onCreate()
 *
 *         // Basic initialisation
 *         initTflSDK(context = this)
 *
 *         // With custom API configuration
 *         val apiConfig = TflApiConfig(
 *             appId = "your-app-id",
 *             appKey = "your-api-key"
 *         )
 *         initTflSDK(context = this, apiConfig = apiConfig)
 *
 *         // With existing Koin application
 *         val koinApp = startKoin { /* your modules */ }
 *         initTflSDK(context = this, koinApplication = koinApp)
 *     }
 * }
 * ```
 */
fun initTflSDK(
    context: android.content.Context,
    koinApplication: KoinApplication? = null,
    enableLogging: Boolean = true,
    apiConfig: TflApiConfig = TflApiConfig()
) {
    if (enableLogging) {
        Napier.base(DebugAntilog())
    }
    if (koinApplication == null) {
        startKoin {
            androidContext(context)
            properties(apiConfig.toKoinProperties())
            modules(tflStatusDiModule)
        }
    } else {
        koinApplication.androidContext(context)
        koinApplication.koin.loadModules(listOf(tflStatusDiModule))
        apiConfig.appKey?.let {
            koinApplication.koin.setProperty(APP_KEY, it)
        }
        apiConfig.appId?.let {
            koinApplication.koin.setProperty(APP_ID_KEY, it)
        }
    }
}