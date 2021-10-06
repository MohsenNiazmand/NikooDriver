package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.driverLocationResponse.DriverLocationResponse
import com.akaf.nikoodriver.data.fcmResponse.FcmResponse
import com.akaf.nikoodriver.data.location.SendLocation
import com.akaf.nikoodriver.data.refreshTokenResponse.RefreshTokenResponse
import io.reactivex.Single
import retrofit2.Response

interface HomeRepository {
    fun onlineStatus(isOnline: Boolean)
    fun sendLocation(sendLocation: SendLocation):Single<Response<DriverLocationResponse>>
    fun refreshToken(token: String, refreshToken: String):Single<Response<RefreshTokenResponse>>
    fun saveToken(token: String, refreshToken: String)
    fun saveTokenStatus(isExpired:Boolean)

}