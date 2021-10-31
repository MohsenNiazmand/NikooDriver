package com.akaf.nikoodriver.data.responses.cancelTripResponse

data class CancelationData(
    val cancelationFee: Int,
    val cost: String,
    val date: String,
    val payed: Int,
    val refundable: Int
)