package com.example.highwaytransactions.data.dto

data class HighwayInfoResponseDto(
    val requestId: Long,
    val statusCode: String,
    val payload: HighwayInfoPayloadDto,
    val dataType: String
)
