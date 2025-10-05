package com.intsoftdev.tflstatus.model.tfl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Children(
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
    @SerialName("id")
    val id: String = "",
    @SerialName("lat")
    val lat: Int = 0,
    @SerialName("lon")
    val lon: Int = 0,
    @SerialName("placeType")
    val placeType: String = "",
    @SerialName("url")
    val url: String = "",
)
