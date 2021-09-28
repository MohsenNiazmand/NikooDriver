package com.akaf.nikoodriver.data.fillInfoResponse

data class Driver(
    val SSN: String,
    val certificationCode: String,
    val company_id: Any,
    val createdAt: String,
    val credit: Any,
    val expireAccount: Any,
    val fcmToken: Any,
    val fname: String,
    val id: Int,
    val isOnline: Boolean,
    val lastLocation: Any,
    val lastLoginAt: String,
    val lname: String,
    val onDuty: Boolean,
    val phoneNumber: String,
    val photoUrl: String,
    val rate: Int,
    val rateCount: Int,
    val sex: String?=null,
    val status: String,
    val updatedAt: String
)