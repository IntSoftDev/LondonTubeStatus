package com.intsoftdev.tflstatus.network.di

import com.intsoftdev.tflstatus.TflApiConfig.Companion.APP_ID_KEY
import com.intsoftdev.tflstatus.TflApiConfig.Companion.APP_KEY
import com.intsoftdev.tflstatus.network.TFLStatusAPI
import com.intsoftdev.tflstatus.network.TFLStatusProxy
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
            prettyPrint = true
        }
    }

    single(named(HTTP_CLIENT)) {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.d(tag = "HTTP") { message }
                    }
                }
                level = LogLevel.INFO
            }
        }
    }

    factory<TFLStatusAPI> {
        TFLStatusProxy(
            httpClient = get(named(HTTP_CLIENT)),
            appId = getPropertyOrNull(APP_ID_KEY),
            appKey = getPropertyOrNull(APP_KEY)
        )
    }
}

internal const val HTTP_CLIENT = "TflStatusHttpClient"
