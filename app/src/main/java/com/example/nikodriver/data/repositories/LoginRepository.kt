package com.example.nikodriver.data.repositories

import io.reactivex.Completable

interface LoginRepository {

    fun login(phoneNumber:String) : Completable

}