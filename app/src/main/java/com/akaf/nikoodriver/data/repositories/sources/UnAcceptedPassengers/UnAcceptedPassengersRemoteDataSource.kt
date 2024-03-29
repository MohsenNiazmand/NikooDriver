package com.akaf.nikoodriver.data.repositories.sources.UnAcceptedPassengers

import com.akaf.nikoodriver.data.responses.unAcceptedPassengers.UnAcceptedPassengersResponse
import com.akaf.nikoodriver.services.http.ApiService
import io.reactivex.Single
import retrofit2.Response

class UnAcceptedPassengersRemoteDataSource(val apiService: ApiService):UnAcceptedPassengersDataSource {
    override fun unAcceptedPassengers(): Single<Response<UnAcceptedPassengersResponse>> {
        return apiService.unAcceptedPassengers()
    }
}