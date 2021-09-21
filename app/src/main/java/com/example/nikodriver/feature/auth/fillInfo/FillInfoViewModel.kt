package com.example.nikodriver.feature.auth.fillInfo

import com.example.nikodriver.common.NikoViewModel
import com.example.nikodriver.data.fillInfoResponse.DriverUploadPhotoResponse.UploadPhotoDriverResponse
import com.example.nikodriver.data.fillInfoResponse.FillInfoResponse
import com.example.nikodriver.data.repositories.FillInfoRepository
import io.reactivex.Completable
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