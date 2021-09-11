package com.example.nikodriver.data.verificationResponse

data class VerificationData(
    val driver: Driver,
    val refreshToken: String,
    val token: String
)