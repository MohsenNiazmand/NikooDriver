package com.akaf.nikoodriver.data.repositories.sources.offersHistory

import com.akaf.nikoodriver.data.responses.offersHistoryResponse.OffersHistoryResponse
import io.reactivex.Single
import retrofit2.Response

interface OffersHistoryDataSource {
    fun offersHistory(page:Int): Single<Response<OffersHistoryResponse>>

}