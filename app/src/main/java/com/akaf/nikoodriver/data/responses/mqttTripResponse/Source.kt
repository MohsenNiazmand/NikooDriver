package com.akaf.nikoodriver.data.responses.mqttTripResponse

data class Source(
    val count: Int,
    val description: Any,
    val id: Int,
    val location: Location,
    val status: String
)