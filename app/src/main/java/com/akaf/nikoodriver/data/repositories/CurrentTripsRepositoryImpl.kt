package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.repositories.sources.CurrentTrips.CurrentTripsDataSource
import com.akaf.nikoodriver.data.repositories.sources.CurrentTrips.CurrentTripsLocalDataSource
import com.akaf.nikoodriver.data.repositories.sources.CurrentTrips.CurrentTripsRemoteDataSource
import com.akaf.nikoodriver.data.responses.completeTripResponse.CompleteTripResponse
import com.akaf.nikoodriver.data.responses.currentTripsResponse.CurrentTripsResponse
import com.akaf.nikoodriver.data.responses.dropOfResponse.DropOfResponse
import com.akaf.nikoodriver.data.responses.pickUpResponse.PickUpResponse
import com.akaf.nikoodriver.data.responses.startTripResponse.StartTripResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

class CurrentTripsRepositoryImpl(
   val currentTripsLocalDataSource: CurrentTripsDataSource,
   val currentTripsRemoteDataSource: CurrentTripsDataSource
) :CurrentTripsRepository {
    override fun currentTrips(): Single<Response<CurrentTripsResponse>> {
        return currentTripsRemoteDataSource.currentTrips()
    }

    override fun startTrip(tripId: Int): Single<Response<StartTripResponse>> {
        return currentTripsRemoteDataSource.startTrip(tripId)
    }

    override fun pickUp(tripId: Int, sourceId: Int): Single<Response<PickUpResponse>> {
        return currentTripsRemoteDataSource.pickUp(tripId,sourceId)
    }

    override fun dropOf(tripId: Int, sourceId: Int): Single<Response<DropOfResponse>> {
        return currentTripsRemoteDataSource.dropOf(tripId,sourceId)
    }

    override fun completeTrip(tripId: Int): Single<Response<CompleteTripResponse>> {
        return currentTripsRemoteDataSource.completeTrip(tripId)
    }

    override fun cancelTrip(tripId: Int): Completable {
        return currentTripsRemoteDataSource.cancelTrip(tripId)
    }
}