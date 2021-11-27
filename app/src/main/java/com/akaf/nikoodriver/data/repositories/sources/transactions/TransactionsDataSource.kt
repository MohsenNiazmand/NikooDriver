package com.akaf.nikoodriver.data.repositories.sources.transactions

import com.akaf.nikoodriver.data.responses.transactionsResponse.TransactionsResponse
import io.reactivex.Single
import retrofit2.Response

interface TransactionsDataSource {
    fun transactions(): Single<Response<TransactionsResponse>>

}