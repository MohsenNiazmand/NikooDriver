package com.example.nikodriver.data.repositories.sources.register

import io.reactivex.Completable

interface RegisterDataSource {
    fun register(token:String,
                 firstName:String,
                 lastName:String,
                 nationalCode:String,
                 phoneNumber:String,
                 certificationCode:String,
                 photo:String,
                 carPlaque:String,
                 carType:String,
                 carColor:String,
                 carInsuranceExpiration:String
    ) : Completable

}