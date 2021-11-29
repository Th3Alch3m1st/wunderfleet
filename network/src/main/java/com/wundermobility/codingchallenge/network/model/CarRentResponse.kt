package com.wundermobility.codingchallenge.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CarRentResponse(

	val cost: Int? = null,

	val reservationId: Int? = null,

	val fuelCardPin: String? = null,

	val licencePlate: String? = null,

	val startAddress: String? = null,

	val isParkModeEnabled: Boolean? = null,

	val drivenDistance: Int? = null,

	val startTime: Int? = null,

	val endTime: Int? = null,

	val userId: Int? = null,

	val carId: Int,

	val damageDescription: String? = null
)
