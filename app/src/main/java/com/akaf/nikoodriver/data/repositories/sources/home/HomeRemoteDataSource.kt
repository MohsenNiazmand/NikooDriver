package com.akaf.nikoodriver.data.repositories.sources.home

import com.akaf.nikoodriver.data.fcmResponse.FcmResponse
import com.akaf.nikoodriver.services.ApiService
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