package com.example.nikodriver.data.repositories

import com.example.nikodriver.data.TokenContainer
import com.example.nikodriver.data.repositories.sources.verification.VerificationDataSource
import com.example.nikodriver.data.verificationResponse.VerificationData
import com.example.nikodriver.data.verificationResponse.VerificationResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

class VerificationRepositoryImpl(
    val verificationLocalDataSource: VerificationDataSource,
    val verificationRemoteDataSource: VerificationDataSource
):VerificationRepository {
    override fun verification(phoneNumber: String, code: String): Single<Response<VerificationResponse>> {
        return verificationRemoteDataSource.verification(phoneNumber,code)
            .doOnSuccess {
                it.body()?.data?.let { it1->onSuccessfulVerification(it1) }
//            it.data?.let { it1 -> onSuccessfulVerification(it1) }
        }
    }

    override fun loadToken() {
        verificationLocalDataSource.loadToken()
    }

    override fun getUserName(): String {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }

    fun onSuccessfulVerification(tokenResponse: VerificationData) {
        TokenContainer.update(tokenResponse.token, tokenResponse.refreshToken)
        tokenResponse.token?.let { tokenResponse.refreshToken?.let { it1 ->
            verificationLocalDataSource.saveToken(it,
                it1
            )
        } }
    }
}