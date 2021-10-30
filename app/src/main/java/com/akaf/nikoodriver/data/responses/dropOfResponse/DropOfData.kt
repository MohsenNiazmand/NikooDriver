package com.akaf.nikoodriver.data.responses.dropOfResponse

data class DropOfData(
    val description: Any,
    val id: Int,
    val location: Location,
    val status: String,
    val updatedAt: String
)