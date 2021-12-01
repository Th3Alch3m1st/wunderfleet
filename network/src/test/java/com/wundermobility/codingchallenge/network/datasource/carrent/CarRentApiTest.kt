package com.wundermobility.codingchallenge.network.datasource.carrent

import com.squareup.moshi.Moshi
import com.wundermobility.codingchallenge.network.model.CarRentRequestBody
import com.wundermobility.codingchallenge.network.testutil.TestUtils
import com.wundermobility.codingchallenge.network.testutil.shouldEqual
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created By Rafiqul Hasan
 */
@RunWith(MockitoJUnitRunner::class)
class CarRentApiTest{
    @get:Rule
    val mockWebServer = MockWebServer()

    private lateinit var mockService: CarRentService

    private lateinit var sutAPI: CarRentApi

    @Before
    fun setup() {
        val moshi = Moshi.Builder()
            .build()

        mockService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(CarRentService::class.java)

        sutAPI = CarRentApi(mockService)
    }

    @After
    fun shutDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `request for rent car and car rent success`() {
        // Arrange
        mockWebServer.enqueue(TestUtils.mockResponse("rentCarSuccess.json"))

        // Act
        sutAPI.rentCar(url = "rent/car",token = "df7c313b47b7ef87c64c0f5f5c",
            CarRentRequestBody(CarRentServiceTest.CAR_ID)
        ).test()
            .assertNoErrors()
            .assertComplete()
            .assertValue { response ->
                // Assert
                response.carId shouldEqual CarRentServiceTest.CAR_ID
                response.licencePlate shouldEqual CarRentServiceTest.LICENCE_PLATE
                return@assertValue true
            }
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .dispatcher(Dispatcher(TestUtils.immediateExecutorService()))
            .retryOnConnectionFailure(true).build()
    }
}