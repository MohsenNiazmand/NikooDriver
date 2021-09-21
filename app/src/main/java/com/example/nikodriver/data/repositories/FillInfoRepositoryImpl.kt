package com.example.nikodriver.data.repositories

import com.example.nikodriver.data.fillInfoResponse.DriverUploadPhotoResponse.UploadPhotoDriverResponse
import com.example.nikodriver.data.fillInfoResponse.FillInfoResponse
import com.example.nikodriver.data.repositories.sources.fillInfo.FillInfoDataSource
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response

class FillInfoRepositoryImpl(
    val fillInfoLocalDataSource:FillInfoDataSource,
    val fillInfoRemoteDataSource:FillInfoDataSource
) : FillInfoRepository {
    override fun register(
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
        return fillInfoRemoteDataSource.register(
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