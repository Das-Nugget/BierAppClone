package com.example.beer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.beer.data.enums.Aftertaste
import com.example.beer.data.enums.Bitterness
import com.example.beer.data.enums.Mouthfeel
import com.example.beer.data.enums.Sweetness

@Entity(tableName = "tastes")
data class TasteModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sweetness: Sweetness?,
    val bitterness: Bitterness?,
    val mouthfeel: Mouthfeel?,
    val aftertaste: Aftertaste?,
    val note: String?
)