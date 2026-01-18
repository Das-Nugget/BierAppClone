package com.example.beer.data.dao


import androidx.room.*
import com.example.beer.data.model.RatingModel
import kotlinx.coroutines.flow.Flow

@Dao
interface RatingDao {
    @Insert
    suspend fun insertRating(rating: RatingModel): Long

    @Query("SELECT * FROM ratings")
    fun getAllRatings(): Flow<List<RatingModel>>

    @Query("SELECT * FROM ratings WHERE id = :id")
    suspend fun getRatingById(id: Int): RatingModel?

    @Update
    suspend fun updateRating(rating: RatingModel)

    @Delete
    suspend fun deleteRating(rating: RatingModel)
}
