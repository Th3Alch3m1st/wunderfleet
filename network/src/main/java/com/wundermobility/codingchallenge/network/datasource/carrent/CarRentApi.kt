package com.wundermobility.codingchallenge.network.datasource.carrent

import com.wundermobility.codingchallenge.network.model.CarRentResponse
import com.wundermobility.codingchallenge.network.onResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */
class CarRentApi @Inject constructor(private val service: CarRentService) {
    fun rentCar(token: String, carId: Int): Single<CarRentResponse> {
        return service.rentCar(token = token, carId = carId)
            .onResponse()
    }
}