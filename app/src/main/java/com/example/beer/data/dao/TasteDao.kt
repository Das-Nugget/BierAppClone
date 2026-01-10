package com.example.beer.data.dao

import androidx.room.*
import com.example.beer.data.model.TasteModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TasteDao {
    @Insert
    suspend fun insertTaste(taste: TasteModel): Long

    @Query("SELECT * FROM tastes")
    fun getAllTastes(): Flow<List<TasteModel>>

    @Query("SELECT * FROM tastes WHERE id = :id")
    suspend fun getTasteById(id: Int): TasteModel?

    @Update
    suspend fun updateTaste(taste: TasteModel)

    @Delete
    suspend fun deleteTaste(taste: TasteModel)
}