package com.example.beer.data.model

data class RatedBeer(
    val beer: BeerModel,
    val rating: RatingModel?,
    val taste: TasteModel?
)