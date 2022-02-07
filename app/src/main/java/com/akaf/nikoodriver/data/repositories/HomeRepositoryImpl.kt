package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.TokenContainer
import com.akaf.nikoodriver.data.responses.driverLocationResponse.DriverLocationResponse
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.akaf.nikoodriver.data.responses.refreshTokenResponse.RefreshTokenResponse
import com.akaf.nikoodriver.data.repositories.sources.home.HomeDataSource
import com.akaf.nikoodriver.data.responses.unAcceptedPassengers.UnAcceptedPassengersResponse
import com.akaf.nikoodriver.data.responses.emptySeatsResponse.EmptySeatsResponse
import com.akaf.nikoodriver.data.responses.offerResponse.accept.AcceptOfferResponse
import com.akaf.nikoodriver.data.responses.offerResponse.reject.RejectOfferResponse
import com.akaf.nikoodriver.data.responses.profileResponse.ProfileResponse
import com.akaf.nikoodriver.data.responses.updateResponse.UpdateResponse
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


    override fun clearSharedPreference() {
         homeLocalDataSource.clearSharedPreference()
        TokenContainer.update(null,null)

    }

    override fun setEmptySeats(emptySeats: Int,isReady:Boolean): Single<Response<EmptySeatsResponse>> {
        return homeRemoteDataSource.setEmptySeats(emptySeats,isReady)
    }


    override fun acceptTrip(tripId: Int,cost:Int): Single<Response<AcceptOfferResponse>> {
        return homeRemoteDataSource.acceptTrip(tripId,cost)
    }

    override fun rejectTrip(tripId: Int): Single<Response<RejectOfferResponse>> {
        return homeRemoteDataSource.rejectTrip(tripId)
    }

    override fun unAcceptedPassengersCount(): Single<Response<UnAcceptedPassengersResponse>> {
        return homeRemoteDataSource.unAcceptedPassengersCount()
    }

    override fun getProfile(): Single<Response<ProfileResponse>> {
        return homeRemoteDataSource.getProfile().doOnSuccess {
            if (it.body()?.data!=null)
                it.body()?.data?.credit?.let { it1 ->
                    homeLocalDataSource.saveUserInformation(it.body()?.data?.fname+" "+it.body()?.data?.lname,
                        it1,it.body()?.data!!.rate,it.body()?.data?.capacity.toString(),it.body()?.data?.currentTripsCount.toString(),it.body()?.data?.openTripsCount.toString(),
                        it.body()!!.data.CarDriver.Service.capacity.toString())
                }

    }
    }

    override fun update(
        type: String,
        platform: String,
        version: String
    ): Single<Response<UpdateResponse>> {
        return homeRemoteDataSource.update(type,platform,version)
    }

    override fun loadToken() {
        homeLocalDataSource.loadToken()
    }

}