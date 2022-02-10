package com.akaf.nikoodriver.data.repositories.sources.offersHistory

import com.akaf.nikoodriver.data.responses.offersHistoryResponse.OffersHistoryResponse
import com.akaf.nikoodriver.services.http.ApiService
import io.reactivex.Single
import retrofit2.Response

class OffersHistoryRemoteDataSource(val apiService: ApiService) : OffersHistoryDataSource {
    override fun offersHistory(page: Int): Single<Response<OffersHistoryResponse>> {
       return apiService.offersHistory(page)
    }
}