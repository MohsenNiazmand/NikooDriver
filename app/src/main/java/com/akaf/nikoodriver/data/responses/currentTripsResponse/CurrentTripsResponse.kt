package com.akaf.nikoodriver.data.responses.currentTripsResponse

data class CurrentTripsResponse(
    val `data`: List<CurrentTripsData>?=null,
    val message: String
)