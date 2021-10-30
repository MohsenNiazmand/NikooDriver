package com.akaf.nikoodriver.data.responses.completeTripResponse

data class CompleteTripData(
    val User: User,
    val endAt: String,
    val id: Int,
    val options: Options,
    val pickUpAt: String,
    val realStartAt: String,
    val shareToken: String,
    val status: String,
    val updatedAt: String
)