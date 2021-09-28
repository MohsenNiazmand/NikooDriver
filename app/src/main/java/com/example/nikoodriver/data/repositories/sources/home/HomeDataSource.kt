package com.example.nikoodriver.data.repositories.sources.home

import com.example.nikoodriver.data.fcmResponse.FcmResponse
import io.reactivex.Single
import retrofit2.Response

interface HomeDataSource {
    fun sendFcmToken(fcmToken:String): Single<Response<FcmResponse>>

}