package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.responses.driverLocationResponse.DriverLocationResponse
import com.akaf.nikoodriver.data.responses.emptySeatsResponse.EmptySeatsResponse
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.akaf.nikoodriver.data.responses.offerResponse.accept.AcceptOfferResponse
import com.akaf.nikoodriver.data.responses.offerResponse.reject.RejectOfferResponse
import com.akaf.nikoodriver.data.responses.refreshTokenResponse.RefreshTokenResponse
import io.reactivex.Single
import retrofit2.Response

interface HomeRepository {
    fun onlineStatus(isOnline: Boolean)
    fun sendLocation(sendLocation: SendLocation):Single<Response<DriverLocationResponse>>
    fun refreshToken(token: String, refreshToken: String):Single<Response<RefreshTokenResponse>>
    fun saveToken(token: String, refreshToken: String)
    fun saveTokenStatus(isExpired:Boolean)
    fun clearSharedPreference()
    fun setEmptySeats(emptySeats:Int):Single<Response<EmptySeatsResponse>>
    fun acceptTrip(tripId:Int,cost:Int):Single<Response<AcceptOfferResponse>>
    fun rejectTrip(tripId:Int):Single<Response<RejectOfferResponse>>
}