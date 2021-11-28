package com.wundermobility.codingchallenge.network.datasource.carinfo

import com.wundermobility.codingchallenge.network.model.CarInfo
import com.wundermobility.codingchallenge.network.onResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */
class CarInfoApi @Inject constructor(private val service: CarInfoService) {
    fun getCarList(): Single<List<CarInfo>> {
        return service.getCarList()
            .onResponse()
    }

    fun getCarDetailsInfo(carId: Int): Single<CarInfo> {
        return service.getCarDetailsInfo(carId)
            .onResponse()
    }
}