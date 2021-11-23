package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.responses.fillInfoResponse.driverUploadPhotoResponse.UploadPhotoDriverResponse
import com.akaf.nikoodriver.data.responses.fillInfoResponse.FillInfoResponse
import com.akaf.nikoodriver.data.repositories.sources.fillInfo.FillInfoDataSource
import com.akaf.nikoodriver.data.responses.serviceTypeResponse.ServiceTypeResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response

class FillInfoRepositoryImpl(
    val fillInfoLocalDataSource:FillInfoDataSource,
    val fillInfoRemoteDataSource:FillInfoDataSource
) : FillInfoRepository {
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
        return fillInfoRemoteDataSource.fillInfo(
            token,
            firstName,
            lastName,
            nationalCode,
            certificationCode,
            photoUrl,
            carPlaque,
            carType,
            carColor,
            carInsuranceExpiration,
            serviceId
        )
    }

    override fun uploadDriverPhoto(
        title: String,
        driverPhoto: MultipartBody.Part
    ): Single<Response<UploadPhotoDriverResponse>> {
        return fillInfoRemoteDataSource.uploadDriverPhoto(title,driverPhoto)
    }

    override fun getServiceTypes(): Single<Response<ServiceTypeResponse>> {
        return fillInfoRemoteDataSource.getServiceTypes()
    }


}