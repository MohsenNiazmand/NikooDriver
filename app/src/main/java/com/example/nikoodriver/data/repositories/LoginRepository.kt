package com.example.nikoodriver.data.repositories

import io.reactivex.Completable

interface LoginRepository {

    fun login(phoneNumber:String) : Completable

}