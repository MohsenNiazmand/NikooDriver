package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.TokenContainer
import com.akaf.nikoodriver.data.responses.fcmResponse.FcmResponse
import com.akaf.nikoodriver.data.repositories.sources.verification.VerificationDataSource
import com.akaf.nikoodriver.data.responses.verificationResponse.VerificationData
import com.akaf.nikoodriver.data.responses.verificationResponse.VerificationResponse
import io.reactivex.Single
import retrofit2.Response

class VerificationRepositoryImpl(
    val verificationLocalDataSource: VerificationDataSource,
    val verificationRemoteDataSource: VerificationDataSource
):VerificationRepository {
    override fun verification(phoneNumber: String, code: String): Single<Response<VerificationResponse>> {
        return verificationRemoteDataSource.verification(phoneNumber,code)
            .doOnSuccess {
                if (it.body()?.data?.driver?.status=="active"){
                    it.body()?.data?.let { it1->onSuccessfulVerification(it1.driver.id,it1) }
                }
        }
    }

    override fun loadToken() {
        verificationLocalDataSource.loadToken()
    }

    override fun sendFcmToken(fcmToken: String): Single<Response<FcmResponse>> {
        return verificationRemoteDataSource.sendFcmToken(fcmToken)
    }

    override fun getUserName(): String {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }

    fun onSuccessfulVerification(driverId:String,tokenResponse: VerificationData) {
        TokenContainer.update(tokenResponse.token, tokenResponse.refreshToken)
        verificationLocalDataSource.saveToken(tokenResponse.token, tokenResponse.refreshToken)
        verificationLocalDataSource.saveDriverId(driverId)
    }
}