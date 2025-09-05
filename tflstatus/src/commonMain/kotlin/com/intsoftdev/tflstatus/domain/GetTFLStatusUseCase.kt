package com.intsoftdev.tflstatus.domain

import com.intsoftdev.tflstatus.data.TFLServicesRepository
import com.intsoftdev.tflstatus.model.TFLStatusResponseItem

class GetTFLStatusUseCase(
    private val tflServicesRepository: TFLServicesRepository
) {
    suspend operator fun invoke(lines: String): Result<List<TFLStatusResponseItem>> {
        return try {
            val status = tflServicesRepository.getLineStatuses(lines)
            Result.success(status)
        } catch (e: Exception) {
            println("Error fetching TFL status: ${e.message}")
            Result.failure(e)
        }
    }
}
