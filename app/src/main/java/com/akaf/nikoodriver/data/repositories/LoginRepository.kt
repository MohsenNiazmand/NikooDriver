package com.akaf.nikoodriver.data.repositories

import io.reactivex.Completable

interface LoginRepository {

    fun login(phoneNumber:String) : Completable

}