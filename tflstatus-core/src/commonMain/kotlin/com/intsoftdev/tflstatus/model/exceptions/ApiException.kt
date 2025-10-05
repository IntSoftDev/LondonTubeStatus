package com.intsoftdev.tflstatus.model.exceptions

sealed class ApiException(message: String? = null) : Exception(message) {
    data class HttpError(val statusCode: Int? = null, override val message: String? = null) :
        ApiException(message)

    data class IOException(override val message: String? = null) : ApiException(message)
}
