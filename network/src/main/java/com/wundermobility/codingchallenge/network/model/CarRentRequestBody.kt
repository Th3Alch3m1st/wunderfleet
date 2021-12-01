package com.wundermobility.codingchallenge.network.model

import com.squareup.moshi.JsonClass

/**
 * Created By Rafiqul Hasan
 */
@JsonClass(generateAdapter = true)
data class CarRentRequestBody(val carId: Int)