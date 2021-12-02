package com.wundermobility.codingchallenge.fakerepository

import com.wundermobility.codingchallenge.network.RequestException
import com.wundermobility.codingchallenge.network.model.CarInfo
import com.wundermobility.codingchallenge.network.model.CarInfoUIModel
import com.wundermobility.codingchallenge.network.repository.carinfo.CarInfoRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Singleton
class FakeCarInfoRepositoryImpl @Inject constructor() : CarInfoRepository {
    companion object {
        const val MSG_ERROR = "Invalid Token"
        const val MSG_EMPTY = "No Car Found"
    }

    var testEmptyResponse = false
    var testError = false
    private val carInfo = CarInfo(
        carId = 2,
        title = "Anton",
        licencePlate = "162 NBT",
        address = "Kuhstraße 13",
        zipCode = "44137",
        city = "Dortmund",
        lat = 51.511998333333,
        lon = 7.4625316666667,
        reservationState = 0,
        vehicleTypeImageUrl = "https://wunderfleet-recruiting-dev.s3.eu-central-1.amazonaws.com/images/vehicle_type_image.png"
    )

    override fun getCarList(): Single<List<CarInfoUIModel>> {
        when {
            testEmptyResponse -> {
                return Single.create { emitter ->
                    emitter.onSuccess(listOf())
                }
            }
            testError -> {
                return Single.create { emitter ->
                    emitter.onError(RequestException(message = MSG_ERROR))
                }
            }
            else -> {
                return Single.create { emitter ->
                    emitter.onSuccess(getFakeCarList())
                }
            }
        }
    }

    override fun getCarDetailsInfo(carId: Int): Single<CarInfo> {
        return if (testError) {
            Single.create { emitter ->
                emitter.onError(RequestException(message = MSG_ERROR))
            }
        } else {
            Single.create { emitter ->
                emitter.onSuccess(carInfo)

            }
        }
    }

    private fun getFakeCarList(): List<CarInfoUIModel> {
        val list = mutableListOf<CarInfoUIModel>()
        for (i in 0 until 10) {
            when {
                i % 5 == 0 -> {
                    list.add(CarInfoUIModel(1, "Manfred", 51.5156, 7.4647))
                }
                i % 5 == 1 -> {
                    list.add(CarInfoUIModel(2, "Anton", 51.511998333333, 7.4625316666667))
                }
                i % 5 == 2 -> {
                    list.add(CarInfoUIModel(5, "HILDE", 51.5173, 7.466))
                }
                i % 5 == 3 -> {
                    list.add(CarInfoUIModel(6, "Heidrun", 51.5173, 7.466))
                }
                else -> {
                    list.add(CarInfoUIModel(8, "123-ABC", 51.5173, 7.466))
                }
            }
        }
        return list
    }
}