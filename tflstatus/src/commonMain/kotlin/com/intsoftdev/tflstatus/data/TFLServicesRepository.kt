package com.intsoftdev.tflstatus.data

import com.intsoftdev.tflstatus.model.TFLStatusResponseItem

interface TFLServicesRepository {
    suspend fun getLineStatuses(lineIds: String): List<TFLStatusResponseItem>
}
