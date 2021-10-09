package com.akaf.nikoodriver.data.responses.verificationResponse


data class VerificationData(
    val driver: Driver,
    val refreshToken: String,
    val token: String,

    )