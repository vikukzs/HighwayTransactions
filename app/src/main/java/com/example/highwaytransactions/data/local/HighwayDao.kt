package com.example.highwaytransactions.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.highwaytransactions.domain.model.CountyEntity
import com.example.highwaytransactions.domain.model.VehicleCategoryEntity
import com.example.highwaytransactions.domain.model.VehicleDataEntity
import com.example.highwaytransactions.domain.model.VignetteEntity

@Dao
interface HighwayDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertVehicleData(vehicleDataEntity: VehicleDataEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertVignettes(vignettes: List<VignetteEntity>)

    @Insert(onConflict = REPLACE)
    suspend fun insertCounties(counties: List<CountyEntity>)

    @Insert(onConflict = REPLACE)
    suspend fun insertVehicleCategories(vehicleCategories: List<VehicleCategoryEntity>)

    @Query("SELECT * FROM VignetteEntity")
    suspend fun getAllVignettes(): List<VignetteEntity>

    @Query("SELECT * FROM VignetteEntity WHERE vignetteType NOT LIKE '%YEAR%'")
    suspend fun getCountryVignettes(): List<VignetteEntity>

    @Query("SELECT * FROM VignetteEntity WHERE vignetteType LIKE :type LIMIT 1")
    suspend fun getVignetteByType(type: String): VignetteEntity

    @Query("SELECT * FROM CountyEntity")
    suspend fun getAllCounties(): List<CountyEntity>

    @Query("SELECT * FROM CountyEntity WHERE typeId IN (:types)")
    suspend fun getCountiesByTypes(types: List<String>): List<CountyEntity>

    @Query("SELECT * FROM VehicleDataEntity LIMIT 1")
    suspend fun getVehicleData(): VehicleDataEntity

    @Query("SELECT * FROM VehicleCategoryEntity LIMIT 1")
    suspend fun getVehicleCategory(): VehicleCategoryEntity

    @Query("DELETE FROM CountyEntity")
    suspend fun deleteCountiesTable()

    @Query("DELETE FROM VehicleCategoryEntity")
    suspend fun deleteVehicleCategoriesTable()

    @Query("DELETE FROM VehicleDataEntity")
    suspend fun deleteVehicleDataTable()

    @Query("DELETE FROM VignetteEntity")
    suspend fun deleteVignettesTable()


}