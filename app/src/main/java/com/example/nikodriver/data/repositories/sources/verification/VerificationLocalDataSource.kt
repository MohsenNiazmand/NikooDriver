package com.example.nikodriver.data.repositories.sources.verification

import android.content.SharedPreferences
import com.example.nikodriver.data.TokenContainer
import com.example.nikodriver.data.verificationResponse.VerificationResponse
import io.reactivex.Single
import retrofit2.Response

class VerificationLocalDataSource(val sharedPreferences: SharedPreferences) : VerificationDataSource{
    override fun verification(phoneNumber: String, code: String): Single<Response<VerificationResponse>> {
        TODO("Not yet implemented")
    }

    override fun loadToken() {
        TokenContainer.update(
            sharedPreferences.getString("access_token", null),
            sharedPreferences.getString("refresh_token", null)
        )
    }

    override fun saveToken(token: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("access_token", token)
            putString("refresh_token", refreshToken)
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