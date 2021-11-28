package com.wundermobility.codingchallenge.network.datasource.carinfo

import com.wundermobility.codingchallenge.network.model.CarInfo
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created By Rafiqul Hasan
 */
interface CarInfoService {
    @GET("wunderfleet-recruiting-dev/cars.json")
    fun getCarList(): Single<Response<List<CarInfo>>>

    @GET("wunderfleet-recruiting-dev/cars/{carId}")
    fun getCarDetailsInfo(@Path("carId") carId: Int): Single<Response<CarInfo>>
}