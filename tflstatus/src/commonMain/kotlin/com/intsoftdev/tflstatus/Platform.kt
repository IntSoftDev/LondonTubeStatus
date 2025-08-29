package com.intsoftdev.tflstatus

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform