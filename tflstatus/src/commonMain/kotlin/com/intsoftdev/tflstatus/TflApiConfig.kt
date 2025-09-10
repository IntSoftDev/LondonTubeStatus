package com.intsoftdev.tflstatus

import com.intsoftdev.tflstatus.TflApiConfig.Companion.APP_ID_KEY
import com.intsoftdev.tflstatus.TflApiConfig.Companion.APP_KEY

data class TflApiConfig(
    val appId: String? = null,
    val appKey: String? = null
) {
    companion object {
        const val APP_ID_KEY = "APP_ID_KEY"
        const val APP_KEY = "APP_KEY"
    }
}

internal fun TflApiConfig.toKoinProperties(): Map<String, Any> =
    listOfNotNull(
        this.appId?.let { APP_ID_KEY to it },
        this.appKey?.let { APP_KEY to it }
    ).toMap()
