package com.example.highwaytransactions.data.remote

import com.example.highwaytransactions.data.dto.HighwayInfoResponseDto
import com.example.highwaytransactions.data.dto.OrderRequest
import com.example.highwaytransactions.data.dto.OrderResponseDto
import com.example.highwaytransactions.data.dto.VehicleDataResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HighwayApi {

    @GET("info")
    suspend fun getHighwayInfo(): HighwayInfoResponseDto

    @GET("vehicle")
    suspend fun getVehicleData(): VehicleDataResponseDto

    @POST("order")
    suspend fun postOrder(@Body orderRequest: OrderRequest): OrderResponseDto
}