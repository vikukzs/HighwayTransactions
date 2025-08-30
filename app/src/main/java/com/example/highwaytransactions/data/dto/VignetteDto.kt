package com.example.highwaytransactions.data.dto

data class VignetteDto(
    val vignetteType: List<String>,
    val vehicleCategory: String,
    val cost: Int,
    val trxFee: Int,
    val sum: Int
)