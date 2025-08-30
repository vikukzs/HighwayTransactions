package com.example.highwaytransactions.ui.model

sealed interface UiStates {
    data object Loading: UiStates
    data object Success: UiStates
    data class Error(val message: String): UiStates
}