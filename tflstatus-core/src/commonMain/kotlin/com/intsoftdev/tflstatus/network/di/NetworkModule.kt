package com.intsoftdev.tflstatus.network.di

import com.intsoftdev.tflstatus.TflApiConfig.Companion.APP_ID_KEY
import com.intsoftdev.tflstatus.TflApiConfig.Companion.APP_KEY
import com.intsoftdev.tflstatus.network.TFLStatusAPI
import com.intsoftdev.tflstatus.network.TFLStatusProxy
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val TIMEOUT_DURATION: Long = 10_000

val networkModule =
    module {
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
                expectSuccess = true
                install(ContentNegotiation) {
                    json(get())
                }
                install(Logging) {
                    logger =
                        object : Logger {
                            override fun log(message: String) {
                                Napier.d(tag = "HTTP") { message }
                            }
                        }
                    level = LogLevel.INFO
                }

                install(HttpRequestRetry) {
                    retryIf { request, response ->
                        !response.status.isSuccess()
                    }
                    exponentialDelay()
                }

                install(HttpTimeout) {
                    connectTimeoutMillis = TIMEOUT_DURATION
                    requestTimeoutMillis = TIMEOUT_DURATION
                    requestTimeoutMillis = TIMEOUT_DURATION
                    socketTimeoutMillis = TIMEOUT_DURATION
                }
            }
        }

        factory<TFLStatusAPI> {
            TFLStatusProxy(
                httpClient = get(named(HTTP_CLIENT)),
                appId = getPropertyOrNull(APP_ID_KEY),
                appKey = getPropertyOrNull(APP_KEY),
            )
        }
    }

internal const val HTTP_CLIENT = "TflStatusHttpClient"
