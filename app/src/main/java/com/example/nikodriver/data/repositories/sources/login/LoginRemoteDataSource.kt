package com.example.nikodriver.data.repositories.sources.login

import com.example.nikodriver.services.ApiService
import com.google.gson.JsonObject
import io.reactivex.Completable

class LoginRemoteDataSource(private val apiService: ApiService ): LoginDataSource {
    override fun login(phoneNumber:String): Completable = apiService.login(JsonObject().apply {
        addProperty("phoneNumber",phoneNumber)
    }).ignoreElement()
}