package com.akaf.nikoodriver.data.driverLocationResponse

data class DriverLocationData(
    val id: Int,
    val lastLocation: LastLocation,
    val updatedAt: String
)