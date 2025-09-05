package com.intsoftdev.tflstatus.network

import com.intsoftdev.tflstatus.model.TFLStatusResponseItem
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

var appId = "9c1dddca"
var appKey = "ecb2f03195b093ac2cbc3a21b8fc5b42"

internal interface TFLStatusAPI {
    suspend fun getLineStatuses(
        lineIds: String
    ): List<TFLStatusResponseItem>
}

//https://api.tfl.gov.uk/Line/victoria,central/Status?detail=false&appId=9c1dddca&appKey=ecb2f03195b093ac2cbc3a21b8fc5b42
internal class TFLStatusProxy(
    private val httpClient: HttpClient,
    private val appId: String?,
    private val appKey: String?
) : TFLStatusAPI {
    private val tflAPIEndPoint = "https://api.tfl.gov.uk/"

    override suspend fun getLineStatuses(lineIds: String): List<TFLStatusResponseItem> {
        if (appId == null || appKey == null) {
            throw IllegalArgumentException("invalid appId or appKey")
        }
        return httpClient.get("$tflAPIEndPoint/Line/$lineIds/Status?detail=false&appId=$appId&appKey=$appKey")
            .body()
    }
}