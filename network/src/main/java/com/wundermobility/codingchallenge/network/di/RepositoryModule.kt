package com.wundermobility.codingchallenge.network.di

import com.wundermobility.codingchallenge.network.repository.carinfo.CarInfoRepository
import com.wundermobility.codingchallenge.network.repository.carinfo.CarInfoRepositoryImpl
import com.wundermobility.codingchallenge.network.repository.carrent.CarRentRepository
import com.wundermobility.codingchallenge.network.repository.carrent.CarRentRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideCarInfoRepository(repository: CarInfoRepositoryImpl): CarInfoRepository

    @Binds
    @Singleton
    abstract fun provideCarRentRepository(repository: CarRentRepositoryImpl): CarRentRepository
}