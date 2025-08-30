package com.example.highwaytransactions.data.dto

data class HighwayInfoPayloadDto(
    val highwayVignettes: List<VignetteDto>,
    val vehicleCategories: List<VehicleCategoryDto>,
    val counties: List<CountyDto>,

    )
