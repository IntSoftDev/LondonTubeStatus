package com.intsoftdev.tflstatus.data.di

import com.intsoftdev.tflstatus.data.TFLServicesRepository
import com.intsoftdev.tflstatus.data.TFLServicesRepositoryImpl
import com.intsoftdev.tflstatus.network.di.networkModule
import org.koin.dsl.module

val dataModule = module {
    includes(networkModule)
    single<TFLServicesRepository> {
        TFLServicesRepositoryImpl(
            tflStatusAPI = get()
        )
    }
}