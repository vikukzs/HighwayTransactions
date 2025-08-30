package com.example.highwaytransactions.ui.model

data class ConfirmationData(
    val plateNumber: String,
    val vignetteTypeResId: Int,
    val chosenVignettes: List<ChosenVignette>,
    val trxFee: Int,
    val priceSum: Int
)
