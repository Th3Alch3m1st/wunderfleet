package com.wundermobility.codingchallenge.network.di

import android.content.Context
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.squareup.moshi.Moshi
import com.wundermobility.codingchallenge.network.BuildConfig
import com.wundermobility.codingchallenge.network.datasource.carinfo.CarInfoService
import com.wundermobility.codingchallenge.network.datasource.carrent.CarRentService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    private const val REQUEST_TIMEOUT = 30L
    private const val CACHE_SIZE: Long = 10 * 1024 * 1024L // 10 MB

    /**
     * The method returns the Moshi object
     **/
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(getLogInterceptors(BuildConfig.DEBUG))
            .cache(getCache(context)).build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideCarInfoService(retrofit: Retrofit): CarInfoService {
        return retrofit.create(CarInfoService::class.java)
    }

    @Provides
    @Singleton
    fun provideCarRentService(retrofit: Retrofit): CarRentService {
        return retrofit.create(CarRentService::class.java)
    }

    @Suppress("SameParameterValue")
    private fun getLogInterceptors(isDebugAble: Boolean = false): Interceptor {
        val builder = LoggingInterceptor.Builder()
            .setLevel(if (isDebugAble) Level.BASIC else Level.NONE)
            .log(Platform.INFO)
            .tag("Sixt")
            .request("Request")
            .response("Response")
        builder.isDebugAble = isDebugAble
        return builder.build()
    }

    private fun getCache(context: Context) = Cache(context.cacheDir, CACHE_SIZE)
}