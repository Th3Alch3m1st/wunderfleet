package com.wundermobility.codingchallenge.network.repository.carinfo

import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoApi
import com.wundermobility.codingchallenge.network.mapper.Mapper
import com.wundermobility.codingchallenge.network.model.CarInfo
import com.wundermobility.codingchallenge.network.model.CarInfoUIModel
import com.wundermobility.codingchallenge.network.onException
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */
class CarInfoRepositoryImpl @Inject constructor(
    private val api: CarInfoApi,
    private val mapper: Mapper<CarInfo, CarInfoUIModel>
) : CarInfoRepository {
    override fun getCarList(): Single<List<CarInfoUIModel>> {
        return api.getCarList()
            .map {
                it.map(mapper::map)
            }.onException()
    }

    override fun getCarDetailsInfo(carId: Int): Single<CarInfo> {
        return api.getCarDetailsInfo(carId)
            .onException()
    }

}