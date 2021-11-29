package com.wundermobility.codingchallenge.network.testutil

import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Single
import org.mockito.kotlin.whenever

/**
 * Created By Rafiqul Hasan
 */

infix fun Any?.returns(mockValue: Any?) =
    whenever(this).thenReturn(Single.create<Any?> { emitter ->
        emitter.onSuccess(mockValue)
    })


infix fun Any?.returnsException(exception: Any?) =
    whenever(this).thenReturn(Single.create<Any?> { emitter ->
        emitter.onError(exception as @NonNull Throwable)
    })