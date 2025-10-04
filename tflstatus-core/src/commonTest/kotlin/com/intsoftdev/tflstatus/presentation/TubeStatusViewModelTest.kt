package com.intsoftdev.tflstatus.presentation

import app.cash.turbine.test
import com.intsoftdev.tflstatus.data.TFLServicesRepository
import com.intsoftdev.tflstatus.domain.GetTFLStatusUseCase
import com.intsoftdev.tflstatus.model.LineStatuse
import com.intsoftdev.tflstatus.model.TFLStatusResponseItem
import com.intsoftdev.tflstatus.presentation.model.TubeLineStatusSeverity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TubeStatusViewModelTest {
    @Test
    fun `initial state should be Loading`() =
        runTest {
            // Arrange
            val fakeRepository = FakeTFLServicesRepository()
            val useCase = GetTFLStatusUseCase(fakeRepository)
            val viewModel = TubeStatusViewModel(useCase)

            // Assert
            viewModel.uiState.test {
                assertEquals(TubeStatusUiState.Loading, awaitItem())
            }
        }

    @Test
    fun `getLineStatuses with successful response emits Success state`() =
        runTest {
            // Arrange
            val successResponse =
                listOf(
                    createTFLStatusResponseItem(
                        id = "central",
                        name = "Central",
                        statusSeverity = 10,
                        statusDescription = "Good Service",
                    ),
                )
            val fakeRepository = FakeTFLServicesRepository(successResult = successResponse)
            val useCase = GetTFLStatusUseCase(fakeRepository)
            val viewModel = TubeStatusViewModel(useCase)

            // Act
            viewModel.getLineStatuses("central")
            advanceUntilIdle()

            // Assert
            viewModel.uiState.test {
                assertEquals(TubeStatusUiState.Loading, awaitItem())
                val successState = awaitItem()
                assertTrue(successState is TubeStatusUiState.Success)
                assertEquals(1, successState.tubeLines.size)
                assertEquals("central", successState.tubeLines[0].id)
                assertEquals("Central", successState.tubeLines[0].displayName)
                assertEquals("Good Service", successState.tubeLines[0].statusText)
                assertEquals(
                    TubeLineStatusSeverity.GOOD_SERVICE,
                    successState.tubeLines[0].statusSeverity,
                )
            }
        }

    @Test
    fun `getLineStatuses with failure emits Error state`() =
        runTest {
            // Arrange
            val exception = Exception("Network error")
            val fakeRepository = FakeTFLServicesRepository(failureException = exception)
            val useCase = GetTFLStatusUseCase(fakeRepository)
            val viewModel = TubeStatusViewModel(useCase)

            // Act
            viewModel.getLineStatuses("central")
            advanceUntilIdle()

            // Assert
            viewModel.uiState.test {
                val initial = awaitItem()
                assertTrue(initial is TubeStatusUiState.Loading)
                val errorState = awaitItem()
                assertTrue(errorState is TubeStatusUiState.Error)
                assertEquals("Unable to load tube status. Please try again.", errorState.message)
            }
        }

    @Test
    fun `getLineStatuses with timeout exception shows timeout error message`() =
        runTest {
            // Arrange
            val exception = Exception("SocketTimeoutException: timeout")
            val fakeRepository = FakeTFLServicesRepository(failureException = exception)
            val useCase = GetTFLStatusUseCase(fakeRepository)
            val viewModel = TubeStatusViewModel(useCase)

            // Act
            viewModel.getLineStatuses("central")
            advanceUntilIdle()

            // Assert
            viewModel.uiState.test {
                val initial = awaitItem()
                assertTrue(initial is TubeStatusUiState.Loading)
                val errorState = awaitItem()
                assertTrue(errorState is TubeStatusUiState.Error)
                assertEquals(
                    "Connection timeout. Please check your internet connection and try again.",
                    errorState.message,
                )
            }
        }

    private fun createTFLStatusResponseItem(
        id: String,
        name: String,
        statusSeverity: Int,
        statusDescription: String,
        reason: String = "",
    ): TFLStatusResponseItem {
        return TFLStatusResponseItem(
            id = id,
            name = name,
            lineStatuses =
                listOf(
                    LineStatuse(
                        statusSeverity = statusSeverity,
                        statusSeverityDescription = statusDescription,
                        reason = reason,
                    ),
                ),
        )
    }

    private class FakeTFLServicesRepository(
        var successResult: List<TFLStatusResponseItem>? = null,
        var failureException: Exception? = null,
    ) : TFLServicesRepository {
        override suspend fun getLineStatuses(lineIds: String): List<TFLStatusResponseItem> {
            failureException?.let { throw it }
            return successResult ?: emptyList()
        }
    }
}
