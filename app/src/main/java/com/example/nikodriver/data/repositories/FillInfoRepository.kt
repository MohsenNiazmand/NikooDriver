package com.example.nikodriver.data.repositories

import com.example.nikodriver.data.fillInfoResponse.DriverUploadPhotoResponse.UploadPhotoDriverResponse
import com.example.nikodriver.data.fillInfoResponse.FillInfoResponse
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response
import java.io.File

interface FillInfoRepository {

    fun register(firstName:String,
                 lastName:String,
                 nationalCode:String,
                 certificationCode:String,
                 photoUrl:String,
                 carPlaque:String,
                 carType:String,
                 carColor:String,
                 carInsuranceExpiration:String
    ) : Single<Response<FillInfoResponse>>

    fun uploadDriverPhoto(title:String,driverPhoto:MultipartBody.Part) : Single<Response<UploadPhotoDriverResponse>>


}