package com.intsoftdev.tflstatus.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intsoftdev.tflstatus.domain.GetTFLStatusUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TubeStatusViewModel(private val usecase: GetTFLStatusUseCase) : ViewModel() {

    private var _uiState = MutableStateFlow<TubeStatusUiState>(TubeStatusUiState.Loading)
    val uiState: StateFlow<TubeStatusUiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TubeStatusUiState.Loading
        )

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        //Napier.e(tag = TAG) { "exception handler $throwable" }
        viewModelScope.launch {
            _uiState.value = TubeStatusUiState.Error(message = throwable.message ?: "An unexpected error occurred")
        }
    }
    fun getLineStatuses(lineIds: String) {
        viewModelScope.launch(exceptionHandler) {
            val result = usecase(lineIds)
            _uiState.value = result.fold(
                onSuccess = { list -> TubeStatusUiState.Success(lineStatuses = list) },
                onFailure = {
                    err -> TubeStatusUiState.Error(message = err.message ?: "An unexpected error occurred")
                }
            )
        }
    }

    companion object {
        const val TFL_LINE_IDS = "victoria,circle,piccadilly,bakerloo,central,metropolitan,district,waterloo-city,hammersmith-city,jubilee,northern,elizabeth, london-overground"

    }
}
