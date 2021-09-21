package com.example.nikoodriver.data.repositories

import com.example.nikoodriver.data.fillInfoResponse.DriverUploadPhotoResponse.UploadPhotoDriverResponse
import com.example.nikoodriver.data.fillInfoResponse.FillInfoResponse
import com.example.nikoodriver.data.repositories.sources.fillInfo.FillInfoDataSource
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
        carInsuranceExpiration: String
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
            carInsuranceExpiration
        )
    }

    override fun uploadDriverPhoto(
        title: String,
        driverPhoto: MultipartBody.Part
    ): Single<Response<UploadPhotoDriverResponse>> {
        return fillInfoRemoteDataSource.uploadDriverPhoto(title,driverPhoto)
    }


}