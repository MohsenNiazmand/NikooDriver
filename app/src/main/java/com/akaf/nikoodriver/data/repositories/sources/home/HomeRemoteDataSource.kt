package com.akaf.nikoodriver.data.repositories.sources.home

import com.akaf.nikoodriver.data.responses.UnAcceptedPassengers.UnAcceptedPassengersResponse
import com.akaf.nikoodriver.data.responses.driverLocationResponse.DriverLocationResponse
import com.akaf.nikoodriver.data.responses.emptySeatsResponse.EmptySeatsResponse
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.akaf.nikoodriver.data.responses.offerResponse.accept.AcceptOfferResponse
import com.akaf.nikoodriver.data.responses.offerResponse.reject.RejectOfferResponse
import com.akaf.nikoodriver.data.responses.refreshTokenResponse.RefreshTokenResponse
import com.akaf.nikoodriver.services.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Response

class HomeRemoteDataSource(val apiService: ApiService):HomeDataSource {
    override fun onlineStatus(isOnline: Boolean) {
        TODO("Not yet implemented")
    }

    override fun sendLocation(sendLocation: SendLocation): Single<Response<DriverLocationResponse>> {
       return apiService.sendLocation(sendLocation)
    }

    override fun saveToken(token: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun refreshToken(token: String, refreshToken: String):Single<Response<RefreshTokenResponse>> {
        return apiService.refreshToken(JsonObject().apply {
            addProperty("token",token)
            addProperty("refreshToken",refreshToken)
        })
    }

    override fun clearSharedPreference() {
        TODO("Not yet implemented")
    }

    override fun saveTokenStatus(isExpired: Boolean) {
        TODO("Not yet implemented")
    }


    override fun setEmptySeats(emptySeats: Int,isReady:Boolean): Single<Response<EmptySeatsResponse>> {
        return apiService.setEmptySeats(emptySeats,isReady)
    }

    override fun emptySeatsCount(emptySeats: Int) {
        TODO("Not yet implemented")
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

}