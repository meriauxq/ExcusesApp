package com.quentinmeriaux.excusesapp.di

import com.quentinmeriaux.excusesapp.data.remote.ExcusesApi
import com.quentinmeriaux.excusesapp.data.remote.repository.ExcusesRepository
import com.quentinmeriaux.excusesapp.ui.viewmodel.ExcusesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { provideApi() }

    single { ExcusesRepository(get()) }
    viewModel { ExcusesViewModel(get()) }
}

fun provideApi() = ExcusesApi.create()