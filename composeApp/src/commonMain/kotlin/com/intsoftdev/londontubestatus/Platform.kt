package com.intsoftdev.londontubestatus

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform