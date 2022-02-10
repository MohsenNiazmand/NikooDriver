package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.responses.offersHistoryResponse.OffersHistoryResponse
import io.reactivex.Single
import retrofit2.Response

interface OffersHistoryRepository {
    fun offersHistory(page:Int):Single<Response<OffersHistoryResponse>>
}