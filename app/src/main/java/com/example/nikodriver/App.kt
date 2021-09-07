package com.example.nikodriver

import android.app.Application
import com.example.nikodriver.services.ApiService
import com.example.nikodriver.services.createApiServiceInstance
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val myModules= module {
            single { createApiServiceInstance() }

        }

        startKoin{
            androidContext(this@App)
            modules(myModules)
        }

    }

}