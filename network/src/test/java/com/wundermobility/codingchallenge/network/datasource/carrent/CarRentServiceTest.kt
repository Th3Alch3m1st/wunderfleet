package com.wundermobility.codingchallenge.network.datasource.carrent

import com.squareup.moshi.Moshi
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
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created By Rafiqul Hasan
 */
@RunWith(JUnit4::class)
class CarRentServiceTest {
    companion object{
        const val CAR_ID = 1
        const val LICENCE_PLATE = "B-BA3048"
    }
    @get:Rule
    val mockWebServer = MockWebServer()

    private lateinit var sutService: CarRentService

    @Before
    fun setUp() {
        val moshi = Moshi.Builder()
            .build()

        sutService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(CarRentService::class.java)
    }

    @Test
    fun `request for rent car and car rent success`() {
        // Arrange
        mockWebServer.enqueue(TestUtils.mockResponse("rentCarSuccess.json"))

        // Act
        sutService.rentCar("default/wunderfleet-rec",token = "df7c313b47b7ef87c64c0f5f5c",carId = 1).test()
            .assertNoErrors()
            .assertComplete()
            .assertValue { response ->
                // Assert
                response.body()?.carId shouldEqual CAR_ID
                response.body()?.licencePlate shouldEqual LICENCE_PLATE
                return@assertValue true
            }
    }

    @After
    fun shutDown() {
        mockWebServer.shutdown()
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .dispatcher(Dispatcher(TestUtils.immediateExecutorService()))
            .retryOnConnectionFailure(true).build()
    }
}