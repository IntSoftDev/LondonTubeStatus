package com.intsoftdev.tflstatus.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.intsoftdev.tflstatus.presentation.TubeLineConstants.TFL_LINE_IDS
import com.intsoftdev.tflstatus.presentation.TubeStatusUiState
import com.intsoftdev.tflstatus.presentation.TubeStatusViewModel
import com.intsoftdev.tflstatus.presentation.model.TubeLineStatusUiModel
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
    backgroundColor: Color
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isRefreshing by remember { mutableStateOf(false) }
    var refreshCount by remember { mutableStateOf(0) }

    // Handle pull-to-refresh - reset refreshing state when we get any non-loading state
    LaunchedEffect(uiState, refreshCount) {
        if (isRefreshing && uiState !is TubeStatusUiState.Loading) {
            isRefreshing = false
        }
    }

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
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                viewModel.getLineStatuses(TFL_LINE_IDS)
                            },
                            modifier = Modifier.padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = "Retry",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                is TubeStatusUiState.Success -> {
                    if (currentState.tubeLines.isEmpty()) {
                        Text(
                            text = "No tube lines found",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        PullToRefreshBox(
                            isRefreshing = isRefreshing,
                            onRefresh = {
                                isRefreshing = true
                                refreshCount++
                                viewModel.getLineStatuses(TFL_LINE_IDS)
                            },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    start = 16.dp,
                                    top = 32.dp,
                                    end = 16.dp,
                                    bottom = 64.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                currentState.lastUpdated?.let { timestamp ->
                                    item {
                                        Text(
                                            text = "Last updated: $timestamp",
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                items(currentState.tubeLines, key = { it.id }) { tubeLineUiModel ->
                                    TubeLineCard(tubeLineUiModel = tubeLineUiModel)
                                }
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
    tubeLineUiModel: TubeLineStatusUiModel,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(24.dp), // More rounded like the screenshot
        colors = CardDefaults.cardColors(
            containerColor = tubeLineUiModel.backgroundColor
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
                    text = tubeLineUiModel.displayName,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = tubeLineUiModel.textColor,
                    textAlign = TextAlign.Start
                )

                // Service status and chevron
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tubeLineUiModel.statusText,
                        style = MaterialTheme.typography.titleSmall,
                        color = tubeLineUiModel.textColor,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End
                    )

                    if (tubeLineUiModel.hasDisruption) {
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { isExpanded = !isExpanded },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (isExpanded) "Collapse disruption details" else "Expand disruption details",
                                tint = tubeLineUiModel.textColor
                            )
                        }
                    }
                }
            }

            // Expandable disruption reason
            tubeLineUiModel.disruptionReason?.let { reason ->
                AnimatedVisibility(visible = isExpanded) {
                    Text(
                        text = reason,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 16.dp),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = tubeLineUiModel.textColor.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
