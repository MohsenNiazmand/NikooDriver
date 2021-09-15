package com.example.nikodriver.data.repositories.sources.register

import com.example.nikodriver.services.ApiService
import com.google.gson.JsonObject
import io.reactivex.Completable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

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

//    override fun upload(image: File): Completable {
//        return apiService.register(MultipartBody.Part).apply {
//
//            val filePart = MultipartBody.Part.createFormData(
//                "file",
//                file.getName(),
//                RequestBody.create(MediaType.parse("image/*"), file)
//            )
//
//            val call: Call<MyResponse> = api.uploadAttachment(filePart)
//        }.ignoreElement()
//    }
}
