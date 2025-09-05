package com.intsoftdev.tflstatus.di

import com.intsoftdev.tflstatus.domain.di.domainModule
import com.intsoftdev.tflstatus.presentation.TubeStatusViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf

val tflStatusDiModule = module {
    includes(domainModule)
    viewModelOf(::TubeStatusViewModel)
}