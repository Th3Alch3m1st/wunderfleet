package com.wundermobility.codingchallenge.network.repository.carinfo

import com.wundermobility.codingchallenge.network.model.CarInfo
import com.wundermobility.codingchallenge.network.model.CarInfoUIModel
import io.reactivex.rxjava3.core.Single

/**
 * Created By Rafiqul Hasan
 */
interface CarInfoRepository {
    fun getCarList(): Single<List<CarInfoUIModel>>
    fun getCarDetailsInfo(carId: Int): Single<CarInfo>
}