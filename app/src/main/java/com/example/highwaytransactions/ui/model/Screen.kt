package com.example.highwaytransactions.ui.model

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object LandingScreen: Screen
    @Serializable
    data object CountyVignettesScreen: Screen
    @Serializable
    data class ConfirmationScreen(val chosenTypes: List<String>, val isCountryVignette: Boolean): Screen
    @Serializable
    data object SuccessScreen: Screen
}