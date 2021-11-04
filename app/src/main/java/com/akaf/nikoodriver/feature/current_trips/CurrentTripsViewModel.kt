package com.akaf.nikoodriver.feature.current_trips

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.akaf.nikoodriver.App
import com.akaf.nikoodriver.common.NikoCompletableObserver
import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.common.NikoViewModel
import com.akaf.nikoodriver.data.repositories.CurrentTripsRepository
import com.akaf.nikoodriver.data.responses.completeTripResponse.CompleteTripResponse
import com.akaf.nikoodriver.data.responses.currentTripsResponse.CurrentTripsResponse
import com.akaf.nikoodriver.data.responses.dropOfResponse.DropOfResponse
import com.akaf.nikoodriver.data.responses.pickUpResponse.PickUpResponse
import com.akaf.nikoodriver.data.responses.startTripResponse.StartTripResponse
import com.google.gson.JsonObject
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

class CurrentTripsViewModel(val currentTripsRepository: CurrentTripsRepository) : NikoViewModel() {

    var currentTripsLiveData=MutableLiveData<CurrentTripsResponse>()
    val dropOfLiveData= MutableLiveData<Response<DropOfResponse>>()
    val startTripLiveData=MutableLiveData<Response<StartTripResponse>>()
    val pickupTripLiveData=MutableLiveData<Response<PickUpResponse>>()

    init {
        currentTrips()
    }

    fun currentTrips(){
        progressBarLiveData.value=true
        currentTripsRepository.currentTrips()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<CurrentTripsResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<CurrentTripsResponse>) {
                    currentTripsLiveData.postValue(t.body())
                    progressBarLiveData.postValue(false)

                }

            })
    }

    fun startTrip(tripId:Int){
        progressBarLiveData.value=true
        currentTripsRepository.startTrip(tripId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<StartTripResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<StartTripResponse>) {
                    progressBarLiveData.postValue(false)
                    startTripLiveData.value=t

                }

            })
    }

    fun pickUp(tripId: Int, sourceId: Int,location0:Double,location1:Double){
        progressBarLiveData.value=true
        currentTripsRepository.pickUp(tripId,sourceId,location0,location1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<PickUpResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<PickUpResponse>) {
                    progressBarLiveData.postValue(false)
                    pickupTripLiveData.value=t

                }

            })
    }

    fun dropOf(tripId: Int, sourceId: Int,location0:Double,location1:Double){
        progressBarLiveData.value=true
        currentTripsRepository.dropOf(tripId,sourceId,location0,location1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<DropOfResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<DropOfResponse>) {
                    progressBarLiveData.postValue(false)
                    dropOfLiveData.value=t

                }

            })
    }

    fun completeTrip(tripId:Int){
        progressBarLiveData.value=true
        currentTripsRepository.completeTrip(tripId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<CompleteTripResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<CompleteTripResponse>) {
                    progressBarLiveData.postValue(false)

                }

            })
    }

    fun cancelTrip(tripId: Int){
        progressBarLiveData.value=true
        currentTripsRepository.cancelTrip(tripId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    progressBarLiveData.postValue(false)
                    currentTrips()

                }

                override fun onError(e: Throwable) {
                    progressBarLiveData.postValue(false)
                }

            })

    }



}