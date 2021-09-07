package com.example.nikodriver.data.repositories.sources

import io.reactivex.Completable

interface LoginDataSource {

    fun checkMobile(mobile:String) : Completable


}