package com.akaf.nikoodriver.data.repositories.sources.transactions

import com.akaf.nikoodriver.data.responses.transactionsResponse.TransactionsResponse
import com.akaf.nikoodriver.services.http.ApiService
import io.reactivex.Single
import retrofit2.Response

class TransactionsRemoteDataSource(val apiService: ApiService) :TransactionsDataSource {
    override fun transactions(): Single<Response<TransactionsResponse>> {
        return apiService.transactions()
    }
}