package com.example.nikodriver.data.repositories

import com.example.nikodriver.data.repositories.sources.register.RegisterDataSource
import io.reactivex.Completable

class RegisterRepositoryImpl(
    val remoteDataSource:RegisterDataSource,
    val localDataSource:RegisterDataSource
) : RegisterRepository {
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
        return remoteDataSource.register(
            token,
            firstName,
            lastName,
            nationalCode,
            phoneNumber,
            certificationCode,
            photo,
            carPlaque,
            carType,
            carColor,
            carInsuranceExpiration
        )
    }


}