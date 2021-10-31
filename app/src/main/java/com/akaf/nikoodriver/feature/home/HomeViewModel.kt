package com.akaf.nikoodriver.feature.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.common.NikoViewModel
import com.akaf.nikoodriver.data.responses.driverLocationResponse.DriverLocationResponse
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.akaf.nikoodriver.data.responses.mqttTripResponse.TripData
import com.akaf.nikoodriver.data.responses.refreshTokenResponse.RefreshTokenResponse
import com.akaf.nikoodriver.data.repositories.HomeRepository
import com.akaf.nikoodriver.data.responses.UnAcceptedPassengers.UnAcceptedPassengersResponse
import com.akaf.nikoodriver.data.responses.emptySeatsResponse.EmptySeatsResponse
import com.akaf.nikoodriver.data.responses.offerResponse.accept.AcceptOfferResponse
import com.akaf.nikoodriver.data.responses.offerResponse.reject.RejectOfferResponse
import com.akaf.nikoodriver.data.responses.profileResponse.ProfileData
import com.akaf.nikoodriver.data.responses.profileResponse.ProfileResponse
import com.akaf.nikoodriver.services.createApiServiceInstance
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import timber.log.Timber
import java.util.*

class HomeViewModel(var mqttManager: HiveMqttManager,val homeRepository: HomeRepository,val sharedPreferences: SharedPreferences):NikoViewModel() {
    val mqttState = MutableLiveData<Boolean>()
    val tripCanceledLiveData = MutableLiveData<Boolean>()
    val tripPayedLiveData = MutableLiveData<Boolean>()
    val newOfferLiveData = MutableLiveData<TripData>()
    val refreshTokenLiveData = MutableLiveData<Response<RefreshTokenResponse>>()
    val profileLiveData=MutableLiveData<ProfileData?>()


    val username:String
    get() =sharedPreferences.getString("username","")?:""

    init {
        retrieveOnlineStatus()
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

    fun setEmptySeats(emptySeats:Int,isReady:Boolean){
        progressBarLiveData.value=true
        homeRepository.setEmptySeats(emptySeats,isReady)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikoSingleObserver<Response<EmptySeatsResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<EmptySeatsResponse>) {
                    progressBarLiveData.postValue(false)
                }

            })
    }


    fun getProfile(){
        progressBarLiveData.value=true
        homeRepository.getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<ProfileResponse>>(compositeDisposable){
                @SuppressLint("BinaryOperationInTimber")
                override fun onSuccess(t: Response<ProfileResponse>) {
                    profileLiveData.postValue(t.body()?.data)
                    progressBarLiveData.postValue(false)
                }

            })
    }

    fun decreaseSeatsCount(emptySeats: Int){
        sharedPreferences.edit().putInt("seatsCount",emptySeats-1).apply()
    }

    fun acceptTrip(tripId:Int,cost:Int){
        progressBarLiveData.value=true
        homeRepository.acceptTrip(tripId,cost)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikoSingleObserver<Response<AcceptOfferResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<AcceptOfferResponse>) {
                    progressBarLiveData.postValue(false)
                }

            })
    }

    fun rejectTrip(tripId:Int){
        progressBarLiveData.value=true
        homeRepository.rejectTrip(tripId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikoSingleObserver<Response<RejectOfferResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<RejectOfferResponse>) {
                    progressBarLiveData.postValue(false)
                }

            })
    }

    fun retrieveOnlineStatus(){
        val isOnline=sharedPreferences.getBoolean("isOnline",false)
        setOnlineStatus(isOnline)
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



    fun clearSharedPreference(){
        homeRepository.clearSharedPreference()
    }


     fun sendRefreshToken(token:String, refreshToken:String){
         progressBarLiveData.value=true
            homeRepository.refreshToken(token,refreshToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :NikoSingleObserver<Response<RefreshTokenResponse>>(compositeDisposable){
                    override fun onSuccess(t: Response<RefreshTokenResponse>) {
                            refreshTokenLiveData.postValue(t)
                           progressBarLiveData.postValue(false)
                        }


                })


    }


}