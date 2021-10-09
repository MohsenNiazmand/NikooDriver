package com.akaf.nikoodriver.data.responses.mqttTripResponse

data class Destination(
    val description: Any,
    val id: Int,
    val location: Location,
    val status: String
)