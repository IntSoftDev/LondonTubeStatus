package com.intsoftdev.londontubestatus

import android.app.Application
import com.intsoftdev.tflstatus.di.tflStatusDiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TFLStatusApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(tflStatusDiModule)
        }
    }
}