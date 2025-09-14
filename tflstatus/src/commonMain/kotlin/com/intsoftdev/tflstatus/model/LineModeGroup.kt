package com.intsoftdev.tflstatus.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LineModeGroup(
    @SerialName("lineIdentifier")
    val lineIdentifier: List<String> = listOf(),
    @SerialName("modeName")
    val modeName: String = "",
)
