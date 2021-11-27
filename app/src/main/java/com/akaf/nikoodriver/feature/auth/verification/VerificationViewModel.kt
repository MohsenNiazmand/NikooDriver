package com.akaf.nikoodriver.feature.auth.verification

import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.common.NikoViewModel
import com.akaf.nikoodriver.data.responses.fcmResponse.FcmResponse
import com.akaf.nikoodriver.data.repositories.VerificationRepository
import com.akaf.nikoodriver.data.responses.verificationResponse.VerificationResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import timber.log.Timber


class VerificationViewModel(private val verificationRepository: VerificationRepository) : NikoViewModel(){



    fun verification(phoneNumber: String, code: String): Single<Response<VerificationResponse>> {
        progressBarLiveData.value = true
        return verificationRepository.verification(phoneNumber, code).doFinally {
            progressBarLiveData.postValue(false)
        }
    }

    fun sendFcmToken(fcmToken:String?){
        progressBarLiveData.value=true
        if (fcmToken != null) {
            verificationRepository.sendFcmToken(fcmToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NikoSingleObserver<Response<FcmResponse>>(compositeDisposable){
                    override fun onSuccess(t: Response<FcmResponse>) {
                        progressBarLiveData.postValue(false)
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e)
                        progressBarLiveData.postValue(false)

                    }

                })
        }

    }

}