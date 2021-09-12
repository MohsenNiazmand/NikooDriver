package com.example.nikodriver.services

import com.example.nikodriver.data.loginResponse.LoginResponse
import com.example.nikodriver.data.verificationResponse.VerificationResponse
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/driver")
    fun login(@Body jsonObject: JsonObject):Single<LoginResponse>

    @POST("auth/driver/verify")
    fun verification(@Body jsonObject: JsonObject):Single<VerificationResponse>

    @POST("auth/driver/fill-info")
    fun register(@Body jsonObject: JsonObject) : Single<VerificationResponse>

}


fun createApiServiceInstance():ApiService{
    val retrofit=Retrofit.Builder()
        .baseUrl("https://api.nikohamrah.ir/api/v1/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

return retrofit.create(ApiService::class.java)

}