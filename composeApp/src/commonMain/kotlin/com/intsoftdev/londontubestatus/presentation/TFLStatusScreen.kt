package com.intsoftdev.londontubestatus.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.intsoftdev.tflstatus.model.TFLStatusResponseItem
import com.intsoftdev.tflstatus.presentation.TubeStatusUiState
import com.intsoftdev.tflstatus.presentation.TubeStatusViewModel
import com.intsoftdev.tflstatus.presentation.TubeStatusViewModel.Companion.TFL_LINE_IDS
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TFLStatusScreen(
    modifier: Modifier = Modifier,
    viewModel: TubeStatusViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getLineStatuses(TFL_LINE_IDS)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "London Tube Status",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val currentState = uiState) {
                is TubeStatusUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
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
                            verticalArrangement = Arrangement.spacedBy(8.dp)
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
private fun TubeLineCard(
    line: TFLStatusResponseItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = line.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            if (line.modeName.isNotEmpty()) {
                Text(
                    text = line.modeName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (line.lineStatuses.isNotEmpty()) {
                line.lineStatuses.forEach { status ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = status.statusSeverityDescription.ifEmpty { "Unknown" },
                            style = MaterialTheme.typography.bodyMedium,
                            color = getStatusColor(status.statusSeverity)
                        )

                        if (status.reason.isNotEmpty()) {
                            Text(
                                text = status.reason,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "No status information available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (line.disruptions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${line.disruptions.size} disruption(s)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun getStatusColor(severity: Int): Color {
    return when (severity) {
        10 -> Color(0xFF4CAF50) // Good Service - Green
        20 -> Color(0xFFFF9800) // Minor Delays - Orange  
        30 -> Color(0xFFf44336) // Severe Delays - Red
        else -> MaterialTheme.colorScheme.onSurface // Default
    }
}