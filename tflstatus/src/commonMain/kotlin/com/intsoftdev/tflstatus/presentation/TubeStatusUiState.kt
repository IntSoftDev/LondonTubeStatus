package com.intsoftdev.tflstatus.presentation

import com.intsoftdev.tflstatus.model.TFLStatusResponseItem

sealed interface TubeStatusUiState {
    data class Error(val message: String) : TubeStatusUiState
    data object Loading : TubeStatusUiState
    data class Success(
        val lineStatuses: List<TFLStatusResponseItem>,
    ) : TubeStatusUiState
}