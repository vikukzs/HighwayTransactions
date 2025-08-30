package com.example.highwaytransactions.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.highwaytransactions.domain.model.LocalizedString

@Entity
data class VehicleCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String,
    val vignetteCategory: String,
    val name: LocalizedString
)
