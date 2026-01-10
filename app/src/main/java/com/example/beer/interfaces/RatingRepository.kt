package com.example.beer.interfaces

import com.example.beer.data.model.RatingModel
import kotlinx.coroutines.flow.Flow

interface RatingRepository {
    fun getAllRatings(): Flow<List<RatingModel>>
    suspend fun getRatingById(id: Int): RatingModel?
    suspend fun addRating(rating: RatingModel): Long
    suspend fun updateRating(rating: RatingModel)
    suspend fun deleteRating(rating: RatingModel)
}