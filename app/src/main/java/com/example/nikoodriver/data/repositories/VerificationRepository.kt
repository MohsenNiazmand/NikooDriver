package com.example.nikoodriver.data.repositories

import com.example.nikoodriver.data.verificationResponse.VerificationResponse
import io.reactivex.Single
import retrofit2.Response

interface VerificationRepository {
    fun verification(phoneNumber: String, code: String): Single<Response<VerificationResponse>>
    fun loadToken()
    fun getUserName():String
    fun signOut()

}