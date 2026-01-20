package com.example.beer.data.dao

import androidx.room.*
import com.example.beer.data.model.BeerModel
import com.example.beer.data.model.RatingModel
import com.example.beer.data.model.TasteModel
import com.example.beer.data.dao.RatingDao
import kotlinx.coroutines.flow.Flow

@Dao
interface BeerDao {
    @Insert
    suspend fun insertBeer(beer: BeerModel)

    @Update
    suspend fun updateBeer(beer: BeerModel)

    @Delete
    suspend fun deleteBeer(beer: BeerModel)

    @Query("SELECT * FROM beers ORDER BY createdAt DESC")
    fun getAllBeers(): Flow<List<BeerModel>>

    @Transaction
    @Query("SELECT * FROM beers WHERE id = :id")
    suspend fun getBeerById(id: Int): BeerModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeers(beers: List<BeerModel>)

    @Upsert
    suspend fun upsertBeer(beer: BeerModel)

    @Upsert
    suspend fun upsertRating(rating: RatingModel):Long

    @Upsert
    suspend fun upsertTaste(taste: TasteModel):Long

    @Transaction
    suspend fun addRating(beer: BeerModel, rating: RatingModel, taste: TasteModel) {
            val newRatingId = upsertRating(rating)
            val newTasteId = upsertTaste(taste)

            // Note: Room's @Upsert returns the row ID if inserted, or -1/existing if updated
            val beerToSave = beer.copy(
                ratingId = if (newRatingId != -1L) newRatingId.toInt() else rating.id,
                tasteId = if (newTasteId != -1L) newTasteId.toInt() else taste.id
            )

            upsertBeer(beerToSave)
    }
}