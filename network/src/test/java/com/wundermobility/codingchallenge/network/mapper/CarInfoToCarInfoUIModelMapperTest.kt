package com.wundermobility.codingchallenge.network.mapper

import com.wundermobility.codingchallenge.network.model.CarInfo
import com.wundermobility.codingchallenge.network.model.CarInfoUIModel
import com.wundermobility.codingchallenge.network.testutil.shouldEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


/**
 * Created By Rafiqul Hasan
 */
@RunWith(MockitoJUnitRunner::class)
class CarInfoToCarInfoUIModelMapperTest{
    private lateinit var sut: CarInfoToCarInfoUIModelMapper

    @Before
    fun setUp() {
        sut = CarInfoToCarInfoUIModelMapper()
    }

    @Test
    fun `mapper should map CarInfo class to CarInfoUIModel class`() {
        val carInfo = CarInfo(
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
        val carUiModel = sut.map(carInfo)
        carUiModel shouldEqual CarInfoUIModel(2,"Anton",51.511998333333,7.4625316666667)
    }

    @Test
    fun `if title is null mapper should map licencePlate as title in CarInfoUIModel class`() {
        val carInfo = CarInfo(
            carId = 2,
            title = null,
            licencePlate = "162 NBT",
            address = "Kuhstraße 13",
            zipCode = "44137",
            city = "Dortmund",
            lat = 51.511998333333,
            lon = 7.4625316666667,
            reservationState = 0,
            vehicleTypeImageUrl = "https://wunderfleet-recruiting-dev.s3.eu-central-1.amazonaws.com/images/vehicle_type_image.png"
        )
        val carUiModel = sut.map(carInfo)
        carUiModel shouldEqual CarInfoUIModel(2,"162 NBT",51.511998333333,7.4625316666667)
    }

    @Test
    fun `if title is empty mapper should map licencePlate as title in CarInfoUIModel class`() {
        val carInfo = CarInfo(
            carId = 2,
            title = "",
            licencePlate = "162 NBT",
            address = "Kuhstraße 13",
            zipCode = "44137",
            city = "Dortmund",
            lat = 51.511998333333,
            lon = 7.4625316666667,
            reservationState = 0,
            vehicleTypeImageUrl = "https://wunderfleet-recruiting-dev.s3.eu-central-1.amazonaws.com/images/vehicle_type_image.png"
        )
        val carUiModel = sut.map(carInfo)
        carUiModel shouldEqual CarInfoUIModel(2,"162 NBT",51.511998333333,7.4625316666667)
    }
}