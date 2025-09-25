package com.intsoftdev.tflstatus.ui.model

import androidx.compose.ui.graphics.Color
import com.intsoftdev.tflstatus.presentation.model.TubeLineStatusModel
import com.intsoftdev.tflstatus.presentation.model.TubeLineStatusSeverity
import com.intsoftdev.tflstatus.ui.constants.TubeLineColors

data class TubeLineStatusUiModel(
    val id: String,
    val displayName: String,
    val statusText: String,
    val statusSeverity: TubeLineStatusSeverity,
    val disruptionReason: String?,
    val backgroundColor: Color,
    val textColor: Color,
    val hasDisruption: Boolean,
)

internal fun TubeLineStatusModel.toUiModel(): TubeLineStatusUiModel {
    val colorScheme = TubeLineColors.getLineColorScheme(id)

    return TubeLineStatusUiModel(
        id = id,
        displayName = displayName,
        statusText = statusText,
        statusSeverity = statusSeverity,
        disruptionReason = disruptionReason,
        backgroundColor = colorScheme.backgroundColor,
        textColor = colorScheme.textColor,
        hasDisruption = disruptionReason != null,
    )
}
