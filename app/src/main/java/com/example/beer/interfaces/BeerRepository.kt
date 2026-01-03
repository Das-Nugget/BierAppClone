package com.example.beer.interfaces

import com.example.beer.data.model.BeerModel
import kotlinx.coroutines.flow.Flow

interface BeerRepository {
    fun getAllBeers(): Flow<List<BeerModel>>
    suspend fun getBeer(id: Int): BeerModel?
    suspend fun addBeer(beer: BeerModel)
    suspend fun addBeers(beers: List<BeerModel>)
    suspend fun updateBeer(beer: BeerModel)
    suspend fun deleteBeer(beer: BeerModel)
}