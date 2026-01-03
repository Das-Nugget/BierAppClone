package com.example.beer.interfaces

import com.example.beer.data.model.RatingModel

interface RatingRepository {
    suspend fun getAllRatings(): List<RatingModel>
    suspend fun getRatingById(id: Int): RatingModel?
    suspend fun addRating(rating: RatingModel): Long
    suspend fun updateRating(rating: RatingModel)
    suspend fun deleteRating(rating: RatingModel)
}