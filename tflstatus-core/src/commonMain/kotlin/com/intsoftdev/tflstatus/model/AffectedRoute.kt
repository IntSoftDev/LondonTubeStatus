package com.intsoftdev.tflstatus.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AffectedRoute(
    @SerialName("destinationName")
    val destinationName: String = "",
    @SerialName("direction")
    val direction: String = "",
    @SerialName("id")
    val id: String = "",
    @SerialName("isEntireRouteSection")
    val isEntireRouteSection: Boolean = false,
    @SerialName("lineId")
    val lineId: String = "",
    @SerialName("lineString")
    val lineString: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("originationName")
    val originationName: String = "",
    @SerialName("routeCode")
    val routeCode: String = "",
    @SerialName("routeSectionNaptanEntrySequence")
    val routeSectionNaptanEntrySequence: List<RouteSectionNaptanEntrySequence> = listOf(),
    @SerialName("validFrom")
    val validFrom: String = "",
    @SerialName("validTo")
    val validTo: String = "",
    @SerialName("via")
    val via: Via = Via(),
)
