package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.fcmResponse.FcmResponse
import io.reactivex.Single
import retrofit2.Response

interface HomeRepository {
    fun sendFcmToken(fcmToken:String):Single<Response<FcmResponse>>
}