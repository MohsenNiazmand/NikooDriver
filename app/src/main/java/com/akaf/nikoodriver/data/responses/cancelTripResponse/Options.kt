package com.akaf.nikoodriver.data.responses.cancelTripResponse

data class Options(
    val cancelationData: CancelationData,
    val disposalMinutes: Int,
    val distance: String
)