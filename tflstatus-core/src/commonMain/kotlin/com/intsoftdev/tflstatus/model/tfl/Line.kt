package com.intsoftdev.tflstatus.model.tfl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Line(
    @SerialName("crowding")
    val crowding: Crowding = Crowding(),
    @SerialName("fullName")
    val fullName: String = "",
    @SerialName("id")
    val id: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("routeType")
    val routeType: String = "",
    @SerialName("status")
    val status: String = "",
    @SerialName("type")
    val type: String = "",
    @SerialName("uri")
    val uri: String = "",
)
