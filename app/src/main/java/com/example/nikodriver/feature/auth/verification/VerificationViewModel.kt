package com.example.nikodriver.feature.auth.verification

import androidx.lifecycle.MutableLiveData
import com.example.nikodriver.common.NikoViewModel
import com.example.nikodriver.data.repositories.VerificationRepository
import com.example.nikodriver.data.verificationResponse.VerificationResponse
import io.reactivex.Single
import retrofit2.Response


class VerificationViewModel(private val verificationRepository: VerificationRepository) : NikoViewModel(){



    fun verification(phoneNumber: String, code: String): Single<Response<VerificationResponse>> {
        progressBarLiveData.value = true
        return verificationRepository.verification(phoneNumber, code).doFinally {
            progressBarLiveData.postValue(false)
        }
    }

}