package com.example.nikodriver.data.repositories.sources.register

import io.reactivex.Completable
import java.io.File

class RegisterLocalDataSource : RegisterDataSource {
    override fun register(
        token: String,
        firstName: String,
        lastName: String,
        nationalCode: String,
        phoneNumber: String,
        certificationCode: String,
        photo:String,
        carPlaque: String,
        carType: String,
        carColor: String,
        carInsuranceExpiration: String
    ): Completable {
        TODO("Not yet implemented")
    }

//    override fun upload(image: File): Completable {
//        TODO("Not yet implemented")
//    }
}