package com.akaf.nikoodriver.data.responses.unAcceptedPassengers

data class UnAcceptedPassengersData(
    val Destination: Destination,
    val Source: Source,
    val TotalPassangers: Int,
    val cost: String,
    val description: String,
    val destinationCity: String,
    val endAt: Any,
    val forOthers: List<ForOther>,
    val id: Int,
    val options: Options,
    val phoneNumber: String,
    val returnAt: String,
    val sourceCity: String,
    val startAt: String,
    val status: String
)