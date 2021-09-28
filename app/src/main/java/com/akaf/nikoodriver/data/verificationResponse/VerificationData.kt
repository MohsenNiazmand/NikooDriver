package com.akaf.nikoodriver.data.verificationResponse

data class VerificationData(
    val driver: Driver,
    val refreshToken: String,
    val token: String,

)