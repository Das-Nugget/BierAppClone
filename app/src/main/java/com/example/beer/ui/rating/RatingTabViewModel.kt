package com.example.beer.ui.rating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beer.data.enums.Aftertaste
import com.example.beer.data.enums.Bitterness
import com.example.beer.data.enums.Mouthfeel
import com.example.beer.data.enums.Sweetness
import com.example.beer.data.model.RatingModel
import com.example.beer.interfaces.BeerRepository
import com.example.beer.interfaces.RatingRepository
import com.example.beer.interfaces.TasteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


data class FilterState(
    val minRating: Double = 0.0,
    val maxRating: Double = 5.0,
    val minTaste: Double = 0.0,
    val maxTaste: Double = 5.0,
    val minLook: Double = 0.0,
    val maxLook: Double = 5.0,
    val minDrinkability: Double = 0.0,
    val maxDrinkability: Double = 5.0,
    val aftertaste: Aftertaste? = null,
    val bitterness: Bitterness? = null,
    val mouthfeel: Mouthfeel? = null,
    val sweetness: Sweetness? = null
)

@HiltViewModel
class RatingTabViewModel @Inject constructor(
    private val ratingRepository: RatingRepository,
    private val tasteRepository: TasteRepository,
    private val beerRepository: BeerRepository
) : ViewModel() {
    // Existing allBeers flow
    val allBeers = beerRepository.getAllBeers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Fetch all ratings to join with beers during filtering
    val allRatings = ratingRepository.getAllRatings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Fetch all ratings to join with beers during filtering
    private val allTastes = tasteRepository.getAllTastes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())



    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _filters = MutableStateFlow(FilterState())
    val filters = _filters.asStateFlow()

    fun applyFilters(
        minRating: Double, maxRating: Double, minTaste: Double, maxTaste: Double,
        minLook: Double, maxLook: Double, minDrinkability: Double, maxDrinkability: Double,
        aftertaste: Aftertaste?, bitterness: Bitterness?, mouthfeel: Mouthfeel?, sweetness: Sweetness?
    ) {
        _filters.value = FilterState(
            minRating, maxRating, minTaste, maxTaste, minLook, maxLook,
            minDrinkability, maxDrinkability, aftertaste, bitterness, mouthfeel, sweetness
        )
    }

    val filteredBeers = combine(allBeers, allRatings, allTastes, _searchQuery, _filters) { beers, ratings, tastes , query, f ->
        val ratingsMap = ratings.associateBy { it.id }

        beers.filter { beer ->
            // 1. Text Search
            val matchesQuery = beer.name.contains(query, ignoreCase = true)

            // 2. Rating Check
            val rating = ratingsMap[beer.ratingId]
            val hasRating = beer.ratingId != null && rating != null

            if (!matchesQuery || !hasRating) return@filter false


            // 3. Hardcoded Attribute Filtering
            val matchesNumeric =
                rating.overallRating?.let { it in f.minRating..f.maxRating } ?: true &&
                        rating.taste?.let { it.toDouble() in f.minTaste..f.maxTaste } ?: true &&
                        rating.look?.let { it.toDouble() in f.minLook..f.maxLook } ?: true &&
                        rating.drinkability?.let { it.toDouble() in f.minDrinkability..f.maxDrinkability } ?: true


            // 4. Taste Check
            val tasteMap = tastes.associateBy { it.id }
            val taste = tasteMap[beer.tasteId]
            val hasTaste = beer.tasteId != null && taste != null

            val matchesEnums = hasTaste && (
                (f.aftertaste == null || taste.aftertaste == f.aftertaste) &&
                (f.bitterness == null || taste.bitterness == f.bitterness) &&
                (f.mouthfeel == null || taste.mouthfeel == f.mouthfeel) &&
                (f.sweetness == null || taste.sweetness == f.sweetness)
            )

            matchesNumeric && matchesEnums
        }.sortedByDescending { beer -> ratingsMap[beer.ratingId]?.overallRating ?: 0.0 }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }
}