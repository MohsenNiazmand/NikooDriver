package com.akaf.nikoodriver.data.repositories.sources.home

import com.akaf.nikoodriver.data.driverLocationResponse.DriverLocationResponse
import com.akaf.nikoodriver.data.fcmResponse.FcmResponse
import com.akaf.nikoodriver.data.location.SendLocation
import com.akaf.nikoodriver.data.refreshTokenResponse.RefreshTokenData
import com.akaf.nikoodriver.data.refreshTokenResponse.RefreshTokenResponse
import io.reactivex.Single
import retrofit2.Response

interface HomeDataSource {
    fun onlineStatus(isOnline: Boolean)
    fun sendLocation(sendLocation: SendLocation):Single<Response<DriverLocationResponse>>
    fun saveToken(token: String, refreshToken: String)
    fun refreshToken(token: String, refreshToken: String):Single<Response<RefreshTokenResponse>>
    fun clearSharedPreference()
    fun saveTokenStatus(isExpired:Boolean)

}