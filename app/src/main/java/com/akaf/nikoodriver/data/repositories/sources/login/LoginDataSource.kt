package com.akaf.nikoodriver.data.repositories.sources.login

import io.reactivex.Completable

interface LoginDataSource {

    fun login(phoneNumber:String) : Completable


}