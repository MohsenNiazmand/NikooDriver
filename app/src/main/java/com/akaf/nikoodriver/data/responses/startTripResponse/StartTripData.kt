package com.akaf.nikoodriver.data.responses.startTripResponse

data class StartTripData(
    val Driver: Driver,
    val TotalPassangers: Int,
    val admin_id: Any,
    val cost: String,
    val coupon_id: Any,
    val createdAt: String,
    val description: String,
    val destinationCity: String,
    val destinationTrip_id: Any,
    val driverComment: Any,
    val driverRate: Any,
    val driver_id: Int,
    val endAt: Any,
    val forOthers: List<ForOther>,
    val id: Int,
    val nationalCode: Any,
    val options: Options,
    val payed: Int,
    val phoneNumber: String,
    val pickUpAt: Any,
    val realStartAt: String,
    val returnAt: String,
    val service_id: Int,
    val sourceCity: String,
    val startAt: String,
    val status: String,
    val type: Any,
    val updatedAt: String,
    val userComment: Any,
    val userRate: Any,
    val user_id: Int
)