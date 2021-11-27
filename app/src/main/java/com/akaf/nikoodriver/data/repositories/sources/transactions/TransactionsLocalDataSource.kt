package com.akaf.nikoodriver.data.repositories.sources.transactions

import com.akaf.nikoodriver.data.responses.transactionsResponse.TransactionsResponse
import io.reactivex.Single
import retrofit2.Response

class TransactionsLocalDataSource : TransactionsDataSource {
    override fun transactions(): Single<Response<TransactionsResponse>> {
        TODO("Not yet implemented")
    }
}