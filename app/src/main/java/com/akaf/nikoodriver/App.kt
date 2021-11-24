package com.akaf.nikoodriver

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.widget.Toast
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.akaf.nikoodriver.data.repositories.*
import com.akaf.nikoodriver.data.repositories.sources.CurrentTrips.CurrentTripsLocalDataSource
import com.akaf.nikoodriver.data.repositories.sources.CurrentTrips.CurrentTripsRemoteDataSource
import com.akaf.nikoodriver.data.repositories.sources.UnAcceptedPassengers.UnAcceptedPassengersLocalDataSource
import com.akaf.nikoodriver.data.repositories.sources.UnAcceptedPassengers.UnAcceptedPassengersRemoteDataSource
import com.akaf.nikoodriver.data.repositories.sources.fillInfo.FillInfoLocalDataSource
import com.akaf.nikoodriver.data.repositories.sources.fillInfo.FillInfoRemoteDataSource
import com.akaf.nikoodriver.data.repositories.sources.home.HomeLocalDataSource
import com.akaf.nikoodriver.data.repositories.sources.home.HomeRemoteDataSource
import com.akaf.nikoodriver.data.repositories.sources.login.LoginLocalDataSource
import com.akaf.nikoodriver.data.repositories.sources.login.LoginRemoteDataSource
import com.akaf.nikoodriver.data.repositories.sources.uploadDocs.UploadDocsLocalDataSource
import com.akaf.nikoodriver.data.repositories.sources.uploadDocs.UploadDocsRemoteDataSource
import com.akaf.nikoodriver.data.repositories.sources.verification.VerificationLocalDataSource
import com.akaf.nikoodriver.data.repositories.sources.verification.VerificationRemoteDataSource
import com.akaf.nikoodriver.feature.auth.registering.fillInfo.FillInfoViewModel
import com.akaf.nikoodriver.feature.auth.login.LoginActivity
import com.akaf.nikoodriver.feature.auth.login.LoginViewModel
import com.akaf.nikoodriver.feature.auth.registering.AuthViewModel
import com.akaf.nikoodriver.feature.auth.registering.upload_docs.UploadDocsViewModel
import com.akaf.nikoodriver.feature.auth.verification.VerificationViewModel
import com.akaf.nikoodriver.feature.current_trips.CurrentTripsViewModel
import com.akaf.nikoodriver.feature.unAccepted_passengers.UnAcceptedPassengersViewModel
import com.akaf.nikoodriver.feature.home.HomeViewModel
import com.akaf.nikoodriver.services.DriverForegroundService
import com.akaf.nikoodriver.services.createApiServiceInstance
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : MultiDexApplication() {
    companion object {
        lateinit var context: Context
        var activity: Activity? = null
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(baseContext)
        Timber.plant(Timber.DebugTree())
        Fresco.initialize(this)
        context = applicationContext



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel=NotificationChannel("NikooDriver","نیکو همراه",NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }




        //these are project dependencies
        val myModules= module {
            single { createApiServiceInstance(get()) }
            single { HiveMqttManager(androidContext()) }
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
            single<UploadDocsRepository> {
                UploadDocsRepositoryImpl(
                    UploadDocsLocalDataSource(),
                    UploadDocsRemoteDataSource(get())
                )
            }

            single<FillInfoRepository> {
                FillInfoRepositoryImpl(
                    FillInfoLocalDataSource(),
                    FillInfoRemoteDataSource(get())
                )
            }
            single { HomeLocalDataSource(get()) }
            single<HomeRepository> {
                HomeRepositoryImpl(
                    HomeLocalDataSource(get()),
                    HomeRemoteDataSource(get(),get())
                )
            }

            single<UnAcceptedPassengersRepository> {
                UnAcceptedPassengersRepositoryImpl(
                    UnAcceptedPassengersLocalDataSource(),
                    UnAcceptedPassengersRemoteDataSource(get())
                )
            }

            single<CurrentTripsRepository> {
                CurrentTripsRepositoryImpl(
                    CurrentTripsLocalDataSource(),
                    CurrentTripsRemoteDataSource(get())
                )
            }


            viewModel { LoginViewModel(get()) }
            viewModel { VerificationViewModel(get()) }
            viewModel { UploadDocsViewModel(get()) }
            viewModel { FillInfoViewModel(get()) }
            viewModel { HomeViewModel(get(),get(),get()) }
            viewModel { UnAcceptedPassengersViewModel(get()) }
            viewModel { CurrentTripsViewModel(get()) }
            viewModel { AuthViewModel() }

        }

            //does dependency injection
        startKoin{
            androidContext(this@App)
            modules(myModules)
        }

        val homeViewModel:HomeViewModel by inject()
        val sharedPreferences:SharedPreferences by inject()
        val token=sharedPreferences.getString("token", null)
        val refreshToken=sharedPreferences.getString("refresh_token", null)

        if (token!=null && refreshToken!=null){
            homeViewModel.sendRefreshToken(token,refreshToken)

            //checks token expire
            homeViewModel.refreshTokenLiveData.observeForever{
                if (it.code()==403){
                    homeViewModel.clearSharedPreference()
                    applicationContext.cacheDir.deleteRecursively()
                    activity?.finish()
                    DriverForegroundService.stopService(applicationContext)
                    val intent= Intent(this, LoginActivity::class.java).apply {
                        action = "com.package.ACTION_LOGOUT"
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }

                    startActivity(intent)


                    activity?.overridePendingTransition(0, 0);
                    Toast.makeText(applicationContext,"لطفا مجددا به حساب خود وارد شوید", Toast.LENGTH_SHORT).show()
                }
            }

            homeViewModel.update()


        }

    }


}