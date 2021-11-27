package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.repositories.sources.transactions.TransactionsDataSource
import com.akaf.nikoodriver.data.repositories.sources.transactions.TransactionsRemoteDataSource
import com.akaf.nikoodriver.data.responses.transactionsResponse.TransactionsResponse
import io.reactivex.Single
import retrofit2.Response

class TransactionsRepositoryImpl(
    val transactionsRemoteDataSource: TransactionsDataSource,
    val transactionsLocalDataSource: TransactionsDataSource
) :TransactionsRepository {
    override fun transactions(): Single<Response<TransactionsResponse>> {
        return transactionsRemoteDataSource.transactions()
    }
}