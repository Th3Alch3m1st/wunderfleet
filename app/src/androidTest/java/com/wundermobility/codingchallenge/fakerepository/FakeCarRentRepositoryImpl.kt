package com.wundermobility.codingchallenge.fakerepository

import com.wundermobility.codingchallenge.network.RequestException
import com.wundermobility.codingchallenge.network.model.CarRentRequestBody
import com.wundermobility.codingchallenge.network.model.CarRentResponse
import com.wundermobility.codingchallenge.network.repository.carrent.CarRentRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Singleton
class FakeCarRentRepositoryImpl @Inject constructor(): CarRentRepository {
    var testError = false
    override fun rentCar(carRentRequestBody: CarRentRequestBody): Single<CarRentResponse> {
        return if (testError) {
            Single.create { emitter ->
                emitter.onError(RequestException(message = FakeCarInfoRepositoryImpl.MSG_ERROR))
            }
        } else {
            Single.create { emitter ->
                emitter.onSuccess(
                    CarRentResponse(
                        reservationId = 76,
                        carId = 1,
                        cost = 0,
                        drivenDistance = 0,
                        licencePlate = "B-BA3048",
                        startAddress = "Uhlandstraße 142",
                        userId = 12,
                        isParkModeEnabled = false,
                        damageDescription = "",
                        fuelCardPin = "1234",
                        endTime = 1404778918,
                        startTime = 1404778018
                    )
                )
            }
        }
    }
}