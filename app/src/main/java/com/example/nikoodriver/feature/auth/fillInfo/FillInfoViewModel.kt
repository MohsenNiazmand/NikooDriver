package com.example.nikoodriver.feature.auth.fillInfo

import com.example.nikoodriver.common.NikoViewModel
import com.example.nikoodriver.data.fillInfoResponse.DriverUploadPhotoResponse.UploadPhotoDriverResponse
import com.example.nikoodriver.data.fillInfoResponse.FillInfoResponse
import com.example.nikoodriver.data.repositories.FillInfoRepository
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response


class FillInfoViewModel(val fillInfoRepository: FillInfoRepository) : NikoViewModel() {


    fun register(
                 firstName:String,
                 lastName:String,
                 nationalCode:String,
                 certificationCode:String,
                 photoUrl:String,
                 carPlaque:String,
                 carType:String,
                 carColor:String,
                 carInsuranceExpiration:String
    ) : Single<Response<FillInfoResponse>> {
        progressBarLiveData.value = true
        return fillInfoRepository.register(
            firstName,
            lastName,
            nationalCode,
            certificationCode,
            photoUrl,
            carPlaque,
            carType,
            carColor,
            carInsuranceExpiration).doFinally {
            progressBarLiveData.postValue(false)
        }
    }

    fun uploadDriverPhoto(title:String,driverPhoto:MultipartBody.Part) : Single<Response<UploadPhotoDriverResponse>>{
        progressBarLiveData.value = true
        return fillInfoRepository.uploadDriverPhoto(title,driverPhoto).doFinally{
            progressBarLiveData.postValue(false)
        }
    }





}