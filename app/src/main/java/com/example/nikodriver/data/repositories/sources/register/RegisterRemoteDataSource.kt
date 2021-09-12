package com.example.nikodriver.data.repositories.sources.register

import com.example.nikodriver.services.ApiService
import com.google.gson.JsonObject
import io.reactivex.Completable

class RegisterRemoteDataSource(private val apiService: ApiService) : RegisterDataSource {
    override fun register(
        token: String,
        firstName: String,
        lastName: String,
        nationalCode: String,
        phoneNumber: String,
        certificationCode: String,
        photo:String,
        carPlaque: String,
        carType: String,
        carColor: String,
        carInsuranceExpiration: String
    ): Completable = apiService.register(JsonObject().apply {
        addProperty("token",token)
        addProperty("fname",firstName)
        addProperty("lname",lastName)
        addProperty("SSN",nationalCode)
        addProperty("phoneNumber",phoneNumber)
        addProperty("certificationCode",certificationCode)
        addProperty("photo",photo)
        addProperty("carPlaque",carPlaque)
        addProperty("carType",carType)
        addProperty("carColor",carColor)
        addProperty("carInsuranceExpiration",carInsuranceExpiration)
    }).ignoreElement()
}
