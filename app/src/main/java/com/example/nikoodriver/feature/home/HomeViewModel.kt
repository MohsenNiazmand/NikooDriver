package com.example.nikoodriver.feature.home

import androidx.lifecycle.MutableLiveData
import com.example.nikoodriver.common.NikoViewModel
import com.example.nikoodriver.services.ApiService
import com.example.nikoodriver.services.mqtt.HiveMqttManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel(var mqttManager: HiveMqttManager,var apiService: ApiService):NikoViewModel() {
    val mqttState = MutableLiveData<Boolean>()
    val tripCanceledLiveData = MutableLiveData<Boolean>()
    val tripPayedLiveData = MutableLiveData<Boolean>()

    init {
        subscribeMqttState()
        subscribeToNewOffers()
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

    private fun subscribeToNewOffers() {
//        compositeDisposable.add(mqttManager.newTripSubject
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                newOfferLiveData.postValue(it)
//            }
//        )
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



}