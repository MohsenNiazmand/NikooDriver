package com.akaf.nikoodriver.data.repositories.sources.CurrentTrips

import com.akaf.nikoodriver.data.responses.completeTripResponse.CompleteTripResponse
import com.akaf.nikoodriver.data.responses.currentTripsResponse.CurrentTripsResponse
import com.akaf.nikoodriver.data.responses.dropOfResponse.DropOfResponse
import com.akaf.nikoodriver.data.responses.pickUpResponse.PickUpResponse
import com.akaf.nikoodriver.data.responses.startTripResponse.StartTripResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

interface CurrentTripsDataSource {
    fun currentTrips():Single<Response<CurrentTripsResponse>>
    fun startTrip(tripId:Int):Single<Response<StartTripResponse>>
    fun pickUp(tripId: Int,sourceId:Int):Single<Response<PickUpResponse>>
    fun dropOf(tripId: Int,sourceId:Int):Single<Response<DropOfResponse>>
    fun completeTrip(tripId: Int):Single<Response<CompleteTripResponse>>
    fun cancelTrip(tripId: Int): Completable

}