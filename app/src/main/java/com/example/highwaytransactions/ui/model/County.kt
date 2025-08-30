package com.example.highwaytransactions.ui.model

data class County(
    val id: String,
    val name: String,
    val cost: Int,
    var isSelected: Boolean = false
)
