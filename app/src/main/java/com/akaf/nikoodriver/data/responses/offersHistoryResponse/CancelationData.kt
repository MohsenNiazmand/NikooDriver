package com.akaf.nikoodriver.data.responses.offersHistoryResponse

data class CancelationData(
    val cancelLocation: CancelLocation,
    val cancelationFee: Int,
    val cost: Int,
    val date: String,
    val payed: Int,
    val reason: String,
    val refundable: Int
)