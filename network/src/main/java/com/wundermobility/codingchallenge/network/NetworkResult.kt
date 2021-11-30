package com.wundermobility.codingchallenge.network

/**
 * Created By Rafiqul Hasan
 */
sealed class NetworkResult<out T> {
    class Success<out T>(val data: T) : NetworkResult<T>()
    class Error(val exception: Throwable) : NetworkResult<Nothing>()
}