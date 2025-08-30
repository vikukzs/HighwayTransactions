package com.example.highwaytransactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.highwaytransactions.domain.model.CountyEntity
import com.example.highwaytransactions.domain.model.LocalizedString
import com.example.highwaytransactions.domain.model.VehicleCategoryEntity
import com.example.highwaytransactions.domain.model.VehicleDataEntity
import com.example.highwaytransactions.domain.model.VignetteEntity
import com.example.highwaytransactions.domain.repository.HighwayRepository
import com.example.highwaytransactions.ui.model.UiStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: HighwayRepository
): ViewModel() {
    private val _uiState =
        MutableStateFlow<UiStates>(UiStates.Loading)
    val uiState = _uiState
        .onStart { getDataFromApi() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            UiStates.Loading
        )

    fun getDataFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiStates.Loading
            try {
                repository.clearDb()
                val getHighwayInfo = async { repository.fetchHighwayInfo() }
                val getVehicleData = async { repository.fetchVehicleData() }

                val highwayInfoResponse = getHighwayInfo.await()
                val vehicleDataResponse = getVehicleData.await()

                val vehicleEntity = vehicleDataResponse.let {
                    VehicleDataEntity(
                        internationalRegistrationCode = it.internationalRegistrationCode,
                        type = it.type,
                        name = it.name,
                        plate = it.plate,
                        country = LocalizedString(
                            hu = it.country.hu,
                            en = it.country.en
                        ),
                        vignetteType = it.vignetteType
                    )
                }

                repository.insertVehicleData(vehicleEntity)

                val vignettes = highwayInfoResponse.highwayVignettes.map {
                    VignetteEntity(
                        vignetteType = it.vignetteType,
                        vehicleCategory = it.vehicleCategory,
                        cost = it.cost,
                        trxFee = it.trxFee,
                        sum = it.sum
                    )
                }

                repository.insertVignettes(vignettes)

                val vehicleCategories = highwayInfoResponse.vehicleCategories.map {
                    VehicleCategoryEntity(
                        category = it.category,
                        vignetteCategory = it.vignetteCategory,
                        name = LocalizedString(
                            hu = it.name.hu,
                            en = it.name.en
                        )
                    )
                }

                val counties = highwayInfoResponse.counties.map {
                    CountyEntity(
                        typeId = it.id,
                        name = it.name
                    )
                }
                repository.insertCounties(counties)

                repository.insertVehicleCategories(vehicleCategories)

                _uiState.value = UiStates.Success
            } catch (e: Exception) {
                _uiState.value = UiStates.Error(e.message ?: "Ismeretlen hiba")
            }
        }
    }
}