package com.wundermobility.codingchallenge.network.testutil

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.mockwebserver.MockResponse
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

/**
 * Created By Rafiqul Hasan
 */
object TestUtils {
    @Throws(IOException::class)
    private fun readFileToString(contextClass: Class<*>, fileName: String): String {
        contextClass.getResourceAsStream(fileName)
            ?.bufferedReader().use {
                val jsonString = it?.readText() ?: ""
                it?.close()
                return jsonString
            }
    }

    @Throws(IOException::class)
    fun mockResponse(fileName: String): MockResponse {
        return MockResponse().setChunkedBody(
            readFileToString(TestUtils::class.java, "/$fileName"),
            32
        )
    }

    fun immediateExecutorService(): ExecutorService {
        return object : AbstractExecutorService() {
            override fun shutdown() {
            }

            override fun shutdownNow(): List<Runnable>? {
                return null
            }

            override fun isShutdown(): Boolean {
                return false
            }

            override fun isTerminated(): Boolean {
                return false
            }

            @Throws(InterruptedException::class)
            override fun awaitTermination(l: Long, timeUnit: TimeUnit): Boolean {
                return false
            }

            override fun execute(runnable: Runnable) {
                runnable.run()
            }
        }
    }
}