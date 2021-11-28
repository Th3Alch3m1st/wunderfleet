package com.wundermobility.codingchallenge.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CarInfo(

    val zipCode: String? = null,

    val vehicleTypeId: Int? = null,

    val fuelLevel: Int? = null,

    val address: String? = null,

    val reservationState: Int? = null,

    val distance: String? = null,

    val vehicleStateId: Int? = null,

    val city: String? = null,

    val licencePlate: String? = null,

    val lon: Double,

    val title: String? = null,

    val pricingTime: String? = null,

    val carId: Int,

    val isClean: Boolean? = null,

    val pricingParking: String? = null,

    val locationId: Int? = null,

    val lat: Double,

    val isDamaged: Boolean? = null,

    val hardwareId: String? = null,

    val damageDescription: String? = null,

    val vehicleTypeImageUrl: String? = null,

    val isActivatedByHardware: Boolean? = null,
)
