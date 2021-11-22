package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.responses.fillInfoResponse.driverUploadPhotoResponse.UploadPhotoDriverResponse
import com.akaf.nikoodriver.data.responses.fillInfoResponse.FillInfoResponse
import com.akaf.nikoodriver.data.responses.serviceTypeResponse.ServiceTypeResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response

interface FillInfoRepository {

    fun fillInfo(token:String,
                 firstName:String,
                 lastName:String,
                 nationalCode:String,
                 certificationCode:String,
                 photoUrl:String,
                 carPlaque:String,
                 carType:String,
                 carColor:String,
                 carInsuranceExpiration:String,
                 serviceId:Int
    ) : Single<Response<FillInfoResponse>>

    fun uploadDriverPhoto(title:String,driverPhoto:MultipartBody.Part) : Single<Response<UploadPhotoDriverResponse>>
    fun getServiceTypes():Single<Response<ServiceTypeResponse>>


}