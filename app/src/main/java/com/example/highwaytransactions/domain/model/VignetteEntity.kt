package com.example.highwaytransactions.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VignetteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val vignetteType: List<String>,
    val vehicleCategory: String,
    val cost: Int,
    val trxFee: Int,
    val sum: Int

)
