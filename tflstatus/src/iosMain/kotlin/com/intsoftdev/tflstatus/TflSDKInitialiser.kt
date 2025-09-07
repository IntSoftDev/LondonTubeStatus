package com.intsoftdev.tflstatus

import com.intsoftdev.tflstatus.di.tflStatusDiModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

private var sdkInitialised = false

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