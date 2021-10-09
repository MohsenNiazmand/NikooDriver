package com.akaf.nikoodriver.data.responses.driverLocationResponse

data class LastLocation(
    val coordinates: List<Double>,
    val type: String
)