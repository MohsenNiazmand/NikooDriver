package com.example.nikoodriver.data.repositories.sources.home

import com.example.nikoodriver.data.fcmResponse.FcmResponse
import com.example.nikoodriver.services.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response

class HomeRemoteDataSource(val apiService: ApiService):HomeDataSource {
    override fun sendFcmToken(fcmToken: String): Single<Response<FcmResponse>> {
        return apiService.sendFcmToken(JsonObject().apply {
            addProperty("fcmToken",fcmToken)
        })
    }
}