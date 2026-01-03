package com.example.beer.repository

import com.example.beer.data.dao.TasteDao
import com.example.beer.data.model.TasteModel
import com.example.beer.interfaces.TasteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TasteRepositoryImpl @Inject constructor(
    private val tasteDao: TasteDao
) : TasteRepository {

    override suspend fun getAllTastes(): List<TasteModel> =
        tasteDao.getAllTastes()

    override suspend fun getTasteById(id: Int): TasteModel? =
        tasteDao.getTasteById(id)

    override suspend fun addTaste(taste: TasteModel): Long =
        tasteDao.insertTaste(taste)

    override suspend fun updateTaste(taste: TasteModel) {
        tasteDao.updateTaste(taste)
    }

    override suspend fun deleteTaste(taste: TasteModel) {
        tasteDao.deleteTaste(taste)
    }
}