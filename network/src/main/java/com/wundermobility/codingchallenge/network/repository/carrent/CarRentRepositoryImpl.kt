package com.wundermobility.codingchallenge.network.repository.carrent

import com.wundermobility.codingchallenge.network.BuildConfig
import com.wundermobility.codingchallenge.network.datasource.carrent.CarRentApi
import com.wundermobility.codingchallenge.network.datasource.carrent.CarRentService
import com.wundermobility.codingchallenge.network.model.CarRentResponse
import com.wundermobility.codingchallenge.network.onException
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */
class CarRentRepositoryImpl @Inject constructor(private val api: CarRentApi) : CarRentRepository {
    override fun rentCar(carId: Int): Single<CarRentResponse> {
        return api.rentCar(
            url = CarRentService.CAR_RENT_URL,
            token = "Bearer ${BuildConfig.AUTH_TOKEN}",
            carId = carId
        ).onException()
    }
}