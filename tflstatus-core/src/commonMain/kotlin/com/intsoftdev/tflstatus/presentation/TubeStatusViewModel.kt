package com.intsoftdev.tflstatus.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intsoftdev.tflstatus.domain.GetTFLStatusUseCase
import com.intsoftdev.tflstatus.presentation.mapper.toUiModels
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

    private fun getUserFriendlyErrorMessage(throwable: Throwable): String {
        return when {
            throwable.message?.contains("SocketTimeoutException", ignoreCase = true) == true ||
                throwable.message?.contains("timeout", ignoreCase = true) == true -> {
                "Connection timeout. Please check your internet connection and try again."
            }

            throwable.message?.contains("UnknownHostException", ignoreCase = true) == true ||
                throwable.message?.contains(
                    "No address associated",
                    ignoreCase = true,
                ) == true -> {
                "Unable to connect to TFL services. Please check your internet connection."
            }

            throwable.message?.contains("ConnectException", ignoreCase = true) == true -> {
                "Connection failed. Please try again later."
            }

            throwable.message?.contains("HttpException", ignoreCase = true) == true ||
                throwable.message?.contains("HTTP", ignoreCase = true) == true -> {
                "TFL services are currently unavailable. Please try again later."
            }

            else -> "Unable to load tube status. Please try again."
        }
    }

    private val exceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Napier.e(tag = TAG) { "exception handler $throwable" }
            viewModelScope.launch {
                _uiState.value =
                    TubeStatusUiState.Error(message = getUserFriendlyErrorMessage(throwable))
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
                        val uiModels = responseList.toUiModels()
                        TubeStatusUiState.Success(
                            tubeLines = uiModels,
                            // add lastUpdated text if needed to surface in UI
                            hasDisruptions = uiModels.any { it.hasDisruption },
                        )
                    },
                    onFailure = { err ->
                        TubeStatusUiState.Error(
                            message =
                                getUserFriendlyErrorMessage(
                                    err,
                                ),
                        )
                    },
                )
        }
    }

    companion object {
        private const val TAG = "TubeStatusViewModel"
    }
}
