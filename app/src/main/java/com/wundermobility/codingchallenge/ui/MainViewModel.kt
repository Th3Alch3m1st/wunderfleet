package com.wundermobility.codingchallenge.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _compositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        super.onCleared()
        _compositeDisposable.dispose()
    }
}