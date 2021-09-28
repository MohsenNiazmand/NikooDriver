package com.akaf.nikoodriver.data.repositories.sources.home

import com.akaf.nikoodriver.data.fcmResponse.FcmResponse
import io.reactivex.Single
import retrofit2.Response

interface HomeDataSource {
    fun sendFcmToken(fcmToken:String): Single<Response<FcmResponse>>

}