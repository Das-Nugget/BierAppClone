package com.example.beer.data.enums

enum class Aftertaste(val description: String) {
    SHORT("Short"),
    SMOOTH("Smooth"),
    SPICY("Spicy"),
    LONG_LASTING("Long-lasting");

    override fun toString(): String = description
}