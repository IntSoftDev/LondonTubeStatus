package com.intsoftdev.tflstatus.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StopPoint(
    @SerialName("accessibilitySummary")
    val accessibilitySummary: String = "",
    @SerialName("additionalProperties")
    val additionalProperties: List<AdditionalProperty> = listOf(),
    @SerialName("children")
    val children: List<Children> = listOf(),
    @SerialName("childrenUrls")
    val childrenUrls: List<String> = listOf(),
    @SerialName("commonName")
    val commonName: String = "",
    @SerialName("distance")
    val distance: Int = 0,
    @SerialName("fullName")
    val fullName: String = "",
    @SerialName("hubNaptanCode")
    val hubNaptanCode: String = "",
    @SerialName("icsCode")
    val icsCode: String = "",
    @SerialName("id")
    val id: String = "",
    @SerialName("indicator")
    val indicator: String = "",
    @SerialName("lat")
    val lat: Int = 0,
    @SerialName("lineGroup")
    val lineGroup: List<LineGroup> = listOf(),
    @SerialName("lineModeGroups")
    val lineModeGroups: List<LineModeGroup> = listOf(),
    @SerialName("lines")
    val lines: List<Line> = listOf(),
    @SerialName("lon")
    val lon: Int = 0,
    @SerialName("modes")
    val modes: List<String> = listOf(),
    @SerialName("naptanId")
    val naptanId: String = "",
    @SerialName("naptanMode")
    val naptanMode: String = "",
    @SerialName("placeType")
    val placeType: String = "",
    @SerialName("platformName")
    val platformName: String = "",
    @SerialName("smsCode")
    val smsCode: String = "",
    @SerialName("stationNaptan")
    val stationNaptan: String = "",
    @SerialName("status")
    val status: Boolean = false,
    @SerialName("stopLetter")
    val stopLetter: String = "",
    @SerialName("stopType")
    val stopType: String = "",
    @SerialName("url")
    val url: String = ""
)