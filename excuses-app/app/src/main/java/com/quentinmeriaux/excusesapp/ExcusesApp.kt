package com.quentinmeriaux.excusesapp

import android.app.Application
import com.quentinmeriaux.excusesapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ExcusesApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@ExcusesApp)
            // Load modules
            modules(appModule)
        }
    }
}