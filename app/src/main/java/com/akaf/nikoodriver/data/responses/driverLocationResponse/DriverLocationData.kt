package com.akaf.nikoodriver.data.responses.driverLocationResponse

data class DriverLocationData(
    val id: Int,
    val lastLocation: LastLocation,
    val updatedAt: String
)