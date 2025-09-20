package com.intsoftdev.tflstatus.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Disruption(
    @SerialName("additionalInfo")
    val additionalInfo: String = "",
    @SerialName("affectedRoutes")
    val affectedRoutes: List<AffectedRoute> = listOf(),
    @SerialName("affectedStops")
    val affectedStops: List<AffectedStop> = listOf(),
    @SerialName("category")
    val category: String = "",
    @SerialName("categoryDescription")
    val categoryDescription: String = "",
    @SerialName("closureText")
    val closureText: String = "",
    @SerialName("created")
    val created: String = "",
    @SerialName("description")
    val description: String = "",
    @SerialName("lastUpdate")
    val lastUpdate: String = "",
    @SerialName("summary")
    val summary: String = "",
    @SerialName("type")
    val type: String = "",
)
