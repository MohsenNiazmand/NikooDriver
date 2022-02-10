package com.akaf.nikoodriver.data.responses.offersHistoryResponse

data class OffersHistoryDoc(
    val Destination: Destination,
    val Dropoff: Any,
    val Pickup: Any,
    val Source: Source,
    val TotalPassangers: Int,
    val User: User,
    val cost: Int,
    val description: String,
    val destinationCity: String,
    val endAt: Any,
    val forOthers: List<ForOther>,
    val id: Int,
    val options: Options,
    val returnAt: Any,
    val sourceCity: String,
    val startAt: String?,
    val status: String
)