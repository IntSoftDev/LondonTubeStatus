package com.intsoftdev.tflstatus.data

import com.intsoftdev.tflstatus.model.tfl.TFLStatusResponseItem

interface TFLServicesRepository {
    suspend fun getLineStatuses(lineIds: String): Result<List<TFLStatusResponseItem>>
}
