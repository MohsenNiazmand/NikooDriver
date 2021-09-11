package com.example.nikodriver.data.repositories

import com.example.nikodriver.data.repositories.sources.login.LoginDataSource
import com.example.nikodriver.data.repositories.sources.login.LoginLocalDataSource
import io.reactivex.Completable

class LoginRepositoryImpl(
    private val remoteDataSource: LoginDataSource,
    val localDataSource: LoginLocalDataSource
    ):LoginRepository {
    override fun login(phoneNumber:String): Completable {
        return remoteDataSource.login(phoneNumber)
    }
}