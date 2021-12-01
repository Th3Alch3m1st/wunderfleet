package com.wundermobility.codingchallenge.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wundermobility.codingchallenge.core.viewmodel.BaseViewModel
import com.wundermobility.codingchallenge.network.NetworkResult
import com.wundermobility.codingchallenge.network.model.CarInfo
import com.wundermobility.codingchallenge.network.model.CarInfoUIModel
import com.wundermobility.codingchallenge.network.model.CarRentRequestBody
import com.wundermobility.codingchallenge.network.model.CarRentResponse
import com.wundermobility.codingchallenge.network.repository.carinfo.CarInfoRepository
import com.wundermobility.codingchallenge.network.repository.carrent.CarRentRepository
import com.wundermobility.codingchallenge.utils.SingleLiveEvent
import com.wundermobility.codingchallenge.utils.withScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val carInfoRepository: CarInfoRepository,
    private val carRentRepository: CarRentRepository
) : BaseViewModel() {
    private val _carList = MutableLiveData<NetworkResult<List<CarInfoUIModel>>>()
    private val _carDetailsInfo = SingleLiveEvent<NetworkResult<CarInfo>>()
    private val _carRentResponse = SingleLiveEvent<NetworkResult<CarRentResponse>>()


    val carList: LiveData<NetworkResult<List<CarInfoUIModel>>>
        get() = _carList

    val carDetailsInfo: LiveData<NetworkResult<CarInfo>>
        get() = _carDetailsInfo

    val carRentResponse: LiveData<NetworkResult<CarRentResponse>>
        get() = _carRentResponse

    fun getCarList() {
        val disposable = carInfoRepository.getCarList()
            .withScheduler()
            .doOnSubscribe {
                _showLoader.postValue(true)
            }.doAfterTerminate {
                _showLoader.postValue(false)
            }.subscribe({ carList ->
                _carList.value = NetworkResult.Success(carList)
            }, {
                _carList.value = NetworkResult.Error(it)
            })
        compositeDisposable.add(disposable)
    }

    fun getCarDetailsInfo(carID: Int) {
        val disposable = carInfoRepository.getCarDetailsInfo(carID)
            .withScheduler()
            .doOnSubscribe {
                _showLoader.postValue(true)
            }.doAfterTerminate {
                _showLoader.postValue(false)
            }.subscribe({ carDetailsInfo ->
                _carDetailsInfo.value = NetworkResult.Success(carDetailsInfo)
            }, {
                _carDetailsInfo.value = NetworkResult.Error(it)
            })
        compositeDisposable.add(disposable)
    }

    fun rentCar(requestBody: CarRentRequestBody) {
        val disposable = carRentRepository.rentCar(requestBody)
            .withScheduler()
            .doOnSubscribe {
                _showLoader.postValue(true)
            }.doAfterTerminate {
                _showLoader.postValue(false)
            }.subscribe({ carRentResponse ->
                _carRentResponse.value = NetworkResult.Success(carRentResponse)
            }, {
                _carRentResponse.value = NetworkResult.Error(it)
            })
        compositeDisposable.add(disposable)
    }
}