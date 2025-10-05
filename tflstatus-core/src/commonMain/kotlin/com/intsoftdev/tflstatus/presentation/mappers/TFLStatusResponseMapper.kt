package com.intsoftdev.tflstatus.presentation.mappers

import com.intsoftdev.tflstatus.model.tfl.TFLStatusResponseItem
import com.intsoftdev.tflstatus.presentation.model.TubeLineStatusModel
import com.intsoftdev.tflstatus.presentation.model.TubeLineStatusSeverity

fun TFLStatusResponseItem.toPresentableModel(): TubeLineStatusModel {
    val statusInfo = getStatusInfo()

    return TubeLineStatusModel(
        id = id,
        displayName = name.formatDisplayName(),
        statusText = statusInfo.text,
        statusSeverity = statusInfo.severity,
        disruptionReason = getDisruptionReason(),
    )
}

fun List<TFLStatusResponseItem>.toPresentableModel(): List<TubeLineStatusModel> {
    return this.map { it.toPresentableModel() }
}

fun mapStatusSeverity(statusSeverity: Int): TubeLineStatusSeverity {
    return when (statusSeverity) {
        10 -> TubeLineStatusSeverity.GOOD_SERVICE
        9 -> TubeLineStatusSeverity.MINOR_DELAYS
        8 -> TubeLineStatusSeverity.MINOR_DELAYS
        7 -> TubeLineStatusSeverity.SEVERE_DELAYS
        6 -> TubeLineStatusSeverity.PART_CLOSURE
        5 -> TubeLineStatusSeverity.PART_CLOSURE
        4 -> TubeLineStatusSeverity.PLANNED_CLOSURE
        3 -> TubeLineStatusSeverity.SERVICE_CLOSED
        2 -> TubeLineStatusSeverity.SERVICE_CLOSED
        1 -> TubeLineStatusSeverity.SERVICE_CLOSED
        0 -> TubeLineStatusSeverity.SERVICE_CLOSED
        else -> TubeLineStatusSeverity.UNKNOWN
    }
}

private fun TFLStatusResponseItem.getStatusInfo(): StatusInfo {
    val status = lineStatuses.firstOrNull()
    return when {
        status == null -> StatusInfo("No status information", TubeLineStatusSeverity.UNKNOWN)
        status.statusSeverityDescription.isEmpty() ->
            StatusInfo(
                "Unknown",
                TubeLineStatusSeverity.UNKNOWN,
            )

        else -> {
            val text = status.statusSeverityDescription
            val severity = mapStatusSeverity(status.statusSeverity)
            StatusInfo(text, severity)
        }
    }
}

private fun TFLStatusResponseItem.getDisruptionReason(): String? {
    return lineStatuses.firstOrNull()?.reason?.takeIf { it.isNotEmpty() }
}

private fun String.formatDisplayName(): String {
    return when (this.lowercase()) {
        "elizabeth line" -> "Elizabeth line"
        "london overground" -> "London Overground"
        "hammersmith & city" -> "Hammersmith & City"
        "waterloo & city" -> "Waterloo & City"
        else ->
            this.split(" ").joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercase() }
            }
    }
}

private data class StatusInfo(
    val text: String,
    val severity: TubeLineStatusSeverity,
)
