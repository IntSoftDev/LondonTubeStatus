package com.intsoftdev.tflstatus.domain.di

import com.intsoftdev.tflstatus.data.di.dataModule
import com.intsoftdev.tflstatus.domain.GetTFLStatusUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule =
    module {
        includes(dataModule)
        factoryOf(::GetTFLStatusUseCase)
    }
