package com.wundermobility.codingchallenge.network.repository.carrent

import com.wundermobility.codingchallenge.network.model.CarRentResponse
import io.reactivex.rxjava3.core.Single

/**
 * Created By Rafiqul Hasan
 */
interface CarRentRepository {
    fun rentCar(carId: Int): Single<CarRentResponse>
}