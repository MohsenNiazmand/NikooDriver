package com.akaf.nikoodriver.data.responses.UnAcceptedPassengers

data class UnAcceptedPassengersResponse(
    val `data`: List<UnAcceptedPassengersData>?=null,
    val message: String
)