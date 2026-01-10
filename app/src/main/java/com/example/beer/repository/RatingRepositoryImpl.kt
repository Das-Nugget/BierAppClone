package com.example.beer.repository

import com.example.beer.data.dao.RatingDao
import com.example.beer.data.model.RatingModel
import com.example.beer.interfaces.RatingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatingRepositoryImpl @Inject constructor(
    private val ratingDao: RatingDao
) : RatingRepository {

    override fun getAllRatings(): Flow<List<RatingModel>> =
        ratingDao.getAllRatings()

    override suspend fun getRatingById(id: Int): RatingModel? =
        ratingDao.getRatingById(id)

    override suspend fun addRating(rating: RatingModel): Long =
        ratingDao.insertRating(rating)

    override suspend fun updateRating(rating: RatingModel) {
        ratingDao.updateRating(rating)
    }

    override suspend fun deleteRating(rating: RatingModel) {
        ratingDao.deleteRating(rating)
    }
}