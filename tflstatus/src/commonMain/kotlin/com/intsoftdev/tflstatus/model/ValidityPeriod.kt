package com.intsoftdev.tflstatus.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ValidityPeriod(
    @SerialName("fromDate")
    val fromDate: String = "",
    @SerialName("isNow")
    val isNow: Boolean = false,
    @SerialName("toDate")
    val toDate: String = ""
)