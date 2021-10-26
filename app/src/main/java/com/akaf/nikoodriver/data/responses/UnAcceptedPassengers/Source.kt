package com.akaf.nikoodriver.data.responses.UnAcceptedPassengers

data class Source(
    val count: Int,
    val description: String,
    val id: Int,
    val location: LocationX,
    val status: String
)