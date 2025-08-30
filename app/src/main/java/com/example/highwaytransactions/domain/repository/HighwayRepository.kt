package com.example.highwaytransactions.domain.repository

import com.example.highwaytransactions.data.dto.HighwayInfoPayloadDto
import com.example.highwaytransactions.data.dto.OrderDto
import com.example.highwaytransactions.data.dto.OrderResponseDto
import com.example.highwaytransactions.data.dto.VehicleDataResponseDto
import com.example.highwaytransactions.domain.model.CountyEntity
import com.example.highwaytransactions.domain.model.VehicleCategoryEntity
import com.example.highwaytransactions.domain.model.VehicleDataEntity
import com.example.highwaytransactions.domain.model.VignetteEntity


interface HighwayRepository {

    suspend fun fetchHighwayInfo(): HighwayInfoPayloadDto
    suspend fun fetchVehicleData(): VehicleDataResponseDto
    suspend fun postOrder(orders: List<OrderDto>): OrderResponseDto

    suspend fun insertVehicleData(data: VehicleDataEntity)
    suspend fun insertCounties(data: List<CountyEntity>)
    suspend fun insertVignettes(data: List<VignetteEntity>)
    suspend fun insertVehicleCategories(data: List<VehicleCategoryEntity>)

    suspend fun getAllVignettes(): List<VignetteEntity>
    suspend fun getCountryVignettes(): List<VignetteEntity>
    suspend fun getVignetteByType(type: String): VignetteEntity?
    suspend fun getVehicleData(): VehicleDataEntity
    suspend fun getVehicleCategory(): VehicleCategoryEntity
    suspend fun getCounties(): List<CountyEntity>
    suspend fun getCountiesByTypes(types: List<String>): List<CountyEntity>

    suspend fun clearDb()


}