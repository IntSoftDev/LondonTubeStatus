package com.intsoftdev.tflstatus.presentation.mappers

fun Throwable.toPresentableError(): String {
    return when {
        message?.contains("SocketTimeoutException", ignoreCase = true) == true ||
            message?.contains("timeout", ignoreCase = true) == true -> {
            "Connection timeout. Please check your internet connection and try again."
        }

        message?.contains("UnknownHostException", ignoreCase = true) == true ||
            message?.contains(
                "No address associated",
                ignoreCase = true,
            ) == true -> {
            "Unable to connect to TFL services. Please check your internet connection."
        }

        message?.contains("ConnectException", ignoreCase = true) == true -> {
            "Connection failed. Please try again later."
        }

        message?.contains("HttpException", ignoreCase = true) == true ||
            message?.contains("HTTP", ignoreCase = true) == true -> {
            "TFL services are currently unavailable. Please try again later."
        }

        else -> "Unable to load tube status. Please try again."
    }
}
