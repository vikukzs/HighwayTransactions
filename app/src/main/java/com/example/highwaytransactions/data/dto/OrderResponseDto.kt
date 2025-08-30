package com.example.highwaytransactions.data.dto

data class OrderResponseDto(
    val statusCode: String,
    val message: String?,
    val receivedOrders: List<OrderDto>?
)
