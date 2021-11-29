package com.wundermobility.codingchallenge.network.repository.carinfo

import com.wundermobility.codingchallenge.network.RequestException
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoApi
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoServiceTest
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoServiceTest.Companion.CAR_ID
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoServiceTest.Companion.CAR_LIST_SIZE
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoServiceTest.Companion.INDEX_0_CAR_TITLE
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoServiceTest.Companion.INDEX_9_CAR_TITLE
import com.wundermobility.codingchallenge.network.mapper.CarInfoToCarInfoUIModelMapper
import com.wundermobility.codingchallenge.network.testutil.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

/**
 * Created By Rafiqul Hasan
 */
@RunWith(MockitoJUnitRunner::class)
class CarInfoRepositoryImplTest {
    companion object{

        const val INDEX_0_CAR_LAT = 51.5156
        const val INDEX_0_CAR_LON = 7.4647

        const val INDEX_9_CAR_LAT = 51.5155
        const val INDEX_9_CAR_LON = 7.46

        const val ERROR_MSG = "An unexpected error has occurred. Please check your network connection and try again."
    }

    @Mock
    lateinit var mockApi: CarInfoApi

    private lateinit var sutRepository: CarInfoRepositoryImpl

    @Before
    fun setUp() {
        sutRepository = CarInfoRepositoryImpl(mockApi, CarInfoToCarInfoUIModelMapper())
    }
    @Test
    fun `get car list and correct car list size returned`() {
        // Arrange
        successCarList()

        // Act
        sutRepository.getCarList().test()
            .assertNoErrors()
            .assertComplete()
            .assertValue {list->
                // Assert
                list.size shouldEqual CAR_LIST_SIZE
                return@assertValue true
            }
    }

    @Test
    fun `get car list and correct car list  to ui data model returned`() {
        // Arrange
        successCarList()

        // Act
        sutRepository.getCarList().test()
            .assertNoErrors()
            .assertComplete()
            .assertValue { list ->
                // Assert
                list[0].title shouldEqual INDEX_0_CAR_TITLE
                list[0].latitude shouldEqual INDEX_0_CAR_LAT
                list[0].longitude shouldEqual INDEX_0_CAR_LON

                list[9].title shouldEqual INDEX_9_CAR_TITLE
                list[9].latitude shouldEqual INDEX_9_CAR_LAT
                list[9].longitude shouldEqual INDEX_9_CAR_LON
                return@assertValue true
            }
    }

    @Test
    fun `get car details info and correct param pass, car details info data returned`() {
        // Arrange
        successCarDetailsInfo()
        val acInt = argumentCaptor<Int>()

        // Act
        sutRepository.getCarDetailsInfo(CAR_ID).test()
            .assertNoErrors()
            .assertComplete()
            .assertValue { carInfo ->
                // Assert
                carInfo.title shouldEqual CarInfoServiceTest.CAR_DETAIL_INFO_TITLE
                carInfo.licencePlate shouldEqual CarInfoServiceTest.CAR_DETAIL_LICENCE_PLATE_NUMBER
                carInfo.hardwareId shouldEqual CarInfoServiceTest.CAR_DETAIL_INFO_HARDWARE_ID
                return@assertValue true
            }

        // Verify
        verify(mockApi).getCarDetailsInfo(acInt.capture())
        acInt.value shouldEqual CAR_ID
    }

    @Test
    fun `get car list info and internet connection exception returned`() {
        // Arrange
        failureCarList()

        // Act
        sutRepository.getCarList().test()
            .assertError {exception->
                // Assert
                assertThat(exception).isInstanceOf(RequestException::class.java)
                val error = exception as RequestException
                error.message shouldEqual ERROR_MSG
                return@assertError true
            }
    }

    @Test
    fun `get car details info and internet connection exception returned`() {
        // Arrange
        failureCarDetailInfo()
        val acInt = argumentCaptor<Int>()

        // Act
        sutRepository.getCarDetailsInfo(CAR_ID).test()
            .assertError {exception->
                // Assert
                assertThat(exception).isInstanceOf(RequestException::class.java)
                val error = exception as RequestException
                error.message shouldEqual ERROR_MSG
                return@assertError true
            }

        // Verify
        verify(mockApi).getCarDetailsInfo(acInt.capture())
        acInt.value shouldEqual CAR_ID
    }


    private fun successCarList() {
        val testData = TestUtils.getCarListTestData("carList.json")
        mockApi.getCarList() returns testData
    }

    private fun failureCarList() {
        val exception = RequestException(message = ERROR_MSG)
        mockApi.getCarList() returnsException exception
    }

    private fun successCarDetailsInfo() {
        val testData = TestUtils.getCarDetailsInfoTestData("carDetailInfo.json")
        mockApi.getCarDetailsInfo(anyInt()) returns testData
    }

    private fun failureCarDetailInfo() {
        val exception = RequestException(message = ERROR_MSG)
        mockApi.getCarDetailsInfo(CAR_ID) returnsException exception
    }

}