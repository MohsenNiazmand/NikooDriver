package com.akaf.nikoodriver.data.repositories.sources.CurrentTrips

import com.akaf.nikoodriver.data.responses.completeTripResponse.CompleteTripResponse
import com.akaf.nikoodriver.data.responses.currentTripsResponse.CurrentTripsResponse
import com.akaf.nikoodriver.data.responses.dropOfResponse.DropOfResponse
import com.akaf.nikoodriver.data.responses.pickUpResponse.PickUpResponse
import com.akaf.nikoodriver.data.responses.startTripResponse.StartTripResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

class CurrentTripsLocalDataSource : CurrentTripsDataSource {
    override fun currentTrips(): Single<Response<CurrentTripsResponse>> {
        TODO("Not yet implemented")
    }

    override fun startTrip(tripId: Int): Single<Response<StartTripResponse>> {
        TODO("Not yet implemented")
    }

    override fun pickUp(tripId: Int, sourceId: Int,location0:Double,location1:Double): Single<Response<PickUpResponse>> {
        TODO("Not yet implemented")
    }

    override fun dropOf(tripId: Int, sourceId: Int,location0:Double,location1:Double): Single<Response<DropOfResponse>> {
        TODO("Not yet implemented")
    }

    override fun completeTrip(tripId: Int): Single<Response<CompleteTripResponse>> {
        TODO("Not yet implemented")
    }

    override fun cancelTrip(tripId: Int): Completable {
        TODO("Not yet implemented")
    }
}