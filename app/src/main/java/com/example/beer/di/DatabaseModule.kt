package com.example.beer.di

import android.content.Context
import androidx.room.Room
import com.example.beer.data.AppDatabase
import com.example.beer.data.dao.BeerDao
import com.example.beer.data.dao.RatingDao
import com.example.beer.data.dao.TasteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app.db"
        )
            // .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideBeerDao(db: AppDatabase): BeerDao = db.beerDao()

    @Provides
    fun provideRatingDao(db: AppDatabase): RatingDao = db.ratingDao()

    @Provides
    fun provideTasteDao(db: AppDatabase): TasteDao = db.tasteDao()
}