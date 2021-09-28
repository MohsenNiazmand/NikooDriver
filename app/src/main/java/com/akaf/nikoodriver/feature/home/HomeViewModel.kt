package com.akaf.nikoodriver.feature.home

import androidx.lifecycle.MutableLiveData
import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.common.NikoViewModel
import com.akaf.nikoodriver.data.fcmResponse.FcmResponse
import com.akaf.nikoodriver.data.repositories.HomeRepository
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class HomeViewModel(var mqttManager: HiveMqttManager,val homeRepository: HomeRepository):NikoViewModel() {
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

    fun sendFcmToken(fcmToken:String?){
        progressBarLiveData.value=true
        if (fcmToken != null) {
            homeRepository.sendFcmToken(fcmToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :NikoSingleObserver<Response<FcmResponse>>(compositeDisposable){
                    override fun onSuccess(t: Response<FcmResponse>) {
                        progressBarLiveData.postValue(false)
                    }

                })
        }

    }


}