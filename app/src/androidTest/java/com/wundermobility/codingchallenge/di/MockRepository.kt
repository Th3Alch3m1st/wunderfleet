package com.wundermobility.codingchallenge.di

import com.wundermobility.codingchallenge.fakerepository.FakeCarInfoRepositoryImpl
import com.wundermobility.codingchallenge.fakerepository.FakeCarRentRepositoryImpl
import com.wundermobility.codingchallenge.network.di.RepositoryModule
import com.wundermobility.codingchallenge.network.repository.carinfo.CarInfoRepository
import com.wundermobility.codingchallenge.network.repository.carrent.CarRentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class MockRepository {
    @Singleton
    @Binds
    abstract fun provideCarInfoRepository(impl: FakeCarInfoRepositoryImpl): CarInfoRepository

    @Singleton
    @Binds
    abstract fun provideCarRentRepository(impl: FakeCarRentRepositoryImpl): CarRentRepository
}