package com.example.nikodriver.data.repositories

import io.reactivex.Completable

interface LoginRepository {

    fun checkMobile(mobile:String) : Completable

}