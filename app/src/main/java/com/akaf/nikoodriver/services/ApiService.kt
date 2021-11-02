package com.akaf.nikoodriver.services

import android.content.SharedPreferences
import com.akaf.nikoodriver.data.TokenContainer
import com.akaf.nikoodriver.data.responses.UnAcceptedPassengers.UnAcceptedPassengersResponse
import com.akaf.nikoodriver.data.responses.completeTripResponse.CompleteTripResponse
import com.akaf.nikoodriver.data.responses.currentTripsResponse.CurrentTripsResponse
import com.akaf.nikoodriver.data.responses.driverLocationResponse.DriverLocationResponse
import com.akaf.nikoodriver.data.responses.dropOfResponse.DropOfResponse
import com.akaf.nikoodriver.data.responses.emptySeatsResponse.EmptySeatsResponse
import com.akaf.nikoodriver.data.responses.fcmResponse.FcmResponse
import com.akaf.nikoodriver.data.responses.fillInfoResponse.driverUploadPhotoResponse.UploadPhotoDriverResponse
import com.akaf.nikoodriver.data.responses.fillInfoResponse.FillInfoResponse
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.akaf.nikoodriver.data.responses.loginResponse.LoginResponse
import com.akaf.nikoodriver.data.responses.offerResponse.accept.AcceptOfferResponse
import com.akaf.nikoodriver.data.responses.offerResponse.reject.RejectOfferResponse
import com.akaf.nikoodriver.data.responses.pickUpResponse.PickUpResponse
import com.akaf.nikoodriver.data.responses.profileResponse.ProfileResponse
import com.akaf.nikoodriver.data.responses.refreshTokenResponse.RefreshTokenResponse
import com.akaf.nikoodriver.data.responses.startTripResponse.StartTripResponse
import com.akaf.nikoodriver.data.responses.submitDocsResponse.SubmitDocsResponse
import com.akaf.nikoodriver.data.responses.updateResponse.UpdateResponse
import com.akaf.nikoodriver.data.responses.uploadDocResponse.UploadDocResponse
import com.akaf.nikoodriver.data.responses.verificationResponse.VerificationResponse
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import timber.log.Timber
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
    fun refreshToken(@Body jsonObject: JsonObject): Single<Response<RefreshTokenResponse>>

    @Multipart
    @POST("auth/driver/docs")
    fun uploadDoc(@Part("title") title:String,
                  @Part doc: MultipartBody.Part) : Single<Response<UploadDocResponse>>

    @POST("auth/driver/docs/all")
    fun submitDocs(@Header("Authorization") token:String,@Body jsonObject: JsonObject) : Single<Response<SubmitDocsResponse>>

    @POST("driver/profile/fcm")
    fun sendFcmToken(@Body jsonObject: JsonObject):Single<Response<FcmResponse>>

    @POST("driver/profile/location")
    fun sendLocation(@Body sendLocation: SendLocation):Single<Response<DriverLocationResponse>>

    @PUT("driver/profile/capacity")
    @FormUrlEncoded
    fun setEmptySeats(@Field("capacity") emptySeats:Int,@Field("isReady") isReady:Boolean):Single<Response<EmptySeatsResponse>>


    @POST("driver/trips/{trip_id}/offer")
    @FormUrlEncoded
    fun acceptTrip(@Path("trip_id") tripId:Int,@Field("cost") cost:Int):Single<Response<AcceptOfferResponse>>



    @POST("driver/trips/reject")
    @FormUrlEncoded
    fun rejectTrip(@Field("trip_id") tripId:Int):Single<Response<RejectOfferResponse>>

    @GET("driver/trips/open?page=1")
    fun unAcceptedPassengers(): Single<Response<UnAcceptedPassengersResponse>>


    @GET("driver/trips/current")
    fun currentTrips():Single<Response<CurrentTripsResponse>>

    @PUT("driver/trips/{trip_id}/start")
    fun startTrip(@Path("trip_id") tripId:Int):Single<Response<StartTripResponse>>

    @PUT("driver/trips/{trip_id}/{source_id}")
    fun pickUp(@Path("trip_id") tripId: Int,@Path("source_id") sourceId:Int,@Query("location[0]") location0:Double,@Query("location[1]") location1:Double):Single<Response<PickUpResponse>>

    @DELETE("driver/trips/{trip_id}/{source_id}")
    fun dropOf(@Path("trip_id") tripId: Int,@Path("source_id") sourceId:Int,@Query("location[0]") location0:Double,@Query("location[1]") location1:Double):Single<Response<DropOfResponse>>

    @PUT("driver/trips/{trip_id}/complete")
    fun completeTrip(@Path("trip_id") tripId: Int):Single<Response<CompleteTripResponse>>

    @GET("driver/profile")
    fun getProfile():Single<Response<ProfileResponse>>

    @DELETE("driver//trips/{trip_id}")
    fun cancelTrip(@Path("trip_id") tripId: Int):Completable

    @POST("app-version")
    @FormUrlEncoded
    fun update(
        @Field("type") type: String,
        @Field("platform") platform: String,
        @Field("version") version: String
    ): Single<Response<UpdateResponse>>

}


fun createApiServiceInstance(sharedPreferences: SharedPreferences):ApiService{



    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor {
            val oldRequest = it.request()
            val newRequestBuilder = oldRequest.newBuilder()
            val token=sharedPreferences.getString("token", null)
            if (token != null)
                newRequestBuilder.addHeader("Authorization", "Bearer ${token}")
                Timber.i("TOKENI apiService: "+TokenContainer.token.toString())
            newRequestBuilder.addHeader("Accept", "application/json")
            newRequestBuilder.method(oldRequest.method(),oldRequest.body())
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