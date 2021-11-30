package com.wundermobility.codingchallenge.ui

import androidx.lifecycle.MutableLiveData
import com.wundermobility.codingchallenge.core.viewmodel.BaseViewModel
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


    val carList: MutableLiveData<List<CarInfoUIModel>>
        get() = _carList

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
}