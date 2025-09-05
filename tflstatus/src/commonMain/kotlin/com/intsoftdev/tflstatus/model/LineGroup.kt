package com.intsoftdev.tflstatus.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LineGroup(
    @SerialName("lineIdentifier")
    val lineIdentifier: List<String> = listOf(),
    @SerialName("naptanIdReference")
    val naptanIdReference: String = "",
    @SerialName("stationAtcoCode")
    val stationAtcoCode: String = ""
)