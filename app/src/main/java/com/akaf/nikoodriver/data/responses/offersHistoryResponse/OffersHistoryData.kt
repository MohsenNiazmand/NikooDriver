package com.akaf.nikoodriver.data.responses.offersHistoryResponse

data class OffersHistoryData(
    val docs: MutableList<OffersHistoryDoc>,
    val pages: Int,
    val total: Int
)