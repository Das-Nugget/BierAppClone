package com.example.beer.ui.rating

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beer.data.enums.Aftertaste
import com.example.beer.data.enums.Bitterness
import com.example.beer.data.enums.Mouthfeel
import com.example.beer.data.enums.Sweetness
import com.example.beer.data.model.BeerModel
import com.example.beer.data.model.RatedBeer
import com.example.beer.data.model.RatingModel
import com.example.beer.data.model.TasteModel
import com.example.beer.interfaces.BeerRepository
import com.example.beer.interfaces.RatingRepository
import com.example.beer.interfaces.TasteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "RatingTabViewModel"

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

    private val allBeers = beerRepository.getAllBeers()
    private val allRatings = ratingRepository.getAllRatings()
    private val allTastes = tasteRepository.getAllTastes()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _filters = MutableStateFlow(FilterState())
    val filters = _filters.asStateFlow()

    fun applyFilters(
        minRating: Double, maxRating: Double, minTaste: Double, maxTaste: Double,
        minLook: Double, maxLook: Double, minDrinkability: Double, maxDrinkability: Double,
        aftertaste: Aftertaste?, bitterness: Bitterness?, mouthfeel: Mouthfeel?, sweetness: Sweetness?
    ) {
        Log.d(TAG, "Applying new filters: Rating[$minRating-$maxRating], Taste[$minTaste-$maxTaste]")
        _filters.value = FilterState(
            minRating, maxRating, minTaste, maxTaste, minLook, maxLook,
            minDrinkability, maxDrinkability, aftertaste, bitterness, mouthfeel, sweetness
        )
    }

    /* The combine block below manages a complex reactive join. It synchronizes
       three independent Room database streams (Beers, Ratings, Tastes).
       It performs an inner-join-like filter to ensure a RatedBeer object
       is only emitted if its associated rating and taste data exist,
       while simultaneously applying text search, range-based numeric
       filters, and enum-based taste profiles.
    */
    val filteredBeers = combine(
        allBeers,
        allRatings,
        allTastes,
        _searchQuery,
        _filters
    ) { beers, ratings, tastes, query, f ->
        Log.v(TAG, "Re-calculating filtered beers. Source sizes: B:${beers.size}, R:${ratings.size}, T:${tastes.size}")

        val ratingsMap = ratings.associateBy { it.id }
        val tasteMap = tastes.associateBy { it.id }

        beers.filter { beer ->
            val matchesQuery = beer.name.contains(query, ignoreCase = true) ||
                    beer.producer.contains(query, ignoreCase = true)

            val rating = ratingsMap[beer.ratingId]
            val taste = tasteMap[beer.tasteId]

            if (!matchesQuery || rating == null || taste == null) return@filter false

            val matchesNumeric =
                rating.overallRating?.let { it in f.minRating..f.maxRating } ?: true &&
                        rating.taste?.let { it.toDouble() in f.minTaste..f.maxTaste } ?: true &&
                        rating.look?.let { it.toDouble() in f.minLook..f.maxLook } ?: true &&
                        rating.drinkability?.let { it.toDouble() in f.minDrinkability..f.maxDrinkability } ?: true

            val matchesEnums =
                (f.aftertaste == null || taste.aftertaste == f.aftertaste) &&
                        (f.bitterness == null || taste.bitterness == f.bitterness) &&
                        (f.mouthfeel == null || taste.mouthfeel == f.mouthfeel) &&
                        (f.sweetness == null || taste.sweetness == f.sweetness)

            matchesNumeric && matchesEnums
        }.map { beer ->
            RatedBeer(
                beer = beer,
                rating = ratingsMap[beer.ratingId]!!,
                taste = tasteMap[beer.tasteId]
            )
        }.also {
            Log.d(TAG, "Filter complete: ${it.size} items matching criteria")
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addRating(beer: BeerModel, rating: RatingModel, taste: TasteModel) {
        Log.i(TAG, "Adding/Updating rating for beer: ${beer.name}")
        viewModelScope.launch(Dispatchers.IO) {
            beerRepository.addRating(beer, rating, taste)
        }
    }

    fun addBeer(beer: BeerModel) {
        Log.i(TAG, "Upserting beer: ${beer.name} (ID: ${beer.id})")
        viewModelScope.launch(Dispatchers.IO) {
            beerRepository.upsertBeer(beer)
        }
    }

    fun deleteBeer(beer: BeerModel) {
        Log.w(TAG, "Deleting beer: ${beer.name} (ID: ${beer.id})")
        viewModelScope.launch(Dispatchers.IO) {
            beerRepository.deleteBeer(beer)
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        Log.v(TAG, "Search query changed: $newQuery")
        _searchQuery.value = newQuery
    }
}