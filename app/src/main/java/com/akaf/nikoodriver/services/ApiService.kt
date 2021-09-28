package com.akaf.nikoodriver.services

import com.akaf.nikoodriver.data.TokenContainer
import com.akaf.nikoodriver.data.fcmResponse.FcmResponse
import com.akaf.nikoodriver.data.fillInfoResponse.DriverUploadPhotoResponse.UploadPhotoDriverResponse
import com.akaf.nikoodriver.data.fillInfoResponse.FillInfoResponse
import com.akaf.nikoodriver.data.loginResponse.LoginResponse
import com.akaf.nikoodriver.data.refreshTokenResponse.RefreshTokenData
import com.akaf.nikoodriver.data.submitDocsResponse.SubmitDocsResponse
import com.akaf.nikoodriver.data.uploadDocResponse.UploadDocResponse
import com.akaf.nikoodriver.data.verificationResponse.VerificationResponse
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {
    @POST("auth/driver")
    fun login(@Body jsonObject: JsonObject):Single<LoginResponse>

    @POST("auth/driver/verify")
    fun verification(@Body jsonObject: JsonObject):Single<Response<VerificationResponse>>

    @POST( "auth/driver/fill-info")
    fun fillInfo(@Header("Authorization") token:String, @Body jsonObject: JsonObject) : Single<Response<FillInfoResponse>>

    @Multipart
    @POST("auth/driver/photo")
    fun uploadDriverPhoto(@Part("title") title: String,
                          @Part driverPhoto:MultipartBody.Part ) : Single<Response<UploadPhotoDriverResponse>>


    @POST("auth/refresh")
    fun refreshToken(@Body jsonObject: JsonObject): Call<RefreshTokenData>

    @Multipart
    @POST("auth/driver/docs")
    fun uploadDoc(@Part("title") title:String,
                  @Part doc: MultipartBody.Part) : Single<Response<UploadDocResponse>>

    @POST("auth/driver/docs/all")
    fun submitDocs(@Header("Authorization") token:String,@Body jsonObject: JsonObject) : Single<Response<SubmitDocsResponse>>

    @POST("driver/profile/fcm")
    fun sendFcmToken(@Body jsonObject: JsonObject):Single<Response<FcmResponse>>


}


fun createApiServiceInstance():ApiService{

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor {
            val oldRequest = it.request()
            val newRequestBuilder = oldRequest.newBuilder()
            if (TokenContainer.token != null)
                newRequestBuilder.addHeader("Authorization", "Bearer ${TokenContainer.token}")

            newRequestBuilder.addHeader("Accept", "application/json")
            newRequestBuilder.method(oldRequest.method, oldRequest.body)
            return@addInterceptor it.proceed(newRequestBuilder.build())
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()

    val retrofit=Retrofit.Builder()
        .baseUrl("https://api.nikohamrah.ir/api/v1/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

return retrofit.create(ApiService::class.java)

}