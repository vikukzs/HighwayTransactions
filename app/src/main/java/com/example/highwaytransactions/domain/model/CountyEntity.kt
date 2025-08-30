package com.example.highwaytransactions.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CountyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val typeId: String,
    val name: String
)
