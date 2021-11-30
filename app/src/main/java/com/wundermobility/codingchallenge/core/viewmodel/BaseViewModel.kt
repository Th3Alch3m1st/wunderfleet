package com.wundermobility.codingchallenge.core.viewmodel

import androidx.lifecycle.ViewModel
import com.wundermobility.codingchallenge.utils.SingleLiveEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Created by Rafiqul Hasan
 */
abstract class BaseViewModel : ViewModel() {

    protected var compositeDisposable = CompositeDisposable()
    val showLoader by lazy { SingleLiveEvent<Boolean>() }
    val toastMessage by lazy { SingleLiveEvent<String>() }

    override fun onCleared() {
        super.onCleared()
        showLoader.value = false
        toastMessage.postValue(null)
        compositeDisposable.dispose()
    }
}