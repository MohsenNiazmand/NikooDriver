package com.akaf.nikoodriver.data.responses.profileResponse

data class CarDriver(
    val brand: String,
    val color: String,
    val id: Int,
    val insuranceExpiration: String,
    val plaque: String,
    val produceYear: Int,
    val type: String
)