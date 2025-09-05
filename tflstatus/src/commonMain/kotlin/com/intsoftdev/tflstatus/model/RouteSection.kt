package com.intsoftdev.tflstatus.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteSection(
    @SerialName("destination")
    val destination: String = "",
    @SerialName("destinationName")
    val destinationName: String = "",
    @SerialName("direction")
    val direction: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("originationName")
    val originationName: String = "",
    @SerialName("originator")
    val originator: String = "",
    @SerialName("routeCode")
    val routeCode: String = "",
    @SerialName("serviceType")
    val serviceType: String = "",
    @SerialName("validFrom")
    val validFrom: String = "",
    @SerialName("validTo")
    val validTo: String = ""
)