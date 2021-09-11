package com.example.nikodriver.feature.auth.verification

import com.example.nikodriver.common.NikoViewModel
import com.example.nikodriver.data.repositories.VerificationRepository
import com.example.nikodriver.data.verificationResponse.VerificationResponse
import io.reactivex.Single


class VerificationViewModel(private val verificationRepository: VerificationRepository) : NikoViewModel(){


    fun verification(phoneNumber: String, code: String): Single<VerificationResponse> {
        progressBarLiveData.value = true
        return verificationRepository.verification(phoneNumber, code).doFinally {
            progressBarLiveData.postValue(false)
        }
    }

}