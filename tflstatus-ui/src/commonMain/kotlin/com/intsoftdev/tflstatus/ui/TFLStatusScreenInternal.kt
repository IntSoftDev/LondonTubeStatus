package com.intsoftdev.tflstatus.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.intsoftdev.tflstatus.model.TFLStatusResponseItem
import com.intsoftdev.tflstatus.model.LineStatuse
import com.intsoftdev.tflstatus.presentation.TubeStatusUiState
import com.intsoftdev.tflstatus.presentation.TubeStatusViewModel
import com.intsoftdev.tflstatus.presentation.TubeStatusViewModel.Companion.TFL_LINE_IDS
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TFLStatusScreenInternal(
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    onBackPressed: () -> Unit = {},
    viewModel: TubeStatusViewModel = koinInject(),
    showTitle: Boolean = true,
    title: String = "London Tube Status",
    backgroundColor: Color = Color(0xFF26285F)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getLineStatuses(TFL_LINE_IDS)
    }

    Scaffold(
        topBar = {
            if (showTitle) {
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        if (showBackButton) {
                            TextButton(onClick = onBackPressed) {
                                Text(
                                    text = "←",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                )
            }
        },
        containerColor = backgroundColor,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(if (showTitle) paddingValues else PaddingValues(0.dp))
        ) {
            when (val currentState = uiState) {
                is TubeStatusUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(75.dp),
                        strokeWidth = 6.dp
                    )
                }

                is TubeStatusUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading tube status",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                viewModel.getLineStatuses(TFL_LINE_IDS)
                            }
                        ) {
                            Text("Retry")
                        }
                    }
                }

                is TubeStatusUiState.Success -> {
                    if (currentState.lineStatuses.isEmpty()) {
                        Text(
                            text = "No tube lines found",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            items(currentState.lineStatuses) { line ->
                                TubeLineCard(line = line)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun TubeLineCard(
    line: TFLStatusResponseItem,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val lineColorInfo = getTFLLineColors(line.name.lowercase())

    val hasDisruptionReason = line.lineStatuses.isNotEmpty() &&
            line.lineStatuses.first().reason.isNotEmpty()

    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(24.dp), // More rounded like the screenshot
        colors = CardDefaults.cardColors(
            containerColor = lineColorInfo.backgroundColor
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Main content row with pill shape design
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Line name - larger and bold like screenshot
                Text(
                    text = line.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = lineColorInfo.textColor,
                    textAlign = TextAlign.Start
                )

                // Service status and chevron
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (line.lineStatuses.isNotEmpty()) {
                            line.lineStatuses.first().statusSeverityDescription.ifEmpty { "Unknown" }
                        } else {
                            "No status information"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        color = lineColorInfo.textColor,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End
                    )

                    if (hasDisruptionReason) {
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { isExpanded = !isExpanded },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (isExpanded) "Collapse disruption details" else "Expand disruption details",
                                tint = lineColorInfo.textColor
                            )
                        }
                    }
                }
            }

            // Expandable disruption reason
            if (hasDisruptionReason) {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Text(
                        text = line.lineStatuses.first().reason,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 16.dp),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = lineColorInfo.textColor.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

internal data class TFLLineColorInfo(
    val backgroundColor: Color,
    val textColor: Color
)

@Composable
internal fun getTFLLineColors(lineName: String): TFLLineColorInfo {
    return when (lineName) {
        "bakerloo" -> TFLLineColorInfo(
            backgroundColor = Color(0xFFB36305),
            textColor = Color.White
        )

        "circle" -> TFLLineColorInfo(
            backgroundColor = Color(0xFFFFD300),
            textColor = Color.Black
        )

        "central" -> TFLLineColorInfo(
            backgroundColor = Color(0xFFE32017),
            textColor = Color.White
        )

        "district" -> TFLLineColorInfo(
            backgroundColor = Color(0xFF00782A),
            textColor = Color.White
        )

        "elizabeth line" -> TFLLineColorInfo(
            backgroundColor = Color(0xFF6950A1),
            textColor = Color.White
        )

        "hammersmith & city" -> TFLLineColorInfo(
            backgroundColor = Color(0xFFF3A9BB),
            textColor = Color.Black
        )

        "jubilee" -> TFLLineColorInfo(
            backgroundColor = Color(0xFF7A7A7A),
            textColor = Color.White
        )

        "metropolitan" -> TFLLineColorInfo(
            backgroundColor = Color(0xFF9B0056),
            textColor = Color.White
        )

        "northern" -> TFLLineColorInfo(
            backgroundColor = Color(0xFF000000),
            textColor = Color.White
        )

        "piccadilly" -> TFLLineColorInfo(
            backgroundColor = Color(0xFF003688),
            textColor = Color.White
        )

        "victoria" -> TFLLineColorInfo(
            backgroundColor = Color(0xFF0098D4),
            textColor = Color.White
        )

        "waterloo & city" -> TFLLineColorInfo(
            backgroundColor = Color(0xFF95CDBA),
            textColor = Color.Black
        )

        "london overground" -> TFLLineColorInfo(
            backgroundColor = Color(0xFFE86A10),
            textColor = Color.White
        )

        else -> TFLLineColorInfo(
            backgroundColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Sample composables for testing and development.
 * These can be used in your app's preview functions.
 */

@Composable
fun TubeLineCardSample() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(Color(0xFF1A1A2E))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TubeLineCard(
                line = TFLStatusResponseItem(
                    id = "bakerloo",
                    name = "Bakerloo",
                    lineStatuses = listOf(
                        LineStatuse(
                            statusSeverity = 10,
                            statusSeverityDescription = "Good Service",
                            reason = ""
                        )
                    )
                )
            )

            TubeLineCard(
                line = TFLStatusResponseItem(
                    id = "central",
                    name = "Central",
                    lineStatuses = listOf(
                        LineStatuse(
                            statusSeverity = 6,
                            statusSeverityDescription = "Part Closure",
                            reason = "Part closure between Liverpool Street and Leytonstone due to planned engineering work. Use alternative routes."
                        )
                    )
                )
            )

            TubeLineCard(
                line = TFLStatusResponseItem(
                    id = "jubilee",
                    name = "Jubilee",
                    lineStatuses = listOf(
                        LineStatuse(
                            statusSeverity = 9,
                            statusSeverityDescription = "Severe Delays",
                            reason = "Severe delays due to an earlier signal failure at Bond Street. Tickets are being accepted on local bus services."
                        )
                    )
                )
            )
        }
    }
}

@Composable
fun TFLStatusSample() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A2E))
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(getSampleTubeLines()) { line ->
                    TubeLineCard(line = line)
                }
            }
        }
    }
}

internal fun getSampleTubeLines(): List<TFLStatusResponseItem> {
    return listOf(
        TFLStatusResponseItem(
            id = "bakerloo",
            name = "Bakerloo",
            lineStatuses = listOf(
                LineStatuse(
                    statusSeverity = 10,
                    statusSeverityDescription = "Good Service",
                    reason = ""
                )
            )
        ),
        TFLStatusResponseItem(
            id = "central",
            name = "Central",
            lineStatuses = listOf(
                LineStatuse(
                    statusSeverity = 6,
                    statusSeverityDescription = "Part Closure",
                    reason = "Part closure between Liverpool Street and Leytonstone due to planned engineering work. Use alternative routes."
                )
            )
        ),
        TFLStatusResponseItem(
            id = "circle",
            name = "Circle",
            lineStatuses = listOf(
                LineStatuse(
                    statusSeverity = 10,
                    statusSeverityDescription = "Good Service",
                    reason = ""
                )
            )
        ),
        TFLStatusResponseItem(
            id = "district",
            name = "District",
            lineStatuses = listOf(
                LineStatuse(
                    statusSeverity = 10,
                    statusSeverityDescription = "Good Service",
                    reason = ""
                )
            )
        ),
        TFLStatusResponseItem(
            id = "elizabeth",
            name = "Elizabeth line",
            lineStatuses = listOf(
                LineStatuse(
                    statusSeverity = 10,
                    statusSeverityDescription = "Good Service",
                    reason = ""
                )
            )
        ),
        TFLStatusResponseItem(
            id = "hammersmith-city",
            name = "Hammersmith & City",
            lineStatuses = listOf(
                LineStatuse(
                    statusSeverity = 10,
                    statusSeverityDescription = "Good Service",
                    reason = ""
                )
            )
        ),
        TFLStatusResponseItem(
            id = "jubilee",
            name = "Jubilee",
            lineStatuses = listOf(
                LineStatuse(
                    statusSeverity = 9,
                    statusSeverityDescription = "Severe Delays",
                    reason = "Severe delays due to an earlier signal failure at Bond Street. Tickets are being accepted on local bus services."
                )
            )
        ),
        TFLStatusResponseItem(
            id = "metropolitan",
            name = "Metropolitan",
            lineStatuses = listOf(
                LineStatuse(
                    statusSeverity = 10,
                    statusSeverityDescription = "Good Service",
                    reason = ""
                )
            )
        ),
        TFLStatusResponseItem(
            id = "northern",
            name = "Northern",
            lineStatuses = listOf(
                LineStatuse(
                    statusSeverity = 10,
                    statusSeverityDescription = "Good Service",
                    reason = ""
                )
            )
        ),
        TFLStatusResponseItem(
            id = "piccadilly",
            name = "Piccadilly",
            lineStatuses = listOf(
                LineStatuse(
                    statusSeverity = 8,
                    statusSeverityDescription = "Minor Delays",
                    reason = "Minor delays due to train cancellations. Allow extra time for your journey."
                )
            )
        ),
        TFLStatusResponseItem(
            id = "victoria",
            name = "Victoria",
            lineStatuses = listOf(
                LineStatuse(
                    statusSeverity = 10,
                    statusSeverityDescription = "Good Service",
                    reason = ""
                )
            )
        ),
        TFLStatusResponseItem(
            id = "waterloo-city",
            name = "Waterloo & City",
            lineStatuses = listOf(
                LineStatuse(
                    statusSeverity = 4,
                    statusSeverityDescription = "Planned Closure",
                    reason = "Closed Saturday and Sunday. The line operates Monday to Friday only, usually from 06:20 to 21:30."
                )
            )
        )
    )
}