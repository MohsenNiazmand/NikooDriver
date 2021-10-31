package com.akaf.nikoodriver.data.repositories.sources.CurrentTrips

import com.akaf.nikoodriver.data.responses.completeTripResponse.CompleteTripResponse
import com.akaf.nikoodriver.data.responses.currentTripsResponse.CurrentTripsResponse
import com.akaf.nikoodriver.data.responses.dropOfResponse.DropOfResponse
import com.akaf.nikoodriver.data.responses.pickUpResponse.PickUpResponse
import com.akaf.nikoodriver.data.responses.startTripResponse.StartTripResponse
import com.akaf.nikoodriver.services.ApiService
import io.reactivex.Single
import retrofit2.Response

class CurrentTripsRemoteDataSource(val apiService: ApiService) : CurrentTripsDataSource {
    override fun currentTrips(): Single<Response<CurrentTripsResponse>> {
        return apiService.currentTrips()
    }

    override fun startTrip(tripId: Int): Single<Response<StartTripResponse>> {
        return apiService.startTrip(tripId)
    }

    override fun pickUp(tripId: Int, sourceId: Int): Single<Response<PickUpResponse>> {
        return apiService.pickUp(tripId,sourceId)
    }

    override fun dropOf(tripId: Int, sourceId: Int): Single<Response<DropOfResponse>> {
        return apiService.dropOf(tripId,sourceId)
    }

    override fun completeTrip(tripId: Int): Single<Response<CompleteTripResponse>> {
        return apiService.completeTrip(tripId)
    }
}