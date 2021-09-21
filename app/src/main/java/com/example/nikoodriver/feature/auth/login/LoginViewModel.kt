package com.example.nikoodriver.feature.auth.login

import com.example.nikoodriver.common.NikoViewModel
import com.example.nikoodriver.data.repositories.LoginRepository
import io.reactivex.Completable

class LoginViewModel(private val loginRepository: LoginRepository): NikoViewModel() {

 fun login(mobile:String): Completable {
     progressBarLiveData.value = true
    return loginRepository.login(mobile).doFinally{
        progressBarLiveData.postValue(false)
    }

 }

}