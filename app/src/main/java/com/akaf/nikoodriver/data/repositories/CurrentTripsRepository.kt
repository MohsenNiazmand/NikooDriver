package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.responses.completeTripResponse.CompleteTripResponse
import com.akaf.nikoodriver.data.responses.currentTripsResponse.CurrentTripsResponse
import com.akaf.nikoodriver.data.responses.dropOfResponse.DropOfResponse
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.akaf.nikoodriver.data.responses.pickUpResponse.PickUpResponse
import com.akaf.nikoodriver.data.responses.startTripResponse.StartTripResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Path

interface CurrentTripsRepository {
    fun currentTrips(): Single<Response<CurrentTripsResponse>>
    fun startTrip(tripId:Int):Single<Response<StartTripResponse>>
    fun pickUp(tripId: Int,sourceId:Int,location0:Double,location1:Double):Single<Response<PickUpResponse>>
    fun dropOf(tripId: Int,sourceId:Int,location0:Double,location1:Double):Single<Response<DropOfResponse>>
    fun completeTrip(tripId: Int):Single<Response<CompleteTripResponse>>
    fun cancelTrip(tripId: Int,reason:String,sendLocation: ArrayList<Double>):Completable
}