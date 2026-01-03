package com.example.beer.data.enums

enum class Mouthfeel(val description: String) {
    THIN("Thin"),
    MEDIUM("Medium"),
    FULL_BODIED("Full-bodied"),
    HEAVY("Heavy");

    override fun toString(): String = description
}