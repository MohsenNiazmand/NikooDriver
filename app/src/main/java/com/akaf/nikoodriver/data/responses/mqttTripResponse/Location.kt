package com.akaf.nikoodriver.data.responses.mqttTripResponse

data class Location(
    val coordinates: List<Double>,
    val type: String
)