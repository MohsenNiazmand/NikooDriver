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
import io.reactivex.Single
import retrofit2.Response

class HomeLocalDataSource(val sharedPreferences: SharedPreferences) : HomeDataSource{
    override fun onlineStatus(isOnline: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean("isOnline",isOnline)
        }.apply()
    }

    override fun sendLocation(sendLocation: SendLocation): Single<Response<DriverLocationResponse>> {
        TODO("Not yet implemented")
    }

    override fun saveToken(token: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("token", token)
            putString("refresh_token", refreshToken)
        }.apply()
    }

    override fun refreshToken(
        token: String,
        refreshToken: String
    ): Single<Response<RefreshTokenResponse>> {
        TODO("Not yet implemented")
    }

    override fun clearSharedPreference(){
        sharedPreferences.edit().clear().apply()
    }

    override fun setEmptySeats(emptySeats: Int,isReady:Boolean): Single<Response<EmptySeatsResponse>> {
        TODO("Not yet implemented")
    }


    override fun acceptTrip(tripId: Int,cost:Int): Single<Response<AcceptOfferResponse>> {
        TODO("Not yet implemented")
    }

    override fun rejectTrip(tripId: Int): Single<Response<RejectOfferResponse>> {
        TODO("Not yet implemented")
    }

    override fun unAcceptedPassengersCount(): Single<Response<UnAcceptedPassengersResponse>> {
        TODO("Not yet implemented")
    }

    override fun getProfile(): Single<Response<ProfileResponse>> {
        TODO("Not yet implemented")
    }

    override fun saveUsername(userName: String) {
        sharedPreferences.edit().apply{
            putString("username",userName)
        }.apply()
    }

    override fun update(
        type: String,
        platform: String,
        version: String
    ): Single<Response<UpdateResponse>> {
        TODO("Not yet implemented")
    }

}