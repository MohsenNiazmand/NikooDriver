package com.akaf.nikoodriver.data.responses.profileResponse

data class Service(
    val capacity: Int,
    val commissionRoad: Double,
    val commissionStation: Double,
    val createdAt: String,
    val description: String,
    val hasRoad: Boolean,
    val id: Int,
    val name: String,
    val photoUrl: String,
    val roadDistanceKM: Int,
    val stationDistanceKM: Int,
    val updatedAt: String
)