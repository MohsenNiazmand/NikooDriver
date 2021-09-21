package com.example.nikoodriver.data.repositories.sources.fillInfo

import com.example.nikoodriver.data.fillInfoResponse.DriverUploadPhotoResponse.UploadPhotoDriverResponse
import com.example.nikoodriver.data.fillInfoResponse.FillInfoResponse
import com.example.nikoodriver.services.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response

class FillInfoRemoteDataSource(private val apiService: ApiService) : FillInfoDataSource {


    override fun fillInfo(
        token:String,
        firstName: String,
        lastName: String,
        nationalCode: String,
        certificationCode: String,
        photoUrl:String,
        carPlaque: String,
        carType: String,
        carColor: String,
        carInsuranceExpiration: String
    ): Single<Response<FillInfoResponse>> {
        return apiService.fillInfo(token,JsonObject().apply {
            addProperty("fname",firstName)
            addProperty("lname",lastName)
            addProperty("SSN",nationalCode)
            addProperty("certificationCode",certificationCode)
            addProperty("photoUrl",photoUrl)
            addProperty("carPlaque",carPlaque)
            addProperty("carType",carType)
            addProperty("carColor",carColor)
            addProperty("carInsuranceExpiration",carInsuranceExpiration)
        })
    }

    override fun uploadDriverPhoto(
        title: String,
        driverPhoto: MultipartBody.Part
    ): Single<Response<UploadPhotoDriverResponse>> {
        return apiService.uploadDriverPhoto(title,driverPhoto)
    }


}
