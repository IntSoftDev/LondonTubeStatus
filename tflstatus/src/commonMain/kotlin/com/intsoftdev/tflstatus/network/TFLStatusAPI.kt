package com.intsoftdev.tflstatus.network

import com.intsoftdev.tflstatus.model.TFLStatusResponseItem
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

internal interface TFLStatusAPI {
    suspend fun getLineStatuses(
        lineIds: String
    ): List<TFLStatusResponseItem>
}

//https://api.tfl.gov.uk/Line/victoria,central/Status?detail=false&appId=appId&appKey=appKey
internal class TFLStatusProxy(
    private val httpClient: HttpClient,
    private val appId: String?,
    private val appKey: String?
) : TFLStatusAPI {
    private val tflAPIEndPoint = "https://api.tfl.gov.uk/"

    override suspend fun getLineStatuses(lineIds: String): List<TFLStatusResponseItem> {
        Napier.d("calling httpClient")
        if (appId == null || appKey == null) {
            // response might get throttled
            return httpClient.get("$tflAPIEndPoint/Line/$lineIds/Status?detail=false")
                .body()
        }
        return httpClient.get("$tflAPIEndPoint/Line/$lineIds/Status?detail=false&appId=$appId&appKey=$appKey")
            .body()
    }
}