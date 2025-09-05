package com.intsoftdev.tflstatus.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdditionalProperty(
    @SerialName("category")
    val category: String = "",
    @SerialName("key")
    val key: String = "",
    @SerialName("modified")
    val modified: String = "",
    @SerialName("sourceSystemKey")
    val sourceSystemKey: String = "",
    @SerialName("value")
    val value: String = ""
)