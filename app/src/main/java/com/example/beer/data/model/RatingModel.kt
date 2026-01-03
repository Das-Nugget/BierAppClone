package com.example.beer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ratings")
data class RatingModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val taste: Int?,
    val look: Int?,
    val drinkability: Int?,
    val overallRating: Double?
)