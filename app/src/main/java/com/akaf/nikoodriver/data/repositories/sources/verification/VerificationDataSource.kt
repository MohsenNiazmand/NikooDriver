package com.akaf.nikoodriver.data.repositories.sources.verification

import com.akaf.nikoodriver.data.verificationResponse.VerificationResponse
import io.reactivex.Single
import retrofit2.Response

interface VerificationDataSource {

    fun verification(phoneNumber: String, code: String): Single<Response<VerificationResponse>>
    fun loadToken()
    fun saveToken(token: String, refreshToken: String)
    fun saveDriverId(driverId: String)
    fun getUsername(): String
    fun signOut()

}