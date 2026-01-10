package com.example.beer.interfaces

import com.example.beer.data.model.TasteModel
import kotlinx.coroutines.flow.Flow

interface TasteRepository {
    fun getAllTastes(): Flow<List<TasteModel>>
    suspend fun getTasteById(id: Int): TasteModel?
    suspend fun addTaste(taste: TasteModel): Long
    suspend fun updateTaste(taste: TasteModel)
    suspend fun deleteTaste(taste: TasteModel)
}