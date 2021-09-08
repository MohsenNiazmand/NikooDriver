package com.example.nikodriver.data.repositories

import com.example.nikodriver.data.repositories.sources.LoginDataSource
import com.example.nikodriver.data.repositories.sources.LoginLocalDataSource
import io.reactivex.Completable

class LoginRepositoryImpl(
    private val remoteDataSource:LoginDataSource,
    val localDataSource: LoginLocalDataSource
    ):LoginRepository {
    override fun login(mobile:String): Completable {
        return remoteDataSource.login(mobile)
    }
}