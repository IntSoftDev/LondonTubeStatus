package com.intsoftdev.tflstatus.model.tfl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LineStatuse(
    @SerialName("created")
    val created: String = "",
    @SerialName("disruption")
    val disruption: Disruption = Disruption(),
    @SerialName("id")
    val id: Int = 0,
    @SerialName("lineId")
    val lineId: String = "",
    @SerialName("modified")
    val modified: String = "",
    @SerialName("reason")
    val reason: String = "",
    @SerialName("statusSeverity")
    val statusSeverity: Int = 0,
    @SerialName("statusSeverityDescription")
    val statusSeverityDescription: String = "",
    @SerialName("validityPeriods")
    val validityPeriods: List<ValidityPeriod> = listOf(),
)
