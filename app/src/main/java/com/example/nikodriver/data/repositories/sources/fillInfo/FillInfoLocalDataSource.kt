package com.example.nikodriver.data.repositories.sources.fillInfo

import com.example.nikodriver.data.fillInfoResponse.DriverUploadPhotoResponse.UploadPhotoDriverResponse
import com.example.nikodriver.data.fillInfoResponse.FillInfoResponse
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response


class FillInfoLocalDataSource : FillInfoDataSource {
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
        TODO("Not yet implemented")
    }

    override fun uploadDriverPhoto(
        title: String,
        driverPhoto: MultipartBody.Part
    ): Single<Response<UploadPhotoDriverResponse>> {
        TODO("Not yet implemented")
    }


}