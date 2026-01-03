package com.example.beer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.beer.data.dao.BeerDao
import com.example.beer.data.dao.RatingDao
import com.example.beer.data.dao.TasteDao
import com.example.beer.data.model.BeerModel
import com.example.beer.data.model.RatingModel
import com.example.beer.data.model.TasteModel

@Database(
    entities = [BeerModel::class, RatingModel::class, TasteModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun beerDao(): BeerDao
    abstract fun ratingDao(): RatingDao
    abstract fun tasteDao(): TasteDao
}