package com.example.beer.data.dto

import com.example.beer.data.enums.BeerType
import com.example.beer.data.model.BeerModel
import kotlinx.serialization.Serializable as KxSerializable

@KxSerializable
data class BeerDto(
    val name: String,
    val producer: String,
    val alcoholPercentage: Double,
    val type: BeerType,
    val price: Double,
    val note: String?,
    val createdAt: Long = System.currentTimeMillis()
)

fun BeerModel.toExport(): BeerDto = BeerDto(
    name = name,
    producer = producer,
    alcoholPercentage = alcoholPercentage,
    type = type,
    price = price,
    note = note
)

fun BeerDto.toModel(): BeerModel =
    BeerModel(
        id = 0,
        name = name,
        producer = producer,
        alcoholPercentage = alcoholPercentage,
        type = type,
        imageURI = null,
        price = price,
        note = note,
        ratingId = null,
        tasteId = null,
        createdAt = createdAt
    )