package com.example.nikoodriver.data

import timber.log.Timber
//it saves our tokens
object TokenContainer {

    var token: String? = null
        private set
    var refreshToken: String? = null
        private set

    fun update(token: String?, refreshToken: String?) {
        Timber.i("Access Token-> ${token?.substring(0, 10)}, Refresh Token-> $refreshToken")
        this.token = token
        this.refreshToken = refreshToken
    }

}