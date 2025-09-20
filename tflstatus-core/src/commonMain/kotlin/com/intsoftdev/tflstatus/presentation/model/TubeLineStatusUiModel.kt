package com.intsoftdev.tflstatus.presentation.model

import androidx.compose.ui.graphics.Color

data class TubeLineStatusUiModel(
    val id: String,
    val displayName: String,
    val statusText: String,
    val statusSeverity: StatusSeverity,
    val disruptionReason: String?,
    val backgroundColor: Color,
    val textColor: Color,
    val hasDisruption: Boolean,
) {
    enum class StatusSeverity(val displayOrder: Int) {
        GOOD_SERVICE(1),
        MINOR_DELAYS(2),
        SEVERE_DELAYS(3),
        PART_CLOSURE(4),
        PLANNED_CLOSURE(5),
        SERVICE_CLOSED(6),
        UNKNOWN(99),
    }
}
