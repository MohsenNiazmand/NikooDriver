package com.example.nikodriver.data.repositories.sources.verification

import com.example.nikodriver.data.verificationResponse.VerificationResponse
import com.example.nikodriver.services.ApiService
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

    override fun loadToken() {
        TODO("Not yet implemented")
    }

    override fun saveToken(token: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

//    override fun saveUsername(username: String) {
//        TODO("Not yet implemented")
//    }

    override fun getUsername(): String {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }
}