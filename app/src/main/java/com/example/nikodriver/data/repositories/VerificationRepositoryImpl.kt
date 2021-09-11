package com.example.nikodriver.data.repositories

import com.example.nikodriver.data.TokenContainer
import com.example.nikodriver.data.repositories.sources.verification.VerificationDataSource
import com.example.nikodriver.data.verificationResponse.VerificationData
import com.example.nikodriver.data.verificationResponse.VerificationResponse
import io.reactivex.Completable
import io.reactivex.Single

class VerificationRepositoryImpl(
    val verificationLocalDataSource: VerificationDataSource,
    val verificationRemoteDataSource: VerificationDataSource
):VerificationRepository {
    override fun verification(phoneNumber: String, code: String): Single<VerificationResponse> {
        return verificationRemoteDataSource.verification(phoneNumber,code).doOnSuccess {
            onSuccessfulVerification(code,it.data)
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

    fun onSuccessfulVerification(username: String, tokenResponse: VerificationData) {
        TokenContainer.update(tokenResponse.token, tokenResponse.refreshToken)
        verificationLocalDataSource.saveToken(tokenResponse.token, tokenResponse.refreshToken)
//        verificationLocalDataSource.saveUsername(username)
    }
}