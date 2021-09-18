package com.example.nikodriver.data.repositories.sources.fillInfo

import com.example.nikodriver.services.ApiService
import com.google.gson.JsonObject
import io.reactivex.Completable
import okhttp3.MultipartBody
import java.io.File

class FillInfoRemoteDataSource(private val apiService: ApiService) : FillInfoDataSource {


    override fun register(
        token: String,
        firstName: String,
        lastName: String,
        nationalCode: String,
        phoneNumber: String,
        certificationCode: String,
        photo: String,
        carPlaque: String,
        carType: String,
        carColor: String,
        carInsuranceExpiration: String
    ): Completable=apiService.register(token,firstName,lastName,nationalCode,certificationCode,carPlaque,carType,carColor,carInsuranceExpiration,photo).ignoreElement()



}
