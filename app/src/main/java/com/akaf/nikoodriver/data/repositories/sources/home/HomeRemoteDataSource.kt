package com.akaf.nikoodriver.data.repositories.sources.home

import com.akaf.nikoodriver.data.driverLocationResponse.DriverLocationResponse
import com.akaf.nikoodriver.data.fcmResponse.FcmResponse
import com.akaf.nikoodriver.data.location.SendLocation
import com.akaf.nikoodriver.data.refreshTokenResponse.RefreshTokenResponse
import com.akaf.nikoodriver.services.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response

class HomeRemoteDataSource(val apiService: ApiService):HomeDataSource {
    override fun onlineStatus(isOnline: Boolean) {
        TODO("Not yet implemented")
    }

    override fun sendLocation(sendLocation: SendLocation): Single<Response<DriverLocationResponse>> {
       return apiService.sendLocation(sendLocation)
    }

    override fun saveToken(token: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun refreshToken(token: String, refreshToken: String):Single<Response<RefreshTokenResponse>> {
        return apiService.refreshToken(JsonObject().apply {
            addProperty("token",token)
            addProperty("refreshToken",refreshToken)
        })
    }

    override fun clearSharedPreference() {
        TODO("Not yet implemented")
    }

    override fun saveTokenStatus(isExpired: Boolean) {
        TODO("Not yet implemented")
    }


}