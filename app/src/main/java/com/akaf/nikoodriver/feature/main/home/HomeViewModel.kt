package com.akaf.nikoodriver.feature.main.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.akaf.nikoodriver.BuildConfig.VERSION_NAME
import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.common.NikoViewModel
import com.akaf.nikoodriver.common.SingleLiveEvent
import com.akaf.nikoodriver.data.responses.driverLocationResponse.DriverLocationResponse
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.akaf.nikoodriver.data.responses.mqttTripResponse.TripData
import com.akaf.nikoodriver.data.responses.refreshTokenResponse.RefreshTokenResponse
import com.akaf.nikoodriver.data.repositories.HomeRepository
import com.akaf.nikoodriver.data.responses.emptySeatsResponse.EmptySeatsResponse
import com.akaf.nikoodriver.data.responses.offerResponse.accept.AcceptOfferResponse
import com.akaf.nikoodriver.data.responses.offerResponse.reject.RejectOfferResponse
import com.akaf.nikoodriver.data.responses.profileResponse.ProfileData
import com.akaf.nikoodriver.data.responses.profileResponse.ProfileResponse
import com.akaf.nikoodriver.data.responses.updateResponse.UpdateData
import com.akaf.nikoodriver.data.responses.updateResponse.UpdateResponse
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import timber.log.Timber

class HomeViewModel(var mqttManager: HiveMqttManager,val homeRepository: HomeRepository,val sharedPreferences: SharedPreferences):NikoViewModel() {
    val refreshTokenLiveData = MutableLiveData<Response<RefreshTokenResponse>>()
    val mqttState = MutableLiveData<Boolean>()
    val tripCanceledLiveData = MutableLiveData<Boolean>()
    val tripPayedLiveData = MutableLiveData<Boolean>()
    val newOfferLiveData = MutableLiveData<TripData>()
    val profileLiveData=MutableLiveData<ProfileData?>()
    var versionAppLiveData = SingleLiveEvent<UpdateData>()
    val rejectTripLiveData=MutableLiveData<Response<RejectOfferResponse>>()
    val acceptTripLiveData=MutableLiveData<Response<AcceptOfferResponse>>()



    val username:String
    get() =sharedPreferences.getString("username","")?:""
    val totalTrips:String
    get()= sharedPreferences.getString("totalTrips","")?:""
    val totalDistance:String
    get()= sharedPreferences.getString("totalDistance","")?:""
    val totalTime:String
    get()=sharedPreferences.getString("totalTime","")?:""
    val rate:String
    get()=sharedPreferences.getString("rate","")?:""
    val income:String
    get()=sharedPreferences.getString("income","")?:""

    init {
//        update()
        retrieveOnlineStatus()
        subscribeMqttState()
        subscribeToNewOffers()
        subscribeToDisconnectSubject()
    }


    fun sendRefreshToken(token:String, refreshToken:String){
        homeRepository.refreshToken(token,refreshToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<RefreshTokenResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<RefreshTokenResponse>) {
                    refreshTokenLiveData.value=t
                    Timber.i("TOKENI REFRESH : "+t.body()?.data?.token)
                }
                override fun onError(e: Throwable) {
                    Timber.e(e)
                    progressBarLiveData.postValue(false)
                }

            })


    }


    fun update(){
        progressBarLiveData.value=true
        homeRepository.update("driver","android",VERSION_NAME)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<UpdateResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<UpdateResponse>) {
                    versionAppLiveData.value=t.body()?.data!!
                    progressBarLiveData.postValue(false)
                }
                override fun onError(e: Throwable) {
                    Timber.e(e)
                    progressBarLiveData.postValue(false)
                }

            })
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
                override fun onError(e: Throwable) {
                    Timber.e(e)
                    progressBarLiveData.postValue(false)
                }

            })
    }

    fun acceptTrip(tripId:Int,cost:Int){
        progressBarLiveData.value=true
        homeRepository.acceptTrip(tripId,cost)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikoSingleObserver<Response<AcceptOfferResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<AcceptOfferResponse>) {
                    progressBarLiveData.postValue(false)
                    acceptTripLiveData.value=t
                }
                override fun onError(e: Throwable) {
                    Timber.e(e)
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
                    rejectTripLiveData.value=t
                }
                override fun onError(e: Throwable) {
                    Timber.e(e)
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
        homeRepository.sendLocation(sendLocation)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<DriverLocationResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<DriverLocationResponse>) {
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                }


            })
    }



    fun clearSharedPreference(){
        homeRepository.clearSharedPreference()
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
                    val totalTrips=t.body()?.data?.totalTrips
                    val totalDistance=t.body()?.data?.totalDistance?.toDouble()?.toInt()
                    val totalTime=t.body()?.data?.totalTime
                    val rate=t.body()?.data?.rate
                    val income=t.body()?.data?.totalSalary?.toDouble()?.toInt()
                    sharedPreferences.edit().apply {
                        putString("totalTrips",totalTrips)
                        putString("totalDistance",totalDistance.toString())
                        putString("totalTime",totalTime)
                        putString("rate",rate.toString())
                        putString("income",income.toString())
                    }.apply()
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    progressBarLiveData.postValue(false)
                }

            })
    }

}