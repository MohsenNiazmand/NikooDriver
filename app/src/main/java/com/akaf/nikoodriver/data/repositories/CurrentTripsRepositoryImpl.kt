package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.repositories.sources.CurrentTrips.CurrentTripsDataSource
import com.akaf.nikoodriver.data.repositories.sources.CurrentTrips.CurrentTripsLocalDataSource
import com.akaf.nikoodriver.data.repositories.sources.CurrentTrips.CurrentTripsRemoteDataSource
import com.akaf.nikoodriver.data.responses.completeTripResponse.CompleteTripResponse
import com.akaf.nikoodriver.data.responses.currentTripsResponse.CurrentTripsResponse
import com.akaf.nikoodriver.data.responses.dropOfResponse.DropOfResponse
import com.akaf.nikoodriver.data.responses.location.SendLocation
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

    override fun pickUp(tripId: Int, sourceId: Int,location0:Double,location1:Double): Single<Response<PickUpResponse>> {
        return currentTripsRemoteDataSource.pickUp(tripId,sourceId,location0,location1)
    }

    override fun dropOf(tripId: Int, sourceId: Int,location0:Double,location1:Double): Single<Response<DropOfResponse>> {
        return currentTripsRemoteDataSource.dropOf(tripId,sourceId,location0,location1)
    }

    override fun completeTrip(tripId: Int): Single<Response<CompleteTripResponse>> {
        return currentTripsRemoteDataSource.completeTrip(tripId)
    }

    override fun cancelTrip(tripId: Int,reason:String,sendLocation: ArrayList<Double>): Completable {
        return currentTripsRemoteDataSource.cancelTrip(tripId,reason,sendLocation)
    }
}