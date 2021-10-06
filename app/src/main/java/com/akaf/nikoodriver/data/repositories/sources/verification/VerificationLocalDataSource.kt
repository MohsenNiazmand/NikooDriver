package com.akaf.nikoodriver.data.repositories.sources.verification

import android.content.SharedPreferences
import com.akaf.nikoodriver.data.TokenContainer
import com.akaf.nikoodriver.data.fcmResponse.FcmResponse
import com.akaf.nikoodriver.data.verificationResponse.VerificationResponse
import io.reactivex.Single
import retrofit2.Response

class VerificationLocalDataSource(val sharedPreferences: SharedPreferences) : VerificationDataSource{
    override fun verification(phoneNumber: String, code: String): Single<Response<VerificationResponse>> {
        TODO("Not yet implemented")
    }

    override fun sendFcmToken(fcmToken: String): Single<Response<FcmResponse>> {
        TODO("Not yet implemented")
    }

    override fun loadToken() {
        TokenContainer.update(
            sharedPreferences.getString("token", null),
            sharedPreferences.getString("refresh_token", null)
        )
    }

    override fun saveToken(token: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("token", token)
            putString("refresh_token", refreshToken)
        }.apply()
    }

    override fun saveDriverId(driverId: String) {
        sharedPreferences.edit().apply {
            putString("driverId", driverId)
        }.apply()
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