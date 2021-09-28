package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.verificationResponse.VerificationResponse
import io.reactivex.Single
import retrofit2.Response

interface VerificationRepository {
    fun verification(phoneNumber: String, code: String): Single<Response<VerificationResponse>>
    fun loadToken()
    fun getUserName():String
    fun signOut()

}