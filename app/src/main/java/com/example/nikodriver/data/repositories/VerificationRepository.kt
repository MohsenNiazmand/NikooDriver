package com.example.nikodriver.data.repositories

import com.example.nikodriver.data.verificationResponse.VerificationResponse
import io.reactivex.Single

interface VerificationRepository {
    fun verification(phoneNumber: String, code: String): Single<VerificationResponse>
    fun loadToken()
    fun getUserName():String
    fun signOut()

}