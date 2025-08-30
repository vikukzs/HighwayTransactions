package com.example.highwaytransactions.di

import com.example.highwaytransactions.data.repository.HighwayRepositoryImpl
import com.example.highwaytransactions.domain.repository.HighwayRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHighwayRepository(
        highwayRepositoryImpl: HighwayRepositoryImpl
    ): HighwayRepository
}