package com.wundermobility.codingchallenge.network.repository.carrent

import com.wundermobility.codingchallenge.network.RequestException
import com.wundermobility.codingchallenge.network.datasource.carrent.CarRentApi
import com.wundermobility.codingchallenge.network.datasource.carrent.CarRentService
import com.wundermobility.codingchallenge.network.datasource.carrent.CarRentServiceTest
import com.wundermobility.codingchallenge.network.model.CarRentRequestBody
import com.wundermobility.codingchallenge.network.repository.carinfo.CarInfoRepositoryImplTest
import com.wundermobility.codingchallenge.network.testutil.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify


/**
 * Created By Rafiqul Hasan
 */
@RunWith(MockitoJUnitRunner::class)
class CarRentRepositoryImplTest {

    @Mock
    lateinit var mockApi: CarRentApi

    private lateinit var sutRepository: CarRentRepositoryImpl

    @Before
    fun setUp() {
        sutRepository = CarRentRepositoryImpl(mockApi)
    }

    @Test
    fun `request for rent car and car rent success`() {
        // Arrange
        successCarRent()
        val acRequestBody = argumentCaptor<CarRentRequestBody>()
        val acString = argumentCaptor<String>()

        // Act
        sutRepository.rentCar(CarRentRequestBody(CarRentServiceTest.CAR_ID)).test()
            .assertNoErrors()
            .assertComplete()
            .assertValue { response ->
                // Assert
                response.carId shouldEqual CarRentServiceTest.CAR_ID
                response.licencePlate shouldEqual CarRentServiceTest.LICENCE_PLATE
                return@assertValue true
            }

        // Verify
        verify(mockApi).rentCar(acString.capture(), acString.capture(), acRequestBody.capture())
        acString.allValues[0] shouldEqual CarRentService.CAR_RENT_URL
        acString.allValues[1] shouldEqual "Bearer df7c313b47b7ef87c64c0f5f5cebd6086bbb0fa"
        acRequestBody.firstValue.carId shouldEqual CarRentServiceTest.CAR_ID
    }

    @Test
    fun `get car list info and internet connection exception returned`() {
        failureCarList()

        sutRepository.rentCar(CarRentRequestBody(CarRentServiceTest.CAR_ID)).test()
            .assertError { exception ->
                assertThat(exception).isInstanceOf(RequestException::class.java)

                val error = exception as RequestException
                error.message shouldEqual CarInfoRepositoryImplTest.ERROR_MSG
                return@assertError true
            }
    }

    private fun failureCarList() {
        val exception = RequestException(message = CarInfoRepositoryImplTest.ERROR_MSG)
        mockApi.rentCar(any(), any(), any()) returnsException exception
    }

    private fun successCarRent() {
        val testData = TestUtils.getCarRentSuccessTestData("rentCarSuccess.json")
        mockApi.rentCar(any(), any(), any()) returns testData
    }
}