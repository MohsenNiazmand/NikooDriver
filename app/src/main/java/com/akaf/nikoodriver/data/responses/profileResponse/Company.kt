package com.akaf.nikoodriver.data.responses.profileResponse

data class Company(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val photoUrl: Any,
    val tripTypes: List<String>
)