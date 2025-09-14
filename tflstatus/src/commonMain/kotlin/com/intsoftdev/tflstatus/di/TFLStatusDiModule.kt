package com.intsoftdev.tflstatus.di

import com.intsoftdev.tflstatus.domain.di.domainModule
import com.intsoftdev.tflstatus.presentation.TubeStatusViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val tflStatusDiModule =
    module {
        includes(domainModule)
        viewModelOf(::TubeStatusViewModel)
    }
