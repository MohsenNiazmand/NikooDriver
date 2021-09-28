package com.example.nikoodriver.data.repositories

import com.example.nikoodriver.data.fcmResponse.FcmResponse
import io.reactivex.Single
import retrofit2.Response

interface HomeRepository {
    fun sendFcmToken(fcmToken:String):Single<Response<FcmResponse>>
}