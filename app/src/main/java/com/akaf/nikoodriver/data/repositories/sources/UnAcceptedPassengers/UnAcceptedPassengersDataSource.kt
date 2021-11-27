package com.akaf.nikoodriver.data.repositories.sources.UnAcceptedPassengers

import com.akaf.nikoodriver.data.responses.unAcceptedPassengers.UnAcceptedPassengersResponse
import io.reactivex.Single
import retrofit2.Response

interface UnAcceptedPassengersDataSource {
    fun unAcceptedPassengers(): Single<Response<UnAcceptedPassengersResponse>>


}