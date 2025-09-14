package com.intsoftdev.tflstatus.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Crowding(
    @SerialName("passengerFlows")
    val passengerFlows: List<PassengerFlow> = listOf(),
    @SerialName("trainLoadings")
    val trainLoadings: List<TrainLoading> = listOf(),
)
