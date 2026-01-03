package com.example.beer.data.enums

enum class Sweetness(val description: String) {
    DRY("Dry"),
    BALANCED("Balanced"),
    SWEET("Sweet"),
    VERY_SWEET("Very sweet");

    override fun toString(): String = description
}