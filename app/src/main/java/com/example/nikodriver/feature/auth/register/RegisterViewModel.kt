package com.example.nikodriver.feature.auth.register

import com.example.nikodriver.common.NikoViewModel
import com.example.nikodriver.data.repositories.RegisterRepository
import io.reactivex.Completable

class RegisterViewModel(val registerRepository: RegisterRepository) : NikoViewModel() {


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
    ) : Completable {
        progressBarLiveData.value = true
        return registerRepository.register(
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
            carInsuranceExpiration).doFinally {
            progressBarLiveData.postValue(false)
        }
    }

}