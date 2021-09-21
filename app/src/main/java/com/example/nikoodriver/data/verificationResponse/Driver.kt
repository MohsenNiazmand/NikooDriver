package com.example.nikoodriver.data.verificationResponse

data class Driver(
    val SSN: String,
    val certificationCode: Any,
    val company_id: Int,
    val createdAt: String,
    val credit: String,
    val expireAccount: String,
    val fcmToken: String,
    val fname: String,
    val id: Int,
    val isOnline: Boolean,
    val lastLocation: LastLocation,
    val lastLoginAt: String,
    val lname: String,
    val onDuty: Boolean,
    val phoneNumber: String,
    val photo_id: Any,
    val rate: Int,
    val rateCount: Int,
    val sex: String,
    val status: String,
    val updatedAt: String
)