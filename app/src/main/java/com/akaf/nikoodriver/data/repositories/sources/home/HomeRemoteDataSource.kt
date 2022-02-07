package com.akaf.nikoodriver.data.repositories.sources.home

import android.content.SharedPreferences
import com.akaf.nikoodriver.data.responses.unAcceptedPassengers.UnAcceptedPassengersResponse
import com.akaf.nikoodriver.data.responses.driverLocationResponse.DriverLocationResponse
import com.akaf.nikoodriver.data.responses.emptySeatsResponse.EmptySeatsResponse
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.akaf.nikoodriver.data.responses.offerResponse.accept.AcceptOfferResponse
import com.akaf.nikoodriver.data.responses.offerResponse.reject.RejectOfferResponse
import com.akaf.nikoodriver.data.responses.profileResponse.ProfileResponse
import com.akaf.nikoodriver.data.responses.refreshTokenResponse.RefreshTokenResponse
import com.akaf.nikoodriver.data.responses.updateResponse.UpdateResponse
import com.akaf.nikoodriver.services.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response

class HomeRemoteDataSource(val apiService: ApiService, val sharedPreferences: SharedPreferences):HomeDataSource {
    override fun onlineStatus(isOnline: Boolean) {
        TODO("Not yet implemented")
    }

    override fun sendLocation(sendLocation: SendLocation): Single<Response<DriverLocationResponse>> {
       return apiService.sendLocation(sendLocation)
    }

    override fun clearSharedPreference() {
        TODO("Not yet implemented")
    }

    override fun setEmptySeats(emptySeats: Int,isReady:Boolean): Single<Response<EmptySeatsResponse>> {
        return apiService.setEmptySeats(emptySeats,isReady)
    }

    override fun acceptTrip(tripId: Int,cost:Int): Single<Response<AcceptOfferResponse>> {
        return apiService.acceptTrip(tripId,cost)
    }

    override fun rejectTrip(tripId: Int): Single<Response<RejectOfferResponse>> {
        return apiService.rejectTrip(tripId)
    }

    override fun unAcceptedPassengersCount(): Single<Response<UnAcceptedPassengersResponse>> {
        return apiService.unAcceptedPassengers()
    }

    override fun getProfile(): Single<Response<ProfileResponse>> {
        return apiService.getProfile()
        }

    override fun saveUserInformation(
        userName: String,
        credit: String,
        rate: String,
        emptySeats: String,
        currentTrips: String,
        unAcceptedPassengers: String,
        maxCapacity:String
    ) {
        TODO("Not yet implemented")
    }


    override fun update(
        type: String,
        platform: String,
        version: String
    ): Single<Response<UpdateResponse>> {
        return apiService.update(type,platform,version)
    }

    override fun loadToken() {
        TODO("Not yet implemented")
    }


}

