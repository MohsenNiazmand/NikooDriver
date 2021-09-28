package com.akaf.nikoodriver.feature.auth.fillInfo

import com.akaf.nikoodriver.common.NikoViewModel
import com.akaf.nikoodriver.data.fillInfoResponse.DriverUploadPhotoResponse.UploadPhotoDriverResponse
import com.akaf.nikoodriver.data.fillInfoResponse.FillInfoResponse
import com.akaf.nikoodriver.data.repositories.FillInfoRepository
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response


class FillInfoViewModel(val fillInfoRepository: FillInfoRepository) : NikoViewModel() {


    fun register(token:String,
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
        return fillInfoRepository.fillInfo(
            token,
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