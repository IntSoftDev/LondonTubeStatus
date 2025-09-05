package com.intsoftdev.tflstatus.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PassengerFlow(
    @SerialName("timeSlice")
    val timeSlice: String = "",
    @SerialName("value")
    val value: Int = 0
)