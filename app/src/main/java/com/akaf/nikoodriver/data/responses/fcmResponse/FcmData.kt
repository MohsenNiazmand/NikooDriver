package com.akaf.nikoodriver.data.responses.fcmResponse

data class FcmData(
    val SSN: String,
    val credit: String,
    val expireAccount: String,
    val fcmToken: String,
    val fname: String,
    val id: Int,
    val isOnline: Boolean,
    val lname: String,
    val onDuty: Boolean,
    val phoneNumber: String,
    val photoUrl: String,
    val sex: String,
    val status: String
)