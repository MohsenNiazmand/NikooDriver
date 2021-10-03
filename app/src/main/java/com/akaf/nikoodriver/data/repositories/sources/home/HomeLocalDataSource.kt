package com.akaf.nikoodriver.data.repositories.sources.home

import android.content.SharedPreferences
import com.akaf.nikoodriver.data.fcmResponse.FcmResponse
import io.reactivex.Single
import retrofit2.Response

class HomeLocalDataSource(val sharedPreferences: SharedPreferences) : HomeDataSource{
    override fun onlineStatus(isOnline: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean("isOnline",isOnline)
        }.apply()
    }

}