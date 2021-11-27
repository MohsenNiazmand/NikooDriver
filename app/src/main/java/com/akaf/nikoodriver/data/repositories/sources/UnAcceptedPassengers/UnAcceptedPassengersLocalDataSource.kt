package com.akaf.nikoodriver.data.repositories.sources.UnAcceptedPassengers

import com.akaf.nikoodriver.data.responses.unAcceptedPassengers.UnAcceptedPassengersResponse
import io.reactivex.Single
import retrofit2.Response

class UnAcceptedPassengersLocalDataSource :UnAcceptedPassengersDataSource {
    override fun unAcceptedPassengers(): Single<Response<UnAcceptedPassengersResponse>> {
        TODO("Not yet implemented")
    }
}