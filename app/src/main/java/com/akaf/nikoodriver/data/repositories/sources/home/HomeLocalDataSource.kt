package com.akaf.nikoodriver.data.repositories.sources.home

import com.akaf.nikoodriver.data.fcmResponse.FcmResponse
import io.reactivex.Single
import retrofit2.Response

class HomeLocalDataSource : HomeDataSource{
    override fun sendFcmToken(fcmToken: String): Single<Response<FcmResponse>> {
        TODO("Not yet implemented")
    }
}