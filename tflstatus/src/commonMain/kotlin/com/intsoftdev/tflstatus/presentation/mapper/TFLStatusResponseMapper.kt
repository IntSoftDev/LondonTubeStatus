package com.intsoftdev.tflstatus.presentation.mapper

import com.intsoftdev.tflstatus.model.TFLStatusResponseItem
import com.intsoftdev.tflstatus.presentation.TubeLineColors
import com.intsoftdev.tflstatus.presentation.model.TubeLineStatusUiModel
import com.intsoftdev.tflstatus.presentation.model.TubeLineStatusUiModel.StatusSeverity

fun TFLStatusResponseItem.toUiModel(): TubeLineStatusUiModel {
    val statusInfo = getStatusInfo()
    val colorScheme = TubeLineColors.getLineColorScheme(name)

    return TubeLineStatusUiModel(
        id = id,
        displayName = name.formatDisplayName(),
        statusText = statusInfo.text,
        statusSeverity = statusInfo.severity,
        disruptionReason = getDisruptionReason(),
        backgroundColor = colorScheme.backgroundColor,
        textColor = colorScheme.textColor,
        hasDisruption = hasDisruptionReason()
    )
}

fun List<TFLStatusResponseItem>.toUiModels(): List<TubeLineStatusUiModel> {
    return this.map { it.toUiModel() }
        .sortedWith(compareBy({ it.statusSeverity.displayOrder }, { it.displayName }))
}

private fun TFLStatusResponseItem.getStatusInfo(): StatusInfo {
    val status = lineStatuses.firstOrNull()
    return when {
        status == null -> StatusInfo("No status information", StatusSeverity.UNKNOWN)
        status.statusSeverityDescription.isEmpty() -> StatusInfo("Unknown", StatusSeverity.UNKNOWN)
        else -> {
            val text = status.statusSeverityDescription
            val severity = when (status.statusSeverity) {
                10 -> StatusSeverity.GOOD_SERVICE
                9 -> StatusSeverity.MINOR_DELAYS
                8 -> StatusSeverity.MINOR_DELAYS
                7 -> StatusSeverity.SEVERE_DELAYS
                6 -> StatusSeverity.PART_CLOSURE
                5 -> StatusSeverity.PART_CLOSURE
                4 -> StatusSeverity.PLANNED_CLOSURE
                3 -> StatusSeverity.SERVICE_CLOSED
                2 -> StatusSeverity.SERVICE_CLOSED
                1 -> StatusSeverity.SERVICE_CLOSED
                0 -> StatusSeverity.SERVICE_CLOSED
                else -> StatusSeverity.UNKNOWN
            }
            StatusInfo(text, severity)
        }
    }
}

private fun TFLStatusResponseItem.getDisruptionReason(): String? {
    return lineStatuses.firstOrNull()?.reason?.takeIf { it.isNotEmpty() }
}

private fun TFLStatusResponseItem.hasDisruptionReason(): Boolean {
    return getDisruptionReason() != null
}

private fun String.formatDisplayName(): String {
    return when (this.lowercase()) {
        "elizabeth line" -> "Elizabeth line"
        "london overground" -> "London Overground"
        "hammersmith & city" -> "Hammersmith & City"
        "waterloo & city" -> "Waterloo & City"
        else -> this.split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }
    }
}

private data class StatusInfo(
    val text: String,
    val severity: StatusSeverity
)
