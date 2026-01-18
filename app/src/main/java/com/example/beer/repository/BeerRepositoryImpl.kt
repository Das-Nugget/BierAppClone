package com.example.beer.repository

import com.example.beer.data.dao.BeerDao
import com.example.beer.data.model.BeerModel
import com.example.beer.data.model.RatingModel
import com.example.beer.data.model.TasteModel
import com.example.beer.interfaces.BeerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeerRepositoryImpl @Inject constructor(
    private val beerDao: BeerDao
) : BeerRepository {

    //override suspend fun getAllBeers(): List<BeerModel> = beerDao.getAllBeers()
    override fun getAllBeers(): Flow<List<BeerModel>> =
        beerDao.getAllBeers()

    override suspend fun getBeer(id: Int): BeerModel? =
        beerDao.getBeerById(id)

    override suspend fun addBeer(beer: BeerModel) {
        beerDao.insertBeer(beer)
    }

    override suspend fun addBeers(beers: List<BeerModel>) {
        beerDao.insertBeers(beers)
    }

    override suspend fun updateBeer(beer: BeerModel) {
        beerDao.updateBeer(beer)
    }

    override suspend fun deleteBeer(beer: BeerModel) {
        beerDao.deleteBeer(beer)
    }

    override suspend fun upsertBeer(beer: BeerModel) {
        beerDao.upsertBeer(beer)
    }

    override suspend fun addRating(beer: BeerModel, rating: RatingModel, taste: TasteModel) {
        beerDao.addRating(beer, rating, taste)
    }
}