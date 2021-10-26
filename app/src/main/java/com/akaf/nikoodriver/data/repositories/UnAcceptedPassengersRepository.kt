package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.responses.UnAcceptedPassengers.UnAcceptedPassengersResponse
import io.reactivex.Single
import retrofit2.Response

interface UnAcceptedPassengersRepository {
    fun unAcceptedPassengers(): Single<Response<UnAcceptedPassengersResponse>>

}