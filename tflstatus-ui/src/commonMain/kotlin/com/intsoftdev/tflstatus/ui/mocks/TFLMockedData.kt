package com.intsoftdev.tflstatus.ui.mocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intsoftdev.tflstatus.presentation.TFLLineConstants
import com.intsoftdev.tflstatus.presentation.model.TubeLineStatusSeverity
import com.intsoftdev.tflstatus.ui.constants.TubeLineColors
import com.intsoftdev.tflstatus.ui.model.TubeLineStatusUiModel
import com.intsoftdev.tflstatus.ui.tubeLineCard

/**
 * Sample composable for testing and development.
 * Can be used in app's preview functions.
 */

@Composable
fun tflStatusSample() {
    MaterialTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(TubeLineColors.BackgroundColors.SampleBackground),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(getSampleTubeLineUiModels()) { tubeLineUiModel ->
                    tubeLineCard(tubeLineUiModel = tubeLineUiModel)
                }
            }
        }
    }
}

internal fun getSampleTubeLineUiModels(): List<TubeLineStatusUiModel> {
    return listOf(
        TubeLineStatusUiModel(
            id = TFLLineConstants.BAKERLOO_ID,
            displayName = "Bakerloo",
            statusText = "Good Service",
            statusSeverity = TubeLineStatusSeverity.GOOD_SERVICE,
            disruptionReason = null,
            backgroundColor = TubeLineColors.LineColors.Bakerloo,
            textColor = TubeLineColors.TextColors.White,
            hasDisruption = false,
        ),
        TubeLineStatusUiModel(
            id = TFLLineConstants.CENTRAL_ID,
            displayName = "Central",
            statusText = "Part Closure",
            statusSeverity = TubeLineStatusSeverity.PART_CLOSURE,
            disruptionReason =
                "Part closure between Liverpool Street and Leytonstone due to planned engineering work. " +
                    "Use alternative routes.",
            backgroundColor = TubeLineColors.LineColors.Central,
            textColor = TubeLineColors.TextColors.White,
            hasDisruption = true,
        ),
        TubeLineStatusUiModel(
            id = TFLLineConstants.CIRCLE_ID,
            displayName = "Circle",
            statusText = "Good Service",
            statusSeverity = TubeLineStatusSeverity.GOOD_SERVICE,
            disruptionReason = null,
            backgroundColor = TubeLineColors.LineColors.Circle,
            textColor = TubeLineColors.TextColors.Black,
            hasDisruption = false,
        ),
        TubeLineStatusUiModel(
            id = TFLLineConstants.DISTRICT_ID,
            displayName = "District",
            statusText = "Good Service",
            statusSeverity = TubeLineStatusSeverity.GOOD_SERVICE,
            disruptionReason = null,
            backgroundColor = TubeLineColors.LineColors.District,
            textColor = TubeLineColors.TextColors.White,
            hasDisruption = false,
        ),
        TubeLineStatusUiModel(
            id = TFLLineConstants.ELIZABETH_ID,
            displayName = "Elizabeth line",
            statusText = "Good Service",
            statusSeverity = TubeLineStatusSeverity.GOOD_SERVICE,
            disruptionReason = null,
            backgroundColor = TubeLineColors.LineColors.ElizabethLine,
            textColor = TubeLineColors.TextColors.White,
            hasDisruption = false,
        ),
        TubeLineStatusUiModel(
            id = TFLLineConstants.HAMMERSMITH_CITY_ID,
            displayName = "Hammersmith & City",
            statusText = "Good Service",
            statusSeverity = TubeLineStatusSeverity.GOOD_SERVICE,
            disruptionReason = null,
            backgroundColor = TubeLineColors.LineColors.HammersmithCity,
            textColor = TubeLineColors.TextColors.Black,
            hasDisruption = false,
        ),
        TubeLineStatusUiModel(
            id = TFLLineConstants.JUBILEE_ID,
            displayName = "Jubilee",
            statusText = "Severe Delays",
            statusSeverity = TubeLineStatusSeverity.SEVERE_DELAYS,
            disruptionReason =
                "Severe delays due to an earlier signal failure at Bond Street. " +
                    "Tickets are being accepted on local bus services.",
            backgroundColor = TubeLineColors.LineColors.Jubilee,
            textColor = TubeLineColors.TextColors.White,
            hasDisruption = true,
        ),
        TubeLineStatusUiModel(
            id = TFLLineConstants.METROPOLITAN_ID,
            displayName = "Metropolitan",
            statusText = "Good Service",
            statusSeverity = TubeLineStatusSeverity.GOOD_SERVICE,
            disruptionReason = null,
            backgroundColor = TubeLineColors.LineColors.Metropolitan,
            textColor = TubeLineColors.TextColors.White,
            hasDisruption = false,
        ),
        TubeLineStatusUiModel(
            id = TFLLineConstants.NORTHERN_ID,
            displayName = "Northern",
            statusText = "Good Service",
            statusSeverity = TubeLineStatusSeverity.GOOD_SERVICE,
            disruptionReason = null,
            backgroundColor = TubeLineColors.LineColors.Northern,
            textColor = TubeLineColors.TextColors.White,
            hasDisruption = false,
        ),
        TubeLineStatusUiModel(
            id = TFLLineConstants.PICCADILLY_ID,
            displayName = "Piccadilly",
            statusText = "Minor Delays",
            statusSeverity = TubeLineStatusSeverity.MINOR_DELAYS,
            disruptionReason = "Minor delays due to train cancellations. Allow extra time for your journey.",
            backgroundColor = TubeLineColors.LineColors.Piccadilly,
            textColor = TubeLineColors.TextColors.White,
            hasDisruption = true,
        ),
        TubeLineStatusUiModel(
            id = TFLLineConstants.VICTORIA_ID,
            displayName = "Victoria",
            statusText = "Good Service",
            statusSeverity = TubeLineStatusSeverity.GOOD_SERVICE,
            disruptionReason = null,
            backgroundColor = TubeLineColors.LineColors.Victoria,
            textColor = TubeLineColors.TextColors.White,
            hasDisruption = false,
        ),
        TubeLineStatusUiModel(
            id = TFLLineConstants.WATERLOO_CITY_ID,
            displayName = "Waterloo & City",
            statusText = "Planned Closure",
            statusSeverity = TubeLineStatusSeverity.PLANNED_CLOSURE,
            disruptionReason = "Closed Saturday and Sunday. The line operates Monday to Friday only, usually from 06:20 to 21:30.",
            backgroundColor = TubeLineColors.LineColors.WaterlooCity,
            textColor = TubeLineColors.TextColors.Black,
            hasDisruption = true,
        ),
    )
}
