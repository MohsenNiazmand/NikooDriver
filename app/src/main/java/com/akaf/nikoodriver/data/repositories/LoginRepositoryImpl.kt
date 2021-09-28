package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.repositories.sources.login.LoginDataSource
import io.reactivex.Completable

class LoginRepositoryImpl(
    private val remoteDataSource: LoginDataSource,
    val localDataSource: LoginDataSource
    ):LoginRepository {
    override fun login(phoneNumber:String): Completable {
        return remoteDataSource.login(phoneNumber)
    }


}