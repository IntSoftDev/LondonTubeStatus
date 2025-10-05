package com.intsoftdev.tflstatus.data

import com.intsoftdev.tflstatus.model.exceptions.ApiException
import com.intsoftdev.tflstatus.model.tfl.TFLStatusResponseItem
import com.intsoftdev.tflstatus.network.TFLStatusAPI
import kotlinx.io.IOException

internal class TFLServicesRepositoryImpl(
    private val tflStatusAPI: TFLStatusAPI,
) : TFLServicesRepository {
    override suspend fun getLineStatuses(lineIds: String): Result<List<TFLStatusResponseItem>> {
        try {
            val result = tflStatusAPI.getLineStatuses(lineIds)
            return Result.success(result)
        } catch (e: IOException) {
            return Result.failure(ApiException.IOException(e.message))
        } catch (e: Exception) {
            return Result.failure(ApiException.HttpError(message = e.message))
        }
    }
}
