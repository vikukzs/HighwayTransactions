package com.example.highwaytransactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.highwaytransactions.domain.repository.HighwayRepository
import com.example.highwaytransactions.ui.model.UiStates
import com.example.highwaytransactions.ui.model.VehicleData
import com.example.highwaytransactions.ui.model.Vignette
import com.example.highwaytransactions.ui.model.VignetteTypeEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.get

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val repository: HighwayRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiStates>(UiStates.Loading)
    val uiState = _uiState.asStateFlow()

    private val _countryVignettes =
        MutableStateFlow<List<Vignette>>(emptyList())
    val countryVignettes = _countryVignettes.asStateFlow()

    private val _vehicleData =
        MutableStateFlow<VehicleData?>(null)
    val vehicleData = _vehicleData.asStateFlow()

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiStates.Loading

            try {
                val carData = repository.getVehicleData()
                val countryVignettes = repository.getCountryVignettes()

                _vehicleData.value = VehicleData(
                    carData.name,
                    carData.plate
                        .uppercase()
                        .replace("-", " ")
                )

                _countryVignettes.value = countryVignettes
                    .map {
                        Vignette(
                            vignetteType = it.vignetteType[0],
                            vignetteName = getVignetteName(
                                carData.vignetteType,
                                it.vignetteType[0]
                            ),
                            cost = it.cost,
                            trxFee = it.trxFee
                        )
                    }

                _uiState.value = UiStates.Success

            } catch (e: Exception) {
                _uiState.value = UiStates.Error(e.message ?: "Ismeretlen hiba")
            }
        }
    }

    private fun getVignetteName(
        carVignettType: String,
        vignetteId: String
    ): String {
        val text = VignetteTypeEnum.valueOf(vignetteId).displayText.lowercase()
        return "$carVignettType - $text"
    }

}