package com.akaf.nikoodriver.data.responses.currentTripsResponse

data class CurrentTripsData(
    val Destination: Destination,
    val Dropoff: Any?,
    val Pickup: Pickup?,
    val Source: Source,
    val TotalPassangers: Int,
    val User: User,
    val admin_id: Any,
    val cost: String,
    val coupon_id: Any,
    val createdAt: String,
    val date: Any,
    val description: String,
    val destinationCity: String,
    val destinationTrip_id: Any,
    val distance: String,
    val driverComment: Any,
    val driverRate: Any,
    val driver_id: Int,
    val endAt: Any,
    val exclusive: Any,
    val forOthers: List<ForOther>,
    val id: Int,
    val instant: Any,
    val nationalCode: Any,
    val options: Options,
    val payed: Int,
    val phoneNumber: String,
    val pickUpAt: String,
    val realStartAt: String,
    val researchAgainAt: Any,
    val returnAt: Any,
    val service_id: Int,
    val sourceCity: String,
    val startAt: String,
    val status: String,
    val time: Any,
    val timeRange_id: Any,
    val type: Any,
    val updatedAt: String,
    val userComment: Any,
    val userRate: Any,
    val user_id: Int
)