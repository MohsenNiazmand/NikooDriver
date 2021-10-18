package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.TokenContainer
import com.akaf.nikoodriver.data.responses.driverLocationResponse.DriverLocationResponse
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.akaf.nikoodriver.data.responses.refreshTokenResponse.RefreshTokenResponse
import com.akaf.nikoodriver.data.repositories.sources.home.HomeDataSource
import com.akaf.nikoodriver.data.responses.emptySeatsResponse.EmptySeatsResponse
import com.akaf.nikoodriver.data.responses.mqttTripResponse.TripData
import com.akaf.nikoodriver.data.responses.offerResponse.accept.AcceptOfferResponse
import com.akaf.nikoodriver.data.responses.offerResponse.reject.RejectOfferResponse
import io.reactivex.Single
import retrofit2.Response

class HomeRepositoryImpl(
    val homeLocalDataSource:HomeDataSource,
    val homeRemoteDataSource:HomeDataSource

    ) : HomeRepository {
    override fun onlineStatus(isOnline: Boolean) {
        homeLocalDataSource.onlineStatus(isOnline)
    }

    override fun sendLocation(sendLocation: SendLocation): Single<Response<DriverLocationResponse>> {
       return homeRemoteDataSource.sendLocation(sendLocation)
    }

    override fun refreshToken(
        token: String,
        refreshToken: String
    ): Single<Response<RefreshTokenResponse>> {
        return homeRemoteDataSource.refreshToken(token,refreshToken).doOnSuccess {
            it.body()?.data?.token?.let { it1 -> it.body()?.data?.refreshToken?.let { it2 ->
                TokenContainer.update(it1, it2)
                homeLocalDataSource.saveToken(it1, it2)

            } }
        }.doOnError {
            homeLocalDataSource.clearSharedPreference()

        }
    }

    override fun saveToken(token: String, refreshToken: String) {
        return homeLocalDataSource.saveToken(token,refreshToken)
    }

    override fun saveTokenStatus(isExpired: Boolean) {
        return homeLocalDataSource.saveTokenStatus(isExpired)
    }

    override fun clearSharedPreference() {
        return homeLocalDataSource.clearSharedPreference()
    }

    override fun setEmptySeats(emptySeats: Int): Single<Response<EmptySeatsResponse>> {
        return homeRemoteDataSource.setEmptySeats(emptySeats)
    }

    override fun acceptTrip(tripId: Int,cost:Int): Single<Response<AcceptOfferResponse>> {
        return homeRemoteDataSource.acceptTrip(tripId,cost)
    }

    override fun rejectTrip(tripId: Int): Single<Response<RejectOfferResponse>> {
        return homeRemoteDataSource.rejectTrip(tripId)
    }

    override fun getCurrentTrip(): Single<Response<TripData>> {
        return homeRemoteDataSource.getCurrentTrip()
    }


}