package com.akaf.nikoodriver.data.repositories.sources.login

import com.akaf.nikoodriver.services.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Completable

class LoginRemoteDataSource(private val apiService: ApiService): LoginDataSource {
    override fun login(phoneNumber:String): Completable = apiService.login(JsonObject().apply {
        addProperty("phoneNumber",phoneNumber)
    }).ignoreElement()
}