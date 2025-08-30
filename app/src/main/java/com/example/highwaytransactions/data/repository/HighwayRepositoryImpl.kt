package com.example.highwaytransactions.data.repository

import com.example.highwaytransactions.data.dto.HighwayInfoPayloadDto
import com.example.highwaytransactions.data.dto.OrderDto
import com.example.highwaytransactions.data.dto.OrderRequest
import com.example.highwaytransactions.data.dto.OrderResponseDto
import com.example.highwaytransactions.data.dto.VehicleDataResponseDto
import com.example.highwaytransactions.data.local.HighwayDao
import com.example.highwaytransactions.data.remote.HighwayApi
import com.example.highwaytransactions.domain.model.CountyEntity
import com.example.highwaytransactions.domain.model.VehicleCategoryEntity
import com.example.highwaytransactions.domain.model.VehicleDataEntity
import com.example.highwaytransactions.domain.model.VignetteEntity
import com.example.highwaytransactions.domain.repository.HighwayRepository
import javax.inject.Inject

class HighwayRepositoryImpl @Inject constructor(
    private val api: HighwayApi,
    private val dao: HighwayDao
): HighwayRepository {

    override suspend fun fetchHighwayInfo(): HighwayInfoPayloadDto {
        return api.getHighwayInfo().payload
    }

    override suspend fun fetchVehicleData(): VehicleDataResponseDto {
        return api.getVehicleData()
    }

    override suspend fun postOrder(orders: List<OrderDto>): OrderResponseDto {
        return api.postOrder(OrderRequest(orders))
    }

    override suspend fun insertVehicleData(data: VehicleDataEntity) {
        dao.insertVehicleData(data)
    }

    override suspend fun insertCounties(data: List<CountyEntity>) {
        dao.insertCounties(data)
    }

    override suspend fun insertVignettes(data: List<VignetteEntity>) {
        dao.insertVignettes(data)
    }

    override suspend fun insertVehicleCategories(data: List<VehicleCategoryEntity>) {
        dao.insertVehicleCategories(data)
    }

    override suspend fun getAllVignettes(): List<VignetteEntity> {
        return dao.getAllVignettes()
    }

    override suspend fun getCountryVignettes(): List<VignetteEntity> {
        return dao.getCountryVignettes()
    }

    override suspend fun getVignetteByType(type: String): VignetteEntity? {
        return dao.getVignetteByType("%$type%")
    }

    override suspend fun getVehicleData(): VehicleDataEntity {
        return dao.getVehicleData()
    }

    override suspend fun getVehicleCategory(): VehicleCategoryEntity {
        return dao.getVehicleCategory()
    }

    override suspend fun getCounties(): List<CountyEntity> {
        return dao.getAllCounties()
    }

    override suspend fun getCountiesByTypes(types: List<String>): List<CountyEntity> {
        return dao.getCountiesByTypes(types)
    }

    override suspend fun clearDb() {
        dao.apply {
            deleteCountiesTable()
            deleteVignettesTable()
            deleteVehicleDataTable()
            deleteVehicleCategoriesTable()
        }
    }
}