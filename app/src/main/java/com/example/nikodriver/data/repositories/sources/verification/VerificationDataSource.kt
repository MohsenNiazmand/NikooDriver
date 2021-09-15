package com.example.nikodriver.data.repositories.sources.verification

import com.example.nikodriver.data.verificationResponse.VerificationResponse
import io.reactivex.Single
import retrofit2.Response

interface VerificationDataSource {

    fun verification(phoneNumber: String, code: String): Single<Response<VerificationResponse>>
    fun loadToken()
    fun saveToken(token: String, refreshToken: String)
//    fun saveUsername(username: String)
    fun getUsername(): String
    fun signOut()

}