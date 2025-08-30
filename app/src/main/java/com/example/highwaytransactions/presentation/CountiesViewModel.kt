package com.example.highwaytransactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.highwaytransactions.domain.repository.HighwayRepository
import com.example.highwaytransactions.ui.model.County
import com.example.highwaytransactions.ui.model.UiStates
import com.example.highwaytransactions.ui.model.VignetteTypeEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountiesViewModel @Inject constructor(
    private val repository: HighwayRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiStates>(UiStates.Loading)
    val uiState = _uiState.asStateFlow()

    private val _counties =
        MutableStateFlow<List<County>>(emptyList())
    val counties = _counties.asStateFlow()

    private val _selectedCost =
        MutableStateFlow<Int>(0)
    val selectedCost = _selectedCost.asStateFlow()

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiStates.Loading

            val countyVignette = repository.getVignetteByType("%${VignetteTypeEnum.YEAR.name}%")
            val countiesList = repository.getCounties()

            _counties.value = countiesList.map {
                County(
                    id = it.typeId,
                    name = it.name,
                    cost = countyVignette?.cost ?: 0
                )
            }
            _uiState.value = UiStates.Success
        }
    }

    fun toggleCounty(id: String) {
        _counties.update { list ->
            list.map {
                if (it.id == id) it.copy(isSelected = !it.isSelected) else it
            }
        }
    }

}