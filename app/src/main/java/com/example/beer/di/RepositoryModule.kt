package com.example.beer.di

import com.example.beer.interfaces.BeerRepository
import com.example.beer.interfaces.RatingRepository
import com.example.beer.interfaces.TasteRepository
import com.example.beer.repository.BeerRepositoryImpl
import com.example.beer.repository.RatingRepositoryImpl
import com.example.beer.repository.TasteRepositoryImpl
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
    abstract fun bindBeerRepository(
        impl: BeerRepositoryImpl
    ): BeerRepository

    @Binds
    @Singleton
    abstract fun bindRatingRepository(
        impl: RatingRepositoryImpl
    ): RatingRepository

    @Binds
    @Singleton
    abstract fun bindTasteRepository(
        impl: TasteRepositoryImpl
    ): TasteRepository
}