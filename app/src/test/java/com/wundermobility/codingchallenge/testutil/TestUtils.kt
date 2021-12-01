package com.wundermobility.codingchallenge.testutil

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.wundermobility.codingchallenge.network.model.CarInfo
import com.wundermobility.codingchallenge.network.model.CarRentResponse
import java.io.IOException
import java.lang.reflect.Type

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

    fun getCarListTestData(fileName: String): List<CarInfo> {
        val moshi = Moshi.Builder()
            .build()
        val listData: Type = Types.newParameterizedType(
            List::class.java,
            CarInfo::class.java
        )
        val adapter: JsonAdapter<List<CarInfo>> = moshi.adapter(listData)
        val jsonString = readFileToString(TestUtils::class.java, "/$fileName")
        return adapter.fromJson(jsonString)!!
    }

    fun getCarDetailsInfoTestData(fileName: String): CarInfo {
        val moshi = Moshi.Builder()
            .build()
        val jsonAdapter: JsonAdapter<CarInfo> = moshi.adapter(CarInfo::class.java)
        val jsonString = readFileToString(TestUtils::class.java, "/$fileName")
        return jsonAdapter.fromJson(jsonString)!!
    }

    fun getCarRentSuccessTestData(fileName: String): CarRentResponse {
        val moshi = Moshi.Builder()
            .build()
        val jsonAdapter: JsonAdapter<CarRentResponse> = moshi.adapter(CarRentResponse::class.java)
        val jsonString = readFileToString(TestUtils::class.java, "/$fileName")
        return jsonAdapter.fromJson(jsonString)!!
    }
}