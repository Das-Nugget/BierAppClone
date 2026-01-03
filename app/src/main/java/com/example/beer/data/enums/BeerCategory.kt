package com.example.beer.data.enums

import kotlinx.serialization.Serializable as KxSerializable

@KxSerializable
enum class BeerCategory(val displayName: String) {
    ALE("Ale Styles (Top-Fermented)"),
    LAGER("Lager Styles (Bottom-Fermented)"),
    WILD("Wild/Spontaneously Fermented"),
    HYBRID("Hybrid/Specialty Styles"),
    UNKNOWN("unknown");
}