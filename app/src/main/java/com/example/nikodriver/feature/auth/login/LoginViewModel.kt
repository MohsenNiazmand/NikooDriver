package com.example.nikodriver.feature.auth.login

import com.example.nikodriver.common.NikoViewModel
import com.example.nikodriver.data.repositories.LoginRepository
import io.reactivex.Completable

class LoginViewModel(private val loginRepository: LoginRepository): NikoViewModel() {

 fun login(mobile:String): Completable {
     progressBarLiveData.value = true
    return loginRepository.login(mobile).doFinally{
        progressBarLiveData.postValue(false)
    }

 }

}