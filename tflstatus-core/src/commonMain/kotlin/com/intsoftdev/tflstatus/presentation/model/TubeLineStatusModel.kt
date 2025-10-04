package com.intsoftdev.tflstatus.presentation.model

data class TubeLineStatusModel(
    val id: String,
    val displayName: String,
    val statusText: String,
    val statusSeverity: TubeLineStatusSeverity,
    val disruptionReason: String?,
)
