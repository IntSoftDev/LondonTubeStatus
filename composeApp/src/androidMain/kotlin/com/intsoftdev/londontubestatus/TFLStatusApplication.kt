package com.intsoftdev.londontubestatus

import android.app.Application
import com.intsoftdev.tflstatus.TflApiConfig
import com.intsoftdev.tflstatus.initTflSDK

class TFLStatusApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Only initialize SDK if both appId and appKey are available
        val appId = BuildConfig.TFL_APP_ID
        val appKey = BuildConfig.TFL_APP_KEY

        if (appId.isNotEmpty() && appKey.isNotEmpty()) {
            val tflApiConfig =
                TflApiConfig(
                    appId = appId,
                    appKey = appKey,
                )
            initTflSDK(context = applicationContext, apiConfig = tflApiConfig)
        } else {
            initTflSDK(context = applicationContext)
        }
    }
}
