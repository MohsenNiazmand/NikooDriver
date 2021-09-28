package com.akaf.nikoodriver.feature.auth.login

import com.akaf.nikoodriver.common.NikoViewModel
import com.akaf.nikoodriver.data.repositories.LoginRepository
import io.reactivex.Completable

class LoginViewModel(private val loginRepository: LoginRepository): NikoViewModel() {

 fun login(mobile:String): Completable {
     progressBarLiveData.value = true
    return loginRepository.login(mobile).doFinally{
        progressBarLiveData.postValue(false)
    }

 }

}