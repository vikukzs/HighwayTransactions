package com.example.highwaytransactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.highwaytransactions.R
import com.example.highwaytransactions.data.dto.OrderDto
import com.example.highwaytransactions.domain.repository.HighwayRepository
import com.example.highwaytransactions.ui.model.ChosenVignette
import com.example.highwaytransactions.ui.model.ConfirmationData
import com.example.highwaytransactions.ui.model.UiStates
import com.example.highwaytransactions.ui.model.VignetteTypeEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor(
    private val repository: HighwayRepository
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<UiStates>(UiStates.Loading)
    val uiState = _uiState.asStateFlow()
    private val _transactionSuccess =
        MutableSharedFlow<Boolean>()
    val transactionSuccess = _transactionSuccess

    private val _confirmationData =
        MutableStateFlow<ConfirmationData?>(null)
    val confirmationData = _confirmationData.asStateFlow()

    fun getConfirmationData(isCountryVignette: Boolean, chosenIds: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiStates.Loading
            val carData = repository.getVehicleData()
            val plateNumber = carData.plate
                .uppercase()
                .replace("-", " ")
            var vignetteTypeResId: Int
            var trxFee = 0
            val chosenVignettes = mutableListOf<ChosenVignette>()
            var sum = 0

            if (isCountryVignette) {
                chosenIds[0].let {
                    vignetteTypeResId = R.string.country_vignette_type
                    val vignette = repository.getVignetteByType(it)
                    val vName = VignetteTypeEnum.valueOf(it).displayText
                    chosenVignettes.add(
                        ChosenVignette(
                            name = vName,
                            price = vignette?.cost ?: 0
                        )
                    )
                    trxFee = vignette?.trxFee ?: 0
                    sum = vignette?.cost ?: 0
                }
            } else {
                vignetteTypeResId = R.string.county_vignette_type
                val vignette = repository.getVignetteByType("%${VignetteTypeEnum.YEAR.name}%")
                val countiesList = repository.getCountiesByTypes(chosenIds)
                chosenIds.forEach {
                    val county = countiesList.find { c -> c.typeId == it }
                    trxFee += vignette?.trxFee ?: 0
                    sum += vignette?.cost ?: 0
                    chosenVignettes.add(
                        ChosenVignette(
                            county?.name ?: "",
                            vignette?.cost ?: 0
                        )
                    )
                }
            }

            _confirmationData.value = ConfirmationData(
                plateNumber = plateNumber,
                vignetteTypeResId = vignetteTypeResId,
                chosenVignettes = chosenVignettes,
                trxFee = trxFee,
                priceSum = sum + trxFee
            )

            _uiState.value = UiStates.Success
        }
    }

    fun postOrder(chosenIds: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiStates.Loading
            val vignettes = repository.getAllVignettes()

            val orders = vignettes.flatMap { vignette ->
                vignette.vignetteType
                    .filter { it in chosenIds }
                    .map { matchedType ->
                        OrderDto(
                            type = matchedType,
                            category = vignette.vehicleCategory,
                            cost = vignette.sum
                        )
                    }
            }

            try {
                val orderResponse = repository.postOrder(orders)
                if (orderResponse.statusCode == "OK") {
                    _transactionSuccess.emit(true)
                } else {
                    _uiState.value = UiStates.Error(orderResponse.message ?: "Error")
                }
            } catch (e: Exception) {
                _uiState.value = UiStates.Error(e.message ?: "Error")
            }
        }
    }
}