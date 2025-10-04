package com.intsoftdev.tflstatus.presentation

import com.intsoftdev.tflstatus.presentation.model.TubeLineStatusModel

sealed interface TubeStatusUiState {
    data object Loading : TubeStatusUiState

    data class Error(val message: String) : TubeStatusUiState

    data class Success(
        val tubeLines: List<TubeLineStatusModel>,
    ) : TubeStatusUiState
}
