package com.wundermobility.codingchallenge.network.mapper

import com.wundermobility.codingchallenge.network.model.CarInfo
import com.wundermobility.codingchallenge.network.model.CarInfoUIModel

/**
 * Created By Rafiqul Hasan
 */
class CarInfoToCarInfoUIModelMapper() : Mapper<CarInfo, CarInfoUIModel> {
    override fun map(input: CarInfo): CarInfoUIModel {
        return CarInfoUIModel(
            input.carId,
            if (input.title.isNullOrEmpty()) input.licencePlate ?: "" else input.title,
            input.lat,
            input.lon
        )
    }
}