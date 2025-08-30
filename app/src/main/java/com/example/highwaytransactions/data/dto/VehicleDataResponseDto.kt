package com.example.highwaytransactions.data.dto

data class VehicleDataResponseDto(
    val requestId: Long,
    val statusCode: String,
    val internationalRegistrationCode: String,
    val type: String,
    val name: String,
    val plate: String,
    val country: CountryDto,
    val vignetteType: String,
)
