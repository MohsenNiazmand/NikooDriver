package com.akaf.nikoodriver.services

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent

//this class works for refreshing token

class AppAuthenticator : Authenticator, KoinComponent {
    override fun authenticate(route: Route?, response: Response): Request? {
        TODO("Not yet implemented")
    }


//    val apiService : ApiService by inject()
//    val hiveMqttManager:HiveMqttManager by inject()
//    val verificationLocalDataSource : VerificationDataSource by inject()
//    val homeLocalDataSource:HomeDataSource by inject()
//    override fun authenticate(route: Route?, response: Response): Request? {
//        if (TokenContainer.token != null && TokenContainer.refreshToken != null ) {
//            try {
//                val token = refreshToken()
//                if (token.isEmpty())
//                    return null
//
//
//                //work to do after getting new refresh token
//
//                return response.request().newBuilder().header("Authorization", "Bearer $token")
//                    .build()
//
//            } catch (exception: Exception) {
//                Timber.e(exception)
//            }
//        }
//
//        return null
//    }
//
//    fun refreshToken(): String {
//        HiveMqttManager.mqttConnectionState.onNext(HiveMqttManager.CONNECTION_FAILURE)
//
//        val response: retrofit2.Response<RefreshTokenData> =
//            apiService.refreshToken(JsonObject().apply {
//                addProperty("token", TokenContainer.token)
//                addProperty("refreshToken", TokenContainer.refreshToken)
//
//            }).execute()
//        response.body()?.let {
//            TokenContainer.update(it.token, it.refreshToken)
//            homeLocalDataSource.saveToken(it.token,it.refreshToken)
//            verificationLocalDataSource.saveToken(it.token, it.refreshToken)
//            hiveMqttManager.disconnect()
//            hiveMqttManager.connect()
//            return it.token
//        }
//
//        return ""
//    }

}