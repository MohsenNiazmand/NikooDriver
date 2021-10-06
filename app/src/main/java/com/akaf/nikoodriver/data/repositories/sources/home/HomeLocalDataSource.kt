package com.akaf.nikoodriver.data.repositories.sources.home

import android.content.SharedPreferences
import com.akaf.nikoodriver.data.driverLocationResponse.DriverLocationResponse
import com.akaf.nikoodriver.data.location.SendLocation
import com.akaf.nikoodriver.data.refreshTokenResponse.RefreshTokenResponse
import io.reactivex.Single
import retrofit2.Response

class HomeLocalDataSource(val sharedPreferences: SharedPreferences) : HomeDataSource{
    override fun onlineStatus(isOnline: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean("isOnline",isOnline)
        }.apply()
    }

    override fun sendLocation(sendLocation: SendLocation): Single<Response<DriverLocationResponse>> {
        TODO("Not yet implemented")
    }

    override fun saveToken(token: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("token", token)
            putString("refresh_token", refreshToken)
        }.apply()
    }

    override fun refreshToken(
        token: String,
        refreshToken: String
    ): Single<Response<RefreshTokenResponse>> {
        TODO("Not yet implemented")
    }

    override fun clearSharedPreference(){
        sharedPreferences.edit().clear().apply()
    }

    override fun saveTokenStatus(isExpired: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean("expired", isExpired)
        }.apply()    }

}