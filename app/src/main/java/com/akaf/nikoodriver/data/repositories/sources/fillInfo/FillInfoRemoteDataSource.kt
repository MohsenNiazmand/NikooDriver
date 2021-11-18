package com.akaf.nikoodriver.data.repositories.sources.fillInfo

import com.akaf.nikoodriver.data.responses.fillInfoResponse.driverUploadPhotoResponse.UploadPhotoDriverResponse
import com.akaf.nikoodriver.data.responses.fillInfoResponse.FillInfoResponse
import com.akaf.nikoodriver.data.responses.serviceTypeResponse.ServiceTypeResponse
import com.akaf.nikoodriver.services.ApiService
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
        carInsuranceExpiration: String,
        serviceId:String
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
            addProperty("service_id",serviceId)
        })
    }

    override fun uploadDriverPhoto(
        title: String,
        driverPhoto: MultipartBody.Part
    ): Single<Response<UploadPhotoDriverResponse>> {
        return apiService.uploadDriverPhoto(title,driverPhoto)
    }

    override fun getServiceTypes(): Single<Response<ServiceTypeResponse>> {
        return apiService.getServiceTypes()
    }


}
