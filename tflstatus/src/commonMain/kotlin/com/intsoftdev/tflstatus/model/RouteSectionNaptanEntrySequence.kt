package com.intsoftdev.tflstatus.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteSectionNaptanEntrySequence(
    @SerialName("ordinal")
    val ordinal: Int = 0,
    @SerialName("stopPoint")
    val stopPoint: StopPoint = StopPoint()
)