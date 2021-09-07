package com.example.nikodriver.data.repositories.sources

import com.example.nikodriver.services.ApiService
import com.google.gson.JsonObject
import io.reactivex.Completable

class LoginRemoteDataSource(private val apiService: ApiService ):LoginDataSource {
    override fun checkMobile(mobile:String): Completable = apiService.login(JsonObject().apply {
        addProperty("mobile",mobile)
    }).ignoreElement()
}