package com.akaf.nikoodriver.feature.main.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.multidex.BuildConfig
import androidx.multidex.BuildConfig.VERSION_NAME
import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.common.NikoViewModel
import com.akaf.nikoodriver.common.SingleLiveEvent
import com.akaf.nikoodriver.data.responses.driverLocationResponse.DriverLocationResponse
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.akaf.nikoodriver.data.responses.mqttTripResponse.TripData
import com.akaf.nikoodriver.data.repositories.HomeRepository
import com.akaf.nikoodriver.data.responses.emptySeatsResponse.EmptySeatsResponse
import com.akaf.nikoodriver.data.responses.offerResponse.accept.AcceptOfferResponse
import com.akaf.nikoodriver.data.responses.offerResponse.reject.RejectOfferResponse
import com.akaf.nikoodriver.data.responses.profileResponse.ProfileResponse
import com.akaf.nikoodriver.data.responses.updateResponse.UpdateData
import com.akaf.nikoodriver.data.responses.updateResponse.UpdateResponse
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.component.KoinApiExtension
import retrofit2.Response
import timber.log.Timber
import android.content.pm.PackageInfo
import android.widget.Toast
import com.akaf.nikoodriver.App


@KoinApiExtension
class HomeViewModel(var mqttManager: HiveMqttManager, private val homeRepository: HomeRepository, val sharedPreferences: SharedPreferences):NikoViewModel() {
    val mqttState = MutableLiveData<Boolean>()
    private val tripCanceledLiveData = MutableLiveData<Boolean>()
    private val tripPayedLiveData = MutableLiveData<Boolean>()
    val newOfferLiveData = MutableLiveData<TripData>()
    val profileLiveData=MutableLiveData<Response<ProfileResponse>>()
    var versionAppLiveData = SingleLiveEvent<UpdateData>()
    val rejectTripLiveData=MutableLiveData<Response<RejectOfferResponse>>()
    val acceptTripLiveData=MutableLiveData<Response<AcceptOfferResponse>>()



    val token=sharedPreferences.getString("token","")
    val refreshToken=sharedPreferences.getString("refresh_token","")

    val username:String
    get() =sharedPreferences.getString("username","")?:""
    val maxCapacity:String
    get() =sharedPreferences.getString("maxCapacity","")?:""
    val credit:String
    get() =sharedPreferences.getString("credit","")?:""
    val emptySeats:String
    get() =sharedPreferences.getString("emptySeats","")?:""
    val currentTrips:String
    get() =sharedPreferences.getString("currentTrips","")?:""
    val unAcceptedPassengers:String
    get() =sharedPreferences.getString("unAcceptedPassengers","")?:""
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
        retrieveOnlineStatus()
        subscribeMqttState()
        subscribeToNewOffers()
        subscribeToDisconnectSubject()
    }



    fun update(){
        val pInfo: PackageInfo =
            App.context.getPackageManager().getPackageInfo(App.context.packageName, 0)
        val version = pInfo.versionName
        homeRepository.update("driver","android",version)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<UpdateResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<UpdateResponse>) {
                    versionAppLiveData.value=t.body()?.data!!
                }
                override fun onError(e: Throwable) {
                    Timber.e(e)
                }

            })
    }


    @KoinApiExtension
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

    @KoinApiExtension
    @SuppressLint("CheckResult")
    private fun subscribeToDisconnectSubject() {
        mqttManager.disconnectSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                homeRepository.onlineStatus(it)
            }

    }

    @KoinApiExtension
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


    @KoinApiExtension
    fun sendDriverLocation(location: Location) {
        mqttManager.publishDriverLocation(location)
    }

    fun setEmptySeats(emptySeats:Int,isReady:Boolean){
        homeProgressBarLiveData.value=true
        homeRepository.setEmptySeats(emptySeats,isReady)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikoSingleObserver<Response<EmptySeatsResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<EmptySeatsResponse>) {
                    homeProgressBarLiveData.postValue(false)
                }
                override fun onError(e: Throwable) {
                    Timber.e(e)
                    homeProgressBarLiveData.postValue(false)
                    Toast.makeText(App.context,"خطای ارتباط با سرور",Toast.LENGTH_SHORT).show()

                }

            })
    }

    fun acceptTrip(tripId:Int,cost:Int){
        homeProgressBarLiveData.value=true
        homeRepository.acceptTrip(tripId,cost)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikoSingleObserver<Response<AcceptOfferResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<AcceptOfferResponse>) {
                    homeProgressBarLiveData.postValue(false)
                    acceptTripLiveData.value=t
                }
                override fun onError(e: Throwable) {
                    Timber.e(e)
                    homeProgressBarLiveData.postValue(false)
                }

            })
    }

    fun rejectTrip(tripId:Int){
        homeProgressBarLiveData.value=true
        homeRepository.rejectTrip(tripId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikoSingleObserver<Response<RejectOfferResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<RejectOfferResponse>) {
                    homeProgressBarLiveData.postValue(false)
                    rejectTripLiveData.value=t
                }
                override fun onError(e: Throwable) {
                    Timber.e(e)
                    homeProgressBarLiveData.postValue(false)
                }

            })
    }

    @KoinApiExtension
    fun retrieveOnlineStatus(){
        val isOnline=sharedPreferences.getBoolean("isOnline",false)
        setOnlineStatus(isOnline)
    }

    @KoinApiExtension
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







    @KoinApiExtension
    fun getProfile(){
        homeProgressBarLiveData.value=true
        homeRepository.getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<ProfileResponse>>(compositeDisposable){
                @SuppressLint("BinaryOperationInTimber")
                override fun onSuccess(t: Response<ProfileResponse>) {
                    homeProgressBarLiveData.postValue(false)

                    profileLiveData.value=t


                    if (t.code()==200){
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


                }

                override fun onError(e: Throwable) {
                    homeProgressBarLiveData.postValue(false)
                    Timber.e(e)
                }

            })
    }

}