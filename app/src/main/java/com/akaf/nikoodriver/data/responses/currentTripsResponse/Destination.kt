package com.akaf.nikoodriver.data.responses.currentTripsResponse

data class Destination(
    val description: Any,
    val id: Int,
    val location: Location,
    val status: String
)