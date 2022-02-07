package com.akaf.nikoodriver.data.repositories.sources.CurrentTrips

import com.akaf.nikoodriver.data.responses.completeTripResponse.CompleteTripResponse
import com.akaf.nikoodriver.data.responses.currentTripsResponse.CurrentTripsResponse
import com.akaf.nikoodriver.data.responses.dropOfResponse.DropOfResponse
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.akaf.nikoodriver.data.responses.pickUpResponse.PickUpResponse
import com.akaf.nikoodriver.data.responses.startTripResponse.StartTripResponse
import com.akaf.nikoodriver.services.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

class CurrentTripsRemoteDataSource(val apiService: ApiService) : CurrentTripsDataSource {
    override fun currentTrips(): Single<Response<CurrentTripsResponse>> {
        return apiService.currentTrips()
    }

    override fun startTrip(tripId: Int): Single<Response<StartTripResponse>> {
        return apiService.startTrip(tripId)
    }

    override fun pickUp(tripId: Int, sourceId: Int,location0:Double,location1:Double): Single<Response<PickUpResponse>> {
        return apiService.pickUp(tripId,sourceId,location0,location1)
    }

    override fun dropOf(tripId: Int, sourceId: Int,location0:Double,location1:Double): Single<Response<DropOfResponse>> {
        return apiService.dropOf(tripId,sourceId,location0,location1)
    }

    override fun completeTrip(tripId: Int): Single<Response<CompleteTripResponse>> {
        return apiService.completeTrip(tripId)
    }

    override fun cancelTrip(tripId: Int,reason:String,sendLocation: ArrayList<Double>): Completable {
        return apiService.cancelTrip(tripId,reason,sendLocation)
    }
}