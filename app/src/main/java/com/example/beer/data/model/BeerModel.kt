package com.example.beer.data.model

import androidx.room.*
import com.example.beer.data.enums.BeerType
import java.io.Serializable

@Entity(
    tableName = "beers",
    indices = [Index("ratingId"), Index("tasteId")],
    foreignKeys = [
        ForeignKey(
            entity = RatingModel::class,
            parentColumns = ["id"],
            childColumns = ["ratingId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TasteModel::class,
            parentColumns = ["id"],
            childColumns = ["tasteId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BeerModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val producer: String,
    val alcoholPercentage: Double,
    val type: BeerType,
    val imageURI: String?,
    val price: Double,
    val note: String?,
    val ratingId: Int?,
    val tasteId: Int?,
    val createdAt: Long = System.currentTimeMillis()
) : Serializable