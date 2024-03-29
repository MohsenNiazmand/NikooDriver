package com.akaf.nikoodriver.feature.auth.registering.fillInfo

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.common.NikoViewModel
import com.akaf.nikoodriver.data.responses.fillInfoResponse.driverUploadPhotoResponse.UploadPhotoDriverResponse
import com.akaf.nikoodriver.data.responses.fillInfoResponse.FillInfoResponse
import com.akaf.nikoodriver.data.repositories.FillInfoRepository
import com.akaf.nikoodriver.data.responses.serviceTypeResponse.ServiceTypeResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.Response
import timber.log.Timber


class FillInfoViewModel(val fillInfoRepository: FillInfoRepository) : NikoViewModel() {

    val serviceTypes=MutableLiveData<Response<ServiceTypeResponse>>()




    fun register(token:String,
                 firstName:String,
                 lastName:String,
                 nationalCode:String,
                 certificationCode:String,
                 photoUrl:String,
                 carPlaque:String,
                 carType:String,
                 carColor:String,
                 carInsuranceExpiration:String?,
                 serviceId:Int
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
            carInsuranceExpiration,
            serviceId).doFinally {
            progressBarLiveData.postValue(false)
        }
    }

    fun uploadDriverPhoto(title:String,driverPhoto:MultipartBody.Part) : Single<Response<UploadPhotoDriverResponse>>{
        progressBarLiveData.value = true
        return fillInfoRepository.uploadDriverPhoto(title,driverPhoto).doFinally{
            progressBarLiveData.postValue(false)
        }
    }


    fun getServiceTypes(vehicleType:String){
        progressBarLiveData.value = true
        fillInfoRepository.getServiceTypes(vehicleType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<ServiceTypeResponse>>(compositeDisposable){
                @SuppressLint("BinaryOperationInTimber")
                override fun onSuccess(t: Response<ServiceTypeResponse>) {
                    serviceTypes.value=t
                    progressBarLiveData.postValue(false)

                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    progressBarLiveData.postValue(false)

                }

            })
    }





}