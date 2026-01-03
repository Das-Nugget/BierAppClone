package com.example.beer.data.dao

import androidx.room.*
import com.example.beer.data.model.BeerModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BeerDao {
    @Insert
    suspend fun insertBeer(beer: BeerModel)

    @Update
    suspend fun updateBeer(beer: BeerModel)

    @Delete
    suspend fun deleteBeer(beer: BeerModel)

    @Query("SELECT * FROM beers ORDER BY name")
    fun getAllBeers(): Flow<List<BeerModel>>

    @Transaction
    @Query("SELECT * FROM beers WHERE id = :id")
    suspend fun getBeerById(id: Int): BeerModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeers(beers: List<BeerModel>)
}