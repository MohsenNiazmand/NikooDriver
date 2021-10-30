package com.akaf.nikoodriver.data.responses.pickUpResponse

data class PickUpData(
    val count: Int,
    val description: String,
    val id: Int,
    val location: Location,
    val status: String,
    val updatedAt: String
)