package com.akaf.nikoodriver.data.responses.currentTripsResponse

data class Pickup(
    val createdAt: String,
    val id: Int,
    val ip: String,
    val location: LocationX,
    val trip_id: Int,
    val updatedAt: String
)