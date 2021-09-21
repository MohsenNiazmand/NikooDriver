package com.example.nikoodriver.feature.auth.verification

import com.example.nikoodriver.common.NikoViewModel
import com.example.nikoodriver.data.repositories.VerificationRepository
import com.example.nikoodriver.data.verificationResponse.VerificationResponse
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