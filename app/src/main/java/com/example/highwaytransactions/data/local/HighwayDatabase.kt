package com.example.highwaytransactions.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.highwaytransactions.domain.model.CountyEntity
import com.example.highwaytransactions.domain.model.VehicleCategoryEntity
import com.example.highwaytransactions.domain.model.VehicleDataEntity
import com.example.highwaytransactions.domain.model.VignetteEntity

@Database(
    entities = [
        VignetteEntity::class,
        VehicleDataEntity::class,
        VehicleCategoryEntity::class,
        CountyEntity::class
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converter::class)
abstract class HighwayDatabase: RoomDatabase() {
    abstract fun highwayDao(): HighwayDao
}