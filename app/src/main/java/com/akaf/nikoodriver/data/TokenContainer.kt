package com.akaf.nikoodriver.data

import timber.log.Timber
//it saves our tokens
object TokenContainer {

    var token: String? = null
        private set
    var refreshToken: String? = null
        private set

    fun update(token: String?, refreshToken: String?) {
//        Timber.i("Access TOOKENI-> ${token?.substring(0, 10)}, Refresh TOOKENI-> $refreshToken")
        Timber.i("TokenContainer TOOKENI:%s", token)

        this.token = token
        this.refreshToken = refreshToken
    }

}