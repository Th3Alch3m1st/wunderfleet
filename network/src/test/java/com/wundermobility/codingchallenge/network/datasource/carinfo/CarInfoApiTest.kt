package com.wundermobility.codingchallenge.network.datasource.carinfo

import com.squareup.moshi.Moshi
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoServiceTest.Companion.CAR_DETAIL_INFO_HARDWARE_ID
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoServiceTest.Companion.CAR_DETAIL_INFO_TITLE
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoServiceTest.Companion.CAR_DETAIL_LICENCE_PLATE_NUMBER
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoServiceTest.Companion.CAR_LIST_SIZE
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoServiceTest.Companion.INDEX_0_CAR_TITLE
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoServiceTest.Companion.INDEX_0_LICENCE_PLATE_NUMBER
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoServiceTest.Companion.INDEX_9_CAR_TITLE
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoServiceTest.Companion.INDEX_9_LICENCE_PLATE_NUMBER
import com.wundermobility.codingchallenge.network.testutil.TestUtils.immediateExecutorService
import com.wundermobility.codingchallenge.network.testutil.TestUtils.mockResponse
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
class CarInfoApiTest {
    @get:Rule
    val mockWebServer = MockWebServer()

    private lateinit var mockService: CarInfoService

    private lateinit var sutAPI: CarInfoApi

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
            .create(CarInfoService::class.java)

        sutAPI = CarInfoApi(mockService)
    }

    @After
    fun shutDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `get car info and correct car list size returned`() {
        // Arrange
        mockWebServer.enqueue(mockResponse("carList.json"))

        // Act
        sutAPI.getCarList().test()
            .assertNoErrors()
            .assertComplete()
            .assertValue { list ->
                // Assert
                list.size shouldEqual CAR_LIST_SIZE
                return@assertValue true
            }.dispose()
    }

    @Test
    fun `get car info and correct car list data returned`() {
        // Arrange
        mockWebServer.enqueue(mockResponse("carList.json"))

        // Act
        sutAPI.getCarList().test()
            .assertNoErrors()
            .assertComplete()
            .assertValue { list ->
                // Assert
                list[0].title shouldEqual INDEX_0_CAR_TITLE
                list[0].licencePlate shouldEqual INDEX_0_LICENCE_PLATE_NUMBER

                list[9].title shouldEqual INDEX_9_CAR_TITLE
                list[9].licencePlate shouldEqual INDEX_9_LICENCE_PLATE_NUMBER
                return@assertValue true
            }.dispose()
    }

    @Test
    fun `get car details info and correct car detail info returned`() {
        // Arrange
        mockWebServer.enqueue(mockResponse("carDetailInfo.json"))

        // Act
        sutAPI.getCarDetailsInfo(CarInfoServiceTest.CAR_ID).test()
            .assertNoErrors()
            .assertComplete()
            .assertValue { response ->
                // Assert
                response.title shouldEqual CAR_DETAIL_INFO_TITLE
                response.licencePlate shouldEqual CAR_DETAIL_LICENCE_PLATE_NUMBER
                response.hardwareId shouldEqual CAR_DETAIL_INFO_HARDWARE_ID
                return@assertValue true
            }.dispose()
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .dispatcher(Dispatcher(immediateExecutorService()))
            .retryOnConnectionFailure(true).build()
    }
}