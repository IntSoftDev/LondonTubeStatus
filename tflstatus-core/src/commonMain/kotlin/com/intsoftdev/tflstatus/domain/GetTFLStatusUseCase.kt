package com.intsoftdev.tflstatus.domain

import com.intsoftdev.tflstatus.data.TFLServicesRepository
import com.intsoftdev.tflstatus.model.tfl.TFLStatusResponseItem

class GetTFLStatusUseCase(
    private val tflServicesRepository: TFLServicesRepository,
) {
    suspend operator fun invoke(lines: String): Result<List<TFLStatusResponseItem>> {
        return tflServicesRepository.getLineStatuses(lines)
    }
}
