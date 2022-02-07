package com.akaf.nikoodriver.services.http

import android.content.Intent
import android.content.SharedPreferences
import com.akaf.nikoodriver.App
import com.akaf.nikoodriver.data.TokenContainer
import com.akaf.nikoodriver.data.responses.refreshTokenResponse.RefreshTokenResponse
import com.akaf.nikoodriver.feature.auth.login.LoginActivity
import com.akaf.nikoodriver.services.DriverForegroundService
import com.google.gson.JsonObject
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

@KoinApiExtension
class NikooAuthenticator : Authenticator, KoinComponent {
    val apiService: ApiService by inject()
    val sharedPreferences:SharedPreferences by inject()
    override fun authenticate(route: Route?, response: Response): Request? {
        if (TokenContainer.token != null && TokenContainer.refreshToken != null) {
            try {
                val token = refreshToken()
                if (token != null) {
                    if (token.isEmpty())
                        return null
                }

                if (token != null) {
                    return response.request().newBuilder().header("Authorization", "Bearer ${token}")
                        .build()
                }

            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }

        return null
    }

    fun refreshToken() : String? {
        val response: retrofit2.Response<RefreshTokenResponse> =
            apiService.refreshToken(JsonObject().apply {
                addProperty("token", TokenContainer.token)
                addProperty("refreshToken", TokenContainer.refreshToken)
            }).execute()
        if (response.code()==200){
            response.body()?.let {
                TokenContainer.update(it.data?.token, it.data?.refreshToken)
                it.data?.let { it1 ->

                    sharedPreferences.edit().apply {
                        putString("token", it1.token)
                        putString("refresh_token", it1.refreshToken)
                    }.apply()

                }
                return it.data?.token
            }
        }
        else if (response.code()==403){
            sharedPreferences.edit().clear().apply()
            App.context.cacheDir.deleteRecursively()
            if (DriverForegroundService.instance!=null)
                DriverForegroundService.stopService(App.context)
            App.activity?.finish()
            val intent= Intent(App.context, LoginActivity::class.java).apply {
                action = "com.package.ACTION_LOGOUT"
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            App.context.startActivity(intent)
            App.activity?.overridePendingTransition(0, 0)

        }
        return ""
    }

}