package com.intsoftdev.tflstatus.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrainLoading(
    @SerialName("direction")
    val direction: String = "",
    @SerialName("line")
    val line: String = "",
    @SerialName("lineDirection")
    val lineDirection: String = "",
    @SerialName("naptanTo")
    val naptanTo: String = "",
    @SerialName("platformDirection")
    val platformDirection: String = "",
    @SerialName("timeSlice")
    val timeSlice: String = "",
    @SerialName("value")
    val value: Int = 0,
)
