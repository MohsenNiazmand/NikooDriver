package com.akaf.nikoodriver.data.repositories.sources.offersHistory

import com.akaf.nikoodriver.data.responses.offersHistoryResponse.OffersHistoryResponse
import io.reactivex.Single
import retrofit2.Response

class OffersHistoryLocalDataSource : OffersHistoryDataSource {
    override fun offersHistory(page: Int): Single<Response<OffersHistoryResponse>> {
        TODO("Not yet implemented")
    }
}