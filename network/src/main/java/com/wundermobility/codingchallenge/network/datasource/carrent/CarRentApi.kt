package com.wundermobility.codingchallenge.network.datasource.carrent

import com.wundermobility.codingchallenge.network.model.CarRentRequestBody
import com.wundermobility.codingchallenge.network.model.CarRentResponse
import com.wundermobility.codingchallenge.network.onResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */
class CarRentApi @Inject constructor(private val service: CarRentService) {
    fun rentCar(
        url: String,
        token: String,
        carRentRequestBody: CarRentRequestBody
    ): Single<CarRentResponse> {
        return service.rentCar(url = url, token = token, carRentRequestBody)
            .onResponse()
    }
}