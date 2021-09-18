package com.example.nikodriver.data.repositories.sources.fillInfo

import io.reactivex.Completable
import okhttp3.MultipartBody
import java.io.File

interface FillInfoDataSource {
    fun register(token:String,
                 firstName:String,
                 lastName:String,
                 nationalCode:String,
                 phoneNumber:String,
                 certificationCode:String,
                 photo: String,
                 carPlaque:String,
                 carType:String,
                 carColor:String,
                 carInsuranceExpiration:String
    ) : Completable

//    fun upload(image : File) : Completable


}