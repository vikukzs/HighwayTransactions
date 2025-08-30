package com.example.highwaytransactions.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.highwaytransactions.domain.model.LocalizedString

@Entity
data class VehicleDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val internationalRegistrationCode: String,
    val type: String,
    val name: String,
    val plate: String,
    val country: LocalizedString,
    val vignetteType: String,
)
