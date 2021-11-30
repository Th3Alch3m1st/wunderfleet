package com.wundermobility.codingchallenge.ui

import androidx.lifecycle.MutableLiveData
import com.wundermobility.codingchallenge.core.viewmodel.BaseViewModel
import com.wundermobility.codingchallenge.network.NetworkResult
import com.wundermobility.codingchallenge.network.model.CarInfo
import com.wundermobility.codingchallenge.network.model.CarInfoUIModel
import com.wundermobility.codingchallenge.network.repository.carinfo.CarInfoRepository
import com.wundermobility.codingchallenge.network.repository.carrent.CarRentRepository
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
    private val _carList = MutableLiveData<List<CarInfoUIModel>>()
    private val _carDetailsInfo = MutableLiveData<NetworkResult<CarInfo>>()


    val carList: MutableLiveData<List<CarInfoUIModel>>
        get() = _carList

    val carDetailsInfo: MutableLiveData<NetworkResult<CarInfo>>
        get() = _carDetailsInfo

    fun getCarList() {
        val disposable = carInfoRepository.getCarList()
            .withScheduler()
            .doOnSubscribe {
                showLoader.postValue(true)
            }.doAfterTerminate {
                showLoader.postValue(false)
            }.subscribe({ carList ->
                _carList.value = carList
            }, {
                toastMessage.value = it.message ?: "Something went wrong"
            })
        compositeDisposable.add(disposable)
    }

    fun getCarDetailsInfo(carID: Int) {
        val disposable = carInfoRepository.getCarDetailsInfo(carID)
            .withScheduler()
            .doOnSubscribe {
                showLoader.postValue(true)
            }.doAfterTerminate {
                showLoader.postValue(false)
            }.subscribe({ carDetailsInfo ->
                _carDetailsInfo.value = NetworkResult.Success(carDetailsInfo)
            }, {
                _carDetailsInfo.value = NetworkResult.Error(it)
            })
        compositeDisposable.add(disposable)
    }
}