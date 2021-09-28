package com.example.nikoodriver.data.repositories

import com.example.nikoodriver.data.fcmResponse.FcmResponse
import com.example.nikoodriver.data.repositories.sources.home.HomeDataSource
import io.reactivex.Single
import retrofit2.Response

class HomeRepositoryImpl(
    val homeLocalDataSource:HomeDataSource,
    val homeRemoteDataSource:HomeDataSource

    ) : HomeRepository {
    override fun sendFcmToken(fcmToken: String): Single<Response<FcmResponse>> {
        return homeRemoteDataSource.sendFcmToken(fcmToken)
    }
}