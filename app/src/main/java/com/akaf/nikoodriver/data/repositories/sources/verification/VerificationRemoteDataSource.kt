package com.akaf.nikoodriver.data.repositories.sources.verification

import com.akaf.nikoodriver.data.responses.fcmResponse.FcmResponse
import com.akaf.nikoodriver.data.responses.verificationResponse.VerificationResponse
import com.akaf.nikoodriver.services.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response

class VerificationRemoteDataSource(val apiService: ApiService) : VerificationDataSource {
    override fun verification(phoneNumber: String, code: String): Single<Response<VerificationResponse>> {
        return apiService.verification(JsonObject().apply {
            addProperty("phoneNumber",phoneNumber)
            addProperty("code",code)
        })
    }

    override fun sendFcmToken(fcmToken: String): Single<Response<FcmResponse>> {
        return apiService.sendFcmToken(JsonObject().apply {
            addProperty("fcmToken",fcmToken)
        })
    }

    override fun loadToken() {
        TODO("Not yet implemented")
    }

    override fun saveToken(token: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun saveDriverId(driverId: String) {
        TODO("Not yet implemented")
    }


    override fun getUsername(): String {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }
}