package com.example.nikodriver

import android.app.Application
import com.example.nikodriver.data.repositories.LoginRepository
import com.example.nikodriver.data.repositories.LoginRepositoryImpl
import com.example.nikodriver.data.repositories.sources.LoginLocalDataSource
import com.example.nikodriver.data.repositories.sources.LoginRemoteDataSource
import com.example.nikodriver.feature.login.LoginViewModel
import com.example.nikodriver.services.ApiService
import com.example.nikodriver.services.createApiServiceInstance
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        val myModules= module {
            single { createApiServiceInstance() }

            single { LoginLocalDataSource() }
            single<LoginRepository> {
                LoginRepositoryImpl(
                    LoginRemoteDataSource(get()),
                    LoginLocalDataSource()
                )
            }


            viewModel { LoginViewModel(get()) }

        }

        startKoin{
            androidContext(this@App)
            modules(myModules)
        }


    }

}