package com.example.nikoodriver.services

import com.example.nikoodriver.data.TokenContainer
import com.example.nikoodriver.data.refreshTokenResponse.RefreshTokenData
import com.example.nikoodriver.data.repositories.sources.verification.VerificationDataSource
import com.google.gson.JsonObject
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

//this class works for refreshing token

class AppAuthenticator : Authenticator, KoinComponent {


    val apiService : ApiService by inject()
    val verificationLocalDataSource : VerificationDataSource by inject()
    override fun authenticate(route: Route?, response: Response): Request? {
        if (TokenContainer.token != null && TokenContainer.refreshToken != null ) {
            try {
                val token = refreshToken()
                if (token.isEmpty())
                    return null


                //work to do after getting new refresh token
                return response.request.newBuilder().header("Authorization", "Bearer $token")
                    .build()

            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }

        return null
    }

    fun refreshToken(): String {
        val response: retrofit2.Response<RefreshTokenData> =
            apiService.refreshToken(JsonObject().apply {
                addProperty("token", TokenContainer.token)
                addProperty("refresh_token", TokenContainer.refreshToken)

            }).execute()
        response.body()?.let {
            TokenContainer.update(it.token, it.refreshToken)
            verificationLocalDataSource.saveToken(it.token, it.refreshToken)
            return it.token
        }

        return ""
    }

}