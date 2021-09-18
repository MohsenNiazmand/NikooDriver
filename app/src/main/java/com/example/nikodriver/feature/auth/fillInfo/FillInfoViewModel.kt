package com.example.nikodriver.feature.auth.fillInfo

import com.example.nikodriver.common.NikoViewModel
import com.example.nikodriver.data.repositories.FillInfoRepository
import io.reactivex.Completable
import okhttp3.MultipartBody


class FillInfoViewModel(val fillInfoRepository: FillInfoRepository) : NikoViewModel() {


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
        return fillInfoRepository.register(
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