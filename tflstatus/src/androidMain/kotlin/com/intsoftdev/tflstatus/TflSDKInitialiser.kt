package com.intsoftdev.tflstatus

import com.intsoftdev.tflstatus.TflApiConfig.Companion.APP_ID_KEY
import com.intsoftdev.tflstatus.TflApiConfig.Companion.APP_KEY
import com.intsoftdev.tflstatus.di.tflStatusDiModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

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