package com.wundermobility.codingchallenge.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.wundermobility.codingchallenge.network.NetworkResult
import com.wundermobility.codingchallenge.network.RequestException
import com.wundermobility.codingchallenge.network.mapper.CarInfoToCarInfoUIModelMapper
import com.wundermobility.codingchallenge.network.model.CarInfo
import com.wundermobility.codingchallenge.network.model.CarInfoUIModel
import com.wundermobility.codingchallenge.network.model.CarRentRequestBody
import com.wundermobility.codingchallenge.network.model.CarRentResponse
import com.wundermobility.codingchallenge.network.repository.carinfo.CarInfoRepository
import com.wundermobility.codingchallenge.network.repository.carrent.CarRentRepository
import com.wundermobility.codingchallenge.testutil.TestUtils.getCarDetailsInfoTestData
import com.wundermobility.codingchallenge.testutil.TestUtils.getCarListTestData
import com.wundermobility.codingchallenge.testutil.TestUtils.getCarRentSuccessTestData
import com.wundermobility.codingchallenge.testutil.returns
import com.wundermobility.codingchallenge.testutil.returnsException
import com.wundermobility.codingchallenge.testutil.shouldEqual
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
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
class MainViewModelTest {
    companion object {
        const val CAR_LIST_SIZE = 10

        const val INDEX_0_CAR_ID = 1
        const val INDEX_0_CAR_LAT = 51.5156
        const val INDEX_0_CAR_LON = 7.4647

        const val INDEX_9_CAR_ID = 16
        const val INDEX_9_CAR_LAT = 51.5155
        const val INDEX_9_CAR_LON = 7.46

        const val CAR_ID = 2
        const val CAR_DETAIL_INFO_TITLE = "Anton"
        const val CAR_DETAIL_INFO_HARDWARE_ID = "NBSG4C7B406U63RE"
        const val CAR_DETAIL_LICENCE_PLATE_NUMBER = "162 NBT"

        const val RENT_CAR_ID = 1
        const val RENT_CAR_LICENCE_PLATE = "B-BA3048"

        const val ERROR_MSG =
            "An unexpected error has occurred. Please check your network connection and try again."
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockCarInfoRepository: CarInfoRepository

    @Mock
    lateinit var mockCarRentRepository: CarRentRepository

    @Mock
    private lateinit var mockCarListObserver: Observer<NetworkResult<List<CarInfoUIModel>>>

    @Mock
    private lateinit var mockCarDetailsObserver: Observer<NetworkResult<CarInfo>>

    @Mock
    private lateinit var mockCarRentResponse: Observer<NetworkResult<CarRentResponse>>


    private lateinit var sutViewModel: MainViewModel

    private lateinit var carListResponse: List<CarInfoUIModel>
    private lateinit var carDetailsResponse: CarInfo
    private lateinit var carRentResponse: CarRentResponse
    private val mapper by lazy { CarInfoToCarInfoUIModelMapper() }
    private val requestException by lazy { RequestException(message = ERROR_MSG) }

    @Before
    fun setup() {
        sutViewModel = MainViewModel(mockCarInfoRepository, mockCarRentRepository)

        carListResponse = getCarListTestData("carList.json").map(mapper::map)
        carDetailsResponse = getCarDetailsInfoTestData("carDetailInfo.json")
        carRentResponse = getCarRentSuccessTestData("rentCarSuccess.json")

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setErrorHandler { }
    }

    @Test
    fun `get car list and test correct data size is returned`() {
        // Arrange
        carListSuccess()

        // act
        sutViewModel.getCarList()
        sutViewModel.carList.observeForever(mockCarListObserver)

        // Assert
        Thread.sleep(100)
        val argumentCaptor = argumentCaptor<NetworkResult<List<CarInfoUIModel>>>()
        verify(mockCarListObserver).onChanged(argumentCaptor.capture())

        (argumentCaptor.firstValue as NetworkResult.Success).data.size shouldEqual CAR_LIST_SIZE
    }

    @Test
    fun `get car list and test correct data is returned`() {
        // Arrange
        carListSuccess()

        // act
        sutViewModel.getCarList()
        sutViewModel.carList.observeForever(mockCarListObserver)

        // Assert
        Thread.sleep(100)
        val argumentCaptor = argumentCaptor<NetworkResult<List<CarInfoUIModel>>>()
        verify(mockCarListObserver).onChanged(argumentCaptor.capture())

        val carList = (argumentCaptor.firstValue as NetworkResult.Success).data
        carList[0].carID shouldEqual INDEX_0_CAR_ID
        carList[0].latitude shouldEqual INDEX_0_CAR_LAT
        carList[0].longitude shouldEqual INDEX_0_CAR_LON

        carList[9].carID shouldEqual INDEX_9_CAR_ID
        carList[9].latitude shouldEqual INDEX_9_CAR_LAT
        carList[9].longitude shouldEqual INDEX_9_CAR_LON
    }

    @Test
    fun `get car list and if error happen correct error msg is returned`() {
        // Arrange
        carListFail()

        // act
        sutViewModel.getCarList()
        sutViewModel.carList.observeForever(mockCarListObserver)

        // Assert
        Thread.sleep(100)
        val argumentCaptor = argumentCaptor<NetworkResult<List<CarInfoUIModel>>>()
        verify(mockCarListObserver).onChanged(argumentCaptor.capture())

        (argumentCaptor.firstValue as NetworkResult.Error).exception shouldEqual requestException
        (argumentCaptor.firstValue as NetworkResult.Error).exception.message shouldEqual requestException.message

    }

    @Test
    fun `get car details and test correct param was pass`() {
        // Arrange
        carDetailsSuccess()

        // act
        sutViewModel.getCarDetailsInfo(CAR_ID)

        // Assert
        Thread.sleep(100)
        val argumentCaptor = argumentCaptor<Int>()
        verify(mockCarInfoRepository).getCarDetailsInfo(argumentCaptor.capture())

        argumentCaptor.firstValue shouldEqual CAR_ID
    }

    @Test
    fun `get car details and test correct correct data is returned`() {
        // Arrange
        carDetailsSuccess()

        // act
        sutViewModel.getCarDetailsInfo(CAR_ID)
        sutViewModel.carDetailsInfo.observeForever(mockCarDetailsObserver)
        // Assert
        Thread.sleep(100)
        val argumentCaptor = argumentCaptor<NetworkResult<CarInfo>>()
        verify(mockCarDetailsObserver).onChanged(argumentCaptor.capture())

        val carInfo = (argumentCaptor.firstValue as NetworkResult.Success).data
        carInfo.carId shouldEqual CAR_ID
        carInfo.title shouldEqual CAR_DETAIL_INFO_TITLE
        carInfo.hardwareId shouldEqual CAR_DETAIL_INFO_HARDWARE_ID
        carInfo.licencePlate shouldEqual CAR_DETAIL_LICENCE_PLATE_NUMBER
    }

    @Test
    fun `get car details and if error happen correct error msg is returned`() {
        // Arrange
        carDetailsFail()

        // act
        sutViewModel.getCarDetailsInfo(CAR_ID)
        sutViewModel.carDetailsInfo.observeForever(mockCarDetailsObserver)

        // Assert
        Thread.sleep(100)
        val argumentCaptor = argumentCaptor<NetworkResult<CarInfo>>()
        verify(mockCarDetailsObserver).onChanged(argumentCaptor.capture())

        (argumentCaptor.firstValue as NetworkResult.Error).exception shouldEqual requestException
        (argumentCaptor.firstValue as NetworkResult.Error).exception.message shouldEqual requestException.message

    }

    @Test
    fun `sent car rent request and test correct param was pass`() {
        // Arrange
        carRentSuccess()

        // act
        sutViewModel.rentCar(CarRentRequestBody(CAR_ID))

        // Assert
        Thread.sleep(100)
        val argumentCaptor = argumentCaptor<CarRentRequestBody>()
        verify(mockCarRentRepository).rentCar(argumentCaptor.capture())

        argumentCaptor.firstValue.carId shouldEqual CAR_ID
    }

    @Test
    fun `sent car rent request and test correct correct data is returned if succeed`() {
        // Arrange
        carRentSuccess()

        // act
        sutViewModel.rentCar(CarRentRequestBody(CAR_ID))
        sutViewModel.carRentResponse.observeForever(mockCarRentResponse)

        // Assert
        Thread.sleep(100)
        val argumentCaptor = argumentCaptor<NetworkResult<CarRentResponse>>()
        verify(mockCarRentResponse).onChanged(argumentCaptor.capture())

        val carInfo = (argumentCaptor.firstValue as NetworkResult.Success).data
        carInfo.carId shouldEqual RENT_CAR_ID
        carInfo.licencePlate shouldEqual RENT_CAR_LICENCE_PLATE
    }

    @Test
    fun `sent car rent request and if error happen correct error msg is returned`() {
        // Arrange
        carRentFail()

        // act
        sutViewModel.rentCar(CarRentRequestBody(CAR_ID))
        sutViewModel.carRentResponse.observeForever(mockCarRentResponse)

        // Assert
        Thread.sleep(100)
        val argumentCaptor = argumentCaptor<NetworkResult<CarRentResponse>>()
        verify(mockCarRentResponse).onChanged(argumentCaptor.capture())

        (argumentCaptor.firstValue as NetworkResult.Error).exception shouldEqual requestException
        (argumentCaptor.firstValue as NetworkResult.Error).exception.message shouldEqual requestException.message

    }


    private fun carListSuccess() {
        mockCarInfoRepository.getCarList() returns carListResponse
    }

    private fun carListFail() {
        mockCarInfoRepository.getCarList() returnsException requestException
    }

    private fun carDetailsSuccess() {
        mockCarInfoRepository.getCarDetailsInfo(any()) returns carDetailsResponse
    }

    private fun carDetailsFail() {
        mockCarInfoRepository.getCarDetailsInfo(any()) returnsException requestException
    }

    private fun carRentSuccess() {
        mockCarRentRepository.rentCar(any()) returns carRentResponse
    }

    private fun carRentFail() {
        mockCarRentRepository.rentCar(any()) returnsException requestException
    }
}