package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.repositories.sources.UnAcceptedPassengers.UnAcceptedPassengersDataSource
import com.akaf.nikoodriver.data.responses.unAcceptedPassengers.UnAcceptedPassengersResponse
import io.reactivex.Single
import retrofit2.Response

class UnAcceptedPassengersRepositoryImpl(
    val unAcceptedPassengersLocalDataSource: UnAcceptedPassengersDataSource,
    val unAcceptedPassengersRemoteDataSource: UnAcceptedPassengersDataSource
) : UnAcceptedPassengersRepository{
    override fun unAcceptedPassengers(): Single<Response<UnAcceptedPassengersResponse>> {
        return unAcceptedPassengersRemoteDataSource.unAcceptedPassengers()
    }
}