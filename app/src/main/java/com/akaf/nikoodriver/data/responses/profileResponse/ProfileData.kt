package com.akaf.nikoodriver.data.responses.profileResponse

data class ProfileData(
    val CarDriver: CarDriver,
    val Company: Company,
    val DriverDoc: Any,
    val SSN: String,
    val capacity: Int,
    val certificationCode: String,
    val createdAt: String,
    val credit: String,
    val currentTripsCount: Int,
    val expireAccount: String,
    val fname: String,
    val id: Int,
    val isOnline: Boolean,
    val isReady: Boolean,
    val lastLocation: LastLocation,
    val lastLoginAt: String,
    val lname: String,
    val onDuty: Boolean,
    val openTripsCount: Int,
    val phoneNumber: String,
    val photoUrl: String,
    val rate: Int,
    val rateCount: Int,
    val sex: String,
    val status: String
)