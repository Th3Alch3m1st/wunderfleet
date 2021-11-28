package com.wundermobility.codingchallenge.network.datasource.carinfo

import com.squareup.moshi.Moshi
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
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created By Rafiqul Hasan
 */
@RunWith(JUnit4::class)
class CarInfoServiceTest {
    companion object {
        const val CAR_LIST_SIZE = 10

        const val INDEX_0_CAR_NAME = "Manfred"
        const val INDEX_0_LICENCE_PLATE_NUMBER = "FBL 081"

        const val INDEX_9_CAR_NAME = "Fanny"
        const val INDEX_9_LICENCE_PLATE_NUMBER = "120-OSM"
    }

    @get:Rule
    val mockWebServer = MockWebServer()

    private lateinit var sutService: CarInfoService

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
            .create(CarInfoService::class.java)
    }

    @After
    fun shutDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `get car info and correct car info size returned`() {
        mockWebServer.enqueue(mockResponse("carList.json"))
        sutService.getCarList().test()
            .assertNoErrors()
            .assertComplete()
            .assertValue { response ->
                response.body()?.size shouldEqual CAR_LIST_SIZE
                return@assertValue true
            }
    }

    @Test
    fun `get car info and correct car info returned`() {
        mockWebServer.enqueue(mockResponse("carList.json"))
        sutService.getCarList().test()
            .assertNoErrors()
            .assertComplete()
            .assertValue { response ->
                response.body()?.get(0)?.title shouldEqual INDEX_0_CAR_NAME
                response.body()?.get(0)?.licencePlate shouldEqual INDEX_0_LICENCE_PLATE_NUMBER

                response.body()?.get(9)?.title shouldEqual INDEX_9_CAR_NAME
                response.body()?.get(9)?.licencePlate shouldEqual INDEX_9_LICENCE_PLATE_NUMBER
                return@assertValue true
            }
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .dispatcher(Dispatcher(immediateExecutorService()))
            .retryOnConnectionFailure(true).build()
    }
}