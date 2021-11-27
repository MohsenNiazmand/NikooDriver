package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.responses.transactionsResponse.TransactionsResponse
import io.reactivex.Single
import retrofit2.Response

interface TransactionsRepository {
    fun transactions(): Single<Response<TransactionsResponse>>

}