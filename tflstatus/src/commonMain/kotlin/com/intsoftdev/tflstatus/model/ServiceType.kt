package com.intsoftdev.tflstatus.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceType(
    @SerialName("name")
    val name: String = "",
    @SerialName("uri")
    val uri: String = ""
)