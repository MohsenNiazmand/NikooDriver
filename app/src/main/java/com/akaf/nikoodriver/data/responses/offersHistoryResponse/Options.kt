package com.akaf.nikoodriver.data.responses.offersHistoryResponse

data class Options(
    val cancelationData: CancelationData,
    val disposalMinutes: Int,
    val distance: Double,
    val stopHour: Int
)