package com.intsoftdev.tflstatus.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intsoftdev.tflstatus.domain.GetTFLStatusUseCase
import com.intsoftdev.tflstatus.presentation.mappers.toPresentableError
import com.intsoftdev.tflstatus.presentation.mappers.toPresentableModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TubeStatusViewModel(private val usecase: GetTFLStatusUseCase) : ViewModel() {
    private var _uiState = MutableStateFlow<TubeStatusUiState>(TubeStatusUiState.Loading)
    val uiState: StateFlow<TubeStatusUiState> =
        _uiState
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = TubeStatusUiState.Loading,
            )

    private val exceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Napier.e(tag = TAG) { "exception handler $throwable" }
            viewModelScope.launch {
                _uiState.value =
                    TubeStatusUiState.Error(message = throwable.toPresentableError())
            }
        }

    fun getLineStatuses(lineIds: String) {
        Napier.d(tag = TAG) { "getLineStatuses: $lineIds" }
        _uiState.value = TubeStatusUiState.Loading
        viewModelScope.launch(exceptionHandler) {
            val result = usecase(lineIds)
            _uiState.value =
                result.fold(
                    onSuccess = { responseList ->
                        val uiModels = responseList.toPresentableModel()
                        TubeStatusUiState.Success(
                            tubeLines = uiModels,
                        )
                    },
                    onFailure = { exception ->
                        Napier.d(exception) { "getLineStatuses: $exception" }
                        TubeStatusUiState.Error(
                            message = exception.toPresentableError(),
                        )
                    },
                )
        }
    }

    companion object {
        private const val TAG = "TubeStatusViewModel"
    }
}
