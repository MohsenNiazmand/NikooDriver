package com.akaf.nikoodriver.data.responses.currentTripsResponse

data class CurrentTripsResponse(
    val `data`: List<CurrentTripsData>,
    val message: String
)