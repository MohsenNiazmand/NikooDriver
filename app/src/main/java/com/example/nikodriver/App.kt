package com.example.nikodriver

import android.app.Application
import android.content.SharedPreferences
import com.example.nikodriver.data.repositories.LoginRepository
import com.example.nikodriver.data.repositories.LoginRepositoryImpl
import com.example.nikodriver.data.repositories.VerificationRepository
import com.example.nikodriver.data.repositories.VerificationRepositoryImpl
import com.example.nikodriver.data.repositories.sources.login.LoginLocalDataSource
import com.example.nikodriver.data.repositories.sources.login.LoginRemoteDataSource
import com.example.nikodriver.data.repositories.sources.verification.VerificationLocalDataSource
import com.example.nikodriver.data.repositories.sources.verification.VerificationRemoteDataSource
import com.example.nikodriver.feature.auth.login.LoginViewModel
import com.example.nikodriver.feature.auth.verification.VerificationViewModel
import com.example.nikodriver.services.createApiServiceInstance
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Fresco.initialize(this)

        //these are project dependencies
        val myModules= module {
            single { createApiServiceInstance() }

            single<SharedPreferences> {
                this@App.getSharedPreferences(
                    "app_settings",
                    MODE_PRIVATE
                )
            }

            single { LoginLocalDataSource() }
            single<LoginRepository> {
                LoginRepositoryImpl(
                    LoginRemoteDataSource(get()),
                    LoginLocalDataSource()
                )
            }

            single { VerificationLocalDataSource(get()) }
            single<VerificationRepository> {
                VerificationRepositoryImpl(
                    VerificationLocalDataSource(get()),
                    VerificationRemoteDataSource(get())
                )

            }


            viewModel { LoginViewModel(get()) }
            viewModel { VerificationViewModel(get()) }

        }

            //does dependency injection
        startKoin{
            androidContext(this@App)
            modules(myModules)
        }


    }

}