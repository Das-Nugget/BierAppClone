package com.example.beer.data.enums

import kotlinx.serialization.Serializable as KxSerializable

@KxSerializable
enum class BeerType(val styleName: String, val category: BeerCategory) {
    // --- Ale Styles (Top-Fermented) ---
    ALTBIER("Altbier", BeerCategory.ALE),
    AMBER_ALE("Amber Ale", BeerCategory.ALE),
    AMERICAN_PALE_ALE("American Pale Ale (APA)", BeerCategory.ALE),
    BARLEY_WINE("Barley Wine (or Barleywine)", BeerCategory.ALE),
    BELGIAN_PALE_ALE("Belgian Pale Ale", BeerCategory.ALE),
    BELGIAN_STRONG_ALE("Belgian Strong Ale", BeerCategory.ALE),
    BERLINER_WEISSE("Berliner Weisse", BeerCategory.ALE),
    BIERE_DE_GARDE("Bière de Garde", BeerCategory.ALE),
    BITTER("Bitter", BeerCategory.ALE),
    BLONDE_ALE("Blonde Ale", BeerCategory.ALE),
    BROWN_ALE("Brown Ale", BeerCategory.ALE),
    CALIFORNIA_COMMON("California Common (Steam Beer)", BeerCategory.HYBRID), // Hybrid since it's an Ale yeast fermented at Lager temps
    CREAM_ALE("Cream Ale", BeerCategory.HYBRID), // Hybrid/Mixed style
    DUBBEL("Dubbel", BeerCategory.ALE),
    DUNKELWEIZEN("Dunkelweizen", BeerCategory.ALE),
    FLANDERS_RED_ALE("Flanders Red Ale", BeerCategory.WILD), // Often soured/aged
    FRUIT_BEER("Fruit Beer", BeerCategory.HYBRID),
    GOLDEN_ALE("Golden Ale", BeerCategory.ALE),
    GOSE("Gose", BeerCategory.HYBRID), // Often soured, sometimes called Wild
    GRODZISKIE("Grodziskie", BeerCategory.ALE),
    GRUIT("Gruit", BeerCategory.HYBRID),
    HEFEWEIZEN("Hefeweizen", BeerCategory.ALE),
    IMPERIAL_STOUT("Imperial Stout", BeerCategory.ALE),
    INDIA_PALE_ALE("India Pale Ale (IPA)", BeerCategory.ALE),
    IRISH_RED_ALE("Irish Red Ale", BeerCategory.ALE),
    KOLSCH("Kölsch", BeerCategory.HYBRID), // Classified as a cold-conditioned Ale
    MILD_ALE("Mild Ale", BeerCategory.ALE),
    OATMEAL_STOUT("Oatmeal Stout", BeerCategory.ALE),
    OLD_ALE("Old Ale", BeerCategory.ALE),
    OUD_BRUIN("Oud Bruin", BeerCategory.WILD), // Often soured/aged
    PALE_ALE("Pale Ale", BeerCategory.ALE),
    PORTER("Porter", BeerCategory.ALE),
    PUMPKIN_ALE("Pumpkin Ale", BeerCategory.HYBRID),
    QUADRUPEL("Quadrupel", BeerCategory.ALE),
    RED_ALE("Red Ale", BeerCategory.ALE),
    RYE_BEER("Rye Beer (or Roggenbier)", BeerCategory.HYBRID), // Often Ale, but sometimes Lager
    SAHTI("Sahti", BeerCategory.HYBRID),
    SAISON("Saison (Farmhouse Ale)", BeerCategory.ALE),
    SCOTCH_ALE("Scotch Ale (or Wee Heavy)", BeerCategory.ALE),
    SOUR_ALE("Sour Ale", BeerCategory.WILD),
    STOUT("Stout", BeerCategory.ALE),
    STRONG_ALE("Strong Ale", BeerCategory.ALE),
    TRIPEL("Tripel", BeerCategory.ALE),
    WHEAT_WINE_ALE("Wheat Wine Ale", BeerCategory.ALE),
    WITBIER("Witbier (Belgian White)", BeerCategory.ALE),

    // --- Lager Styles (Bottom-Fermented) ---
    AMBER_LAGER("Amber Lager", BeerCategory.LAGER),
    BOCK("Bock", BeerCategory.LAGER),
    DARK_LAGER("Dark Lager", BeerCategory.LAGER),
    DORTMUNDER_EXPORT("Dortmunder Export", BeerCategory.LAGER),
    HELLES("Helles (Munich Helles)", BeerCategory.LAGER),
    LAGER("Lager", BeerCategory.LAGER),
    MARZEN("Märzen (or Oktoberfestbier)", BeerCategory.LAGER),
    PILSNER("Pilsner (or Pils, Pilsener)", BeerCategory.LAGER),
    RAUCHBIER("Rauchbier (Smoked Beer)", BeerCategory.HYBRID), // Can be Ale or Lager
    VIENNA_LAGER("Vienna Lager", BeerCategory.LAGER),

    // --- Wild/Spontaneously Fermented Styles ---
    GUEUZE("Gueuze", BeerCategory.WILD),
    LAMBIC("Lambic", BeerCategory.WILD),
    KRIEK("Kriek (Cherry Lambic)", BeerCategory.WILD),
    WILD_ALE("Wild Ale", BeerCategory.WILD),

    // --- Other/Hybrid/Specialty Styles ---
    BARREL_AGED_BEER("Barrel-Aged Beer", BeerCategory.HYBRID),
    BLACK_ALE("Black Ale (or Cascadian Dark Ale)", BeerCategory.ALE), // Generally considered an Ale
    CHILI_PEPPER_BEER("Chili Pepper Beer", BeerCategory.HYBRID),
    COFFEE_BEER("Coffee Beer", BeerCategory.HYBRID),
    EXPERIMENTAL_BEER("Experimental Beer", BeerCategory.HYBRID),
    FIELD_BEER("Field Beer", BeerCategory.HYBRID),
    HISTORICAL_BEER("Historical Beer", BeerCategory.HYBRID),
    MALT_LIQUOR("Malt Liquor", BeerCategory.HYBRID),
    SMOKED_BEER("Smoked Beer (in general)", BeerCategory.HYBRID),
    SPECIALTY_BEER("Specialty Beer", BeerCategory.HYBRID),
    UNKNOWN("unknown", BeerCategory.UNKNOWN);


    // Helper method to provide a more readable output than the constant name
    override fun toString(): String = styleName
}