package com.wundermobility.codingchallenge.network.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created By Rafiqul Hasan
 */
@Parcelize
class CarInfoUIModel(
    val carID: Int,
    val title: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable