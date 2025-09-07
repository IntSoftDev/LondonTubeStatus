package com.intsoftdev.tflstatus.domain

import com.intsoftdev.tflstatus.data.TFLServicesRepository
import com.intsoftdev.tflstatus.model.TFLStatusResponseItem
import io.github.aakira.napier.Napier

class GetTFLStatusUseCase(
    private val tflServicesRepository: TFLServicesRepository
) {
    suspend operator fun invoke(lines: String): Result<List<TFLStatusResponseItem>> {
        return try {
            Napier.d("Fetching TFL status")
            val status = tflServicesRepository.getLineStatuses(lines)
            Napier.d("Fetched status")
            Result.success(status)
        } catch (e: Exception) {
            Napier.e("Error fetching TFL status", e)
            Result.failure(e)
        }
    }
}
