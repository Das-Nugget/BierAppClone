package com.example.beer.interfaces

import com.example.beer.data.model.TasteModel

interface TasteRepository {
    suspend fun getAllTastes(): List<TasteModel>
    suspend fun getTasteById(id: Int): TasteModel?
    suspend fun addTaste(taste: TasteModel): Long
    suspend fun updateTaste(taste: TasteModel)
    suspend fun deleteTaste(taste: TasteModel)
}