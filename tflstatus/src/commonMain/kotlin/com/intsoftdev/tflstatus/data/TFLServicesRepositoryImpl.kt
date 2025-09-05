package com.intsoftdev.tflstatus.data

import com.intsoftdev.tflstatus.model.TFLStatusResponseItem
import com.intsoftdev.tflstatus.network.TFLStatusAPI

internal class TFLServicesRepositoryImpl(
    private val tflStatusAPI: TFLStatusAPI
): TFLServicesRepository {
    override suspend fun getLineStatuses(lineIds: String): List<TFLStatusResponseItem> {
        return tflStatusAPI.getLineStatuses(lineIds)
    }
}