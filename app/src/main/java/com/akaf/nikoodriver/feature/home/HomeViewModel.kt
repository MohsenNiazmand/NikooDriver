package com.akaf.nikoodriver.feature.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.common.NikoViewModel
import com.akaf.nikoodriver.data.TokenContainer
import com.akaf.nikoodriver.data.TokenContainer.refreshToken
import com.akaf.nikoodriver.data.TokenContainer.token
import com.akaf.nikoodriver.data.driverLocationResponse.DriverLocationResponse
import com.akaf.nikoodriver.data.location.SendLocation
import com.akaf.nikoodriver.data.offer.TripData
import com.akaf.nikoodriver.data.refreshTokenResponse.RefreshTokenResponse
import com.akaf.nikoodriver.data.repositories.HomeRepository
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import timber.log.Timber

class HomeViewModel(var mqttManager: HiveMqttManager,val homeRepository: HomeRepository,val sharedPreferences: SharedPreferences):NikoViewModel() {
    val mqttState = MutableLiveData<Boolean>()
    val tripCanceledLiveData = MutableLiveData<Boolean>()
    val tripPayedLiveData = MutableLiveData<Boolean>()
    var currentTripLiveData = MutableLiveData<String>()
    val newOfferLiveData = MutableLiveData<TripData>()



    init {
        subscribeMqttState()
        subscribeToNewOffers()
        subscribeToDisconnectSubject()
    }




    private fun subscribeMqttState() {
        compositeDisposable.add(
            HiveMqttManager.mqttConnectionState.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it) {
                        HiveMqttManager.CONNECTED -> {
                            mqttState.postValue(true)
                        }
                        HiveMqttManager.CONNECTION_FAILURE -> {
                            mqttState.postValue(false)

                        }
                        else -> {
                            mqttState.postValue(false)

                        }
                    }
                })


    }

    @SuppressLint("CheckResult")
    private fun subscribeToDisconnectSubject() {
        mqttManager.disconnectSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                homeRepository.onlineStatus(it)
            }

    }

    private fun subscribeToNewOffers() {
        compositeDisposable.add(mqttManager.newTripSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                newOfferLiveData.postValue(it)
            }
        )
        compositeDisposable.add(mqttManager.canceledTripSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                tripCanceledLiveData.postValue(true)
            }
        )

        compositeDisposable.add(mqttManager.payedTripSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                tripPayedLiveData.postValue(true)
            }
        )
    }


    fun sendDriverLocation(location: Location) {
        mqttManager.publishDriverLocation(location)
    }

    fun setOnlineStatus(isOnline:Boolean){
        homeRepository.onlineStatus(isOnline)
        when {
            isOnline -> mqttManager.connect()
            else -> mqttManager.disconnect()
        }
    }



    fun sendDriverLocationToRest(sendLocation: SendLocation) {
        progressBarLiveData.value=true
        homeRepository.sendLocation(sendLocation)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<DriverLocationResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<DriverLocationResponse>) {
                    progressBarLiveData.postValue(false)
                }


            })
    }


    fun getCurrentTrip() {
      //needs current trip rest
    }



     fun sendRefreshToken(token:String, refreshToken:String){
        progressBarLiveData.value=true
            homeRepository.refreshToken(token,refreshToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :NikoSingleObserver<Response<RefreshTokenResponse>>(compositeDisposable){
                    override fun onSuccess(t: Response<RefreshTokenResponse>) {
                        Timber.i("refresh token"+t.body().toString())
                        if (t.isSuccessful){
                            progressBarLiveData.postValue(false)
                            homeRepository.saveTokenStatus(false)

                        }else{
                            progressBarLiveData.postValue(false)
                            homeRepository.saveTokenStatus(true)
                        }
                    }

                })


    }


}