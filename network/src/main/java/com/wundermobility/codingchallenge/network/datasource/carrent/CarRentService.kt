package com.wundermobility.codingchallenge.network.datasource.carrent

import com.wundermobility.codingchallenge.network.model.CarRentResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.*

/**
 * Created By Rafiqul Hasan
 */
interface CarRentService {
    companion object{
        const val CAR_RENT_URL = "https://4i96gtjfia.execute-api.eu-central-1.amazonaws.com/default/wunderfleet-rec"
    }
    @POST
    @FormUrlEncoded
    fun rentCar(
        @Url url: String,
        @Header("Authorization") token: String,
        @Field("carId") carId: Int
    ): Single<Response<CarRentResponse>>
}