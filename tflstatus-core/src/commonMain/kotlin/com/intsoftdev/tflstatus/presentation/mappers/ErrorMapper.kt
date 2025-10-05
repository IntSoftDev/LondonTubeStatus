package com.intsoftdev.tflstatus.presentation.mappers

import com.intsoftdev.tflstatus.model.exceptions.ApiException

object ErrorMessages {
    const val NETWORK_ERROR = "Network error occurred. Please check your internet connection."
    const val HTTP_ERROR = "Error getting data from TFL services. Please try again later."
    const val UNEXPECTED_ERROR = "An unexpected error occurred. Please try again."
}

fun Throwable.toPresentableError(): String {
    return when (this) {
        is ApiException.IOException -> ErrorMessages.NETWORK_ERROR
        is ApiException.HttpError -> ErrorMessages.HTTP_ERROR
        else -> ErrorMessages.UNEXPECTED_ERROR
    }
}
