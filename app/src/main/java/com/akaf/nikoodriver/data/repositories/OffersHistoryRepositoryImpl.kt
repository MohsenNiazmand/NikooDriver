package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.repositories.sources.offersHistory.OffersHistoryDataSource
import com.akaf.nikoodriver.data.responses.offersHistoryResponse.OffersHistoryResponse
import io.reactivex.Single
import retrofit2.Response

class OffersHistoryRepositoryImpl(
    private val offersHistoryRemoteDataSource:OffersHistoryDataSource,
    val offersHistoryLocalDataSource:OffersHistoryDataSource
):OffersHistoryRepository {
    override fun offersHistory(page: Int): Single<Response<OffersHistoryResponse>> {
        return offersHistoryRemoteDataSource.offersHistory(page)
    }


}