package com.wundermobility.codingchallenge.core.viewmodel

import androidx.lifecycle.ViewModel
import com.wundermobility.codingchallenge.utils.SingleLiveEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Created by Rafiqul Hasan
 */
abstract class BaseViewModel : ViewModel() {

    protected var compositeDisposable = CompositeDisposable()
    protected val _showLoader by lazy { SingleLiveEvent<Boolean>() }
    protected val _toastMessage by lazy { SingleLiveEvent<String>() }

    val toastMessage : SingleLiveEvent<String>
        get() = _toastMessage

    val showLoader:SingleLiveEvent<Boolean>
        get() = _showLoader

    override fun onCleared() {
        super.onCleared()
        _showLoader.value = false
        _toastMessage.postValue(null)
        compositeDisposable.dispose()
    }
}