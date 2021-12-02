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

    val showLoader:SingleLiveEvent<Boolean>
        get() = _showLoader

    override fun onCleared() {
        super.onCleared()
        _showLoader.value = false
        compositeDisposable.dispose()
    }
}