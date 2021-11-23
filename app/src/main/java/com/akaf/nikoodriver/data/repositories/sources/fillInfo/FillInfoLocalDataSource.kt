package com.akaf.nikoodriver.data.repositories.sources.fillInfo

import com.akaf.nikoodriver.data.responses.fillInfoResponse.driverUploadPhotoResponse.UploadPhotoDriverResponse
import com.akaf.nikoodriver.data.responses.fillInfoResponse.FillInfoResponse
import com.akaf.nikoodriver.data.responses.serviceTypeResponse.ServiceTypeResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response


class FillInfoLocalDataSource : FillInfoDataSource {
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
        carInsuranceExpiration: String?,
        serviceId:Int
    ): Single<Response<FillInfoResponse>> {
        TODO("Not yet implemented")
    }

    override fun uploadDriverPhoto(
        title: String,
        driverPhoto: MultipartBody.Part
    ): Single<Response<UploadPhotoDriverResponse>> {
        TODO("Not yet implemented")
    }

    override fun getServiceTypes(): Single<Response<ServiceTypeResponse>> {
        TODO("Not yet implemented")
    }


}