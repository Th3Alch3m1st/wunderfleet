package com.wundermobility.codingchallenge.utils

/**
 * Created By Rafiqul Hasan on 25/9/21
 * Brain Station 23
 */
sealed class NetworkState {
    data class Loading(val isLoading: Boolean = false) : NetworkState()
    data class Error(var exception: Throwable) : NetworkState()
}