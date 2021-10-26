package com.akaf.nikoodriver.feature.declined_passengers

import androidx.lifecycle.MutableLiveData
import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.common.NikoViewModel
import com.akaf.nikoodriver.data.repositories.UnAcceptedPassengersRepository
import com.akaf.nikoodriver.data.responses.UnAcceptedPassengers.UnAcceptedPassengersResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import timber.log.Timber

class UnAcceptedPassengersViewModel(val unAcceptedPassengersRepository: UnAcceptedPassengersRepository) : NikoViewModel() {

    var unAcceptedPassengersResponse = MutableLiveData<UnAcceptedPassengersResponse>()

    init {
        unAcceptedPassengers()
    }


    fun unAcceptedPassengers() {
        progressBarLiveData.value=true
        unAcceptedPassengersRepository.unAcceptedPassengers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikoSingleObserver<Response<UnAcceptedPassengersResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<UnAcceptedPassengersResponse>) {
                    unAcceptedPassengersResponse.postValue(t.body())
                    progressBarLiveData.postValue(false)
                    Timber.i("currentTripLiveData"+t.body().toString())

                }

            })
    }

}