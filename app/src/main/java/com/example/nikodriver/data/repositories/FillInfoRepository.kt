package com.example.nikodriver.data.repositories

import io.reactivex.Completable
import okhttp3.MultipartBody
import java.io.File

interface FillInfoRepository {

    fun register(token:String,
                 firstName:String,
                 lastName:String,
                 nationalCode:String,
                 phoneNumber:String,
                 certificationCode:String,
                 photo:String,
                 carPlaque:String,
                 carType:String,
                 carColor:String,
                 carInsuranceExpiration:String
    ) : Completable


}