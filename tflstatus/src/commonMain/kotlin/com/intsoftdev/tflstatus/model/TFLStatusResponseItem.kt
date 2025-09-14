package com.intsoftdev.tflstatus.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TFLStatusResponseItem(
    @SerialName("created")
    val created: String = "",
    @SerialName("crowding")
    val crowding: Crowding = Crowding(),
    @SerialName("disruptions")
    val disruptions: List<Disruption> = listOf(),
    @SerialName("id")
    val id: String = "",
    @SerialName("lineStatuses")
    val lineStatuses: List<LineStatuse> = listOf(),
    @SerialName("modeName")
    val modeName: String = "",
    @SerialName("modified")
    val modified: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("routeSections")
    val routeSections: List<RouteSection> = listOf(),
    @SerialName("serviceTypes")
    val serviceTypes: List<ServiceType> = listOf(),
)
