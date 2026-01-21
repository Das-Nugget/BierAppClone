package com.example.beer.ui.beer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beer.data.model.BeerModel
import com.example.beer.data.model.RatedBeer
import com.example.beer.data.model.RatingModel
import com.example.beer.data.model.TasteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.beer.interfaces.BeerRepository
import com.example.beer.interfaces.RatingRepository
import com.example.beer.interfaces.TasteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine

@HiltViewModel
class BeerTabViewModel @Inject constructor(
    private val ratingRepository: RatingRepository,
    private val tasteRepository: TasteRepository,
    private val beerRepository: BeerRepository
) : ViewModel() {

    val allBeers = beerRepository.getAllBeers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    private val allRatings = ratingRepository.getAllRatings()
    private val allTastes = tasteRepository.getAllTastes()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val filteredBeers = combine(allBeers, allRatings, allTastes, _searchQuery) { beers, ratings, tastes, query ->
        if (query.isBlank()) {
            beers.map { beer ->
                val ratingsMap = ratings.associateBy { it.id }
                val tasteMap = tastes.associateBy { it.id }
                // Wrap the filtered results into our combined object
                RatedBeer(
                    beer = beer,
                    rating = ratingsMap[beer.ratingId],
                    taste = tasteMap[beer.tasteId]
                )
            }
        } else {
            beers.filter { it.name.contains(query, ignoreCase = true) }.map { beer ->
                val ratingsMap = ratings.associateBy { it.id }
                val tasteMap = tastes.associateBy { it.id }
                // Wrap the filtered results into our combined object
                RatedBeer(
                    beer = beer,
                    rating = ratingsMap[beer.ratingId],
                    taste = tasteMap[beer.tasteId]
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun addBeer(beer: BeerModel) {
        viewModelScope.launch {
            beerRepository.addBeer(beer)
        }
    }

    fun updateBeer(beer: BeerModel) {
        viewModelScope.launch {
            beerRepository.updateBeer(beer)
        }

    }

    fun deleteBeer(beer: BeerModel) {
        viewModelScope.launch {
            beerRepository.deleteBeer(beer)
        }
    }

    fun addRating(beer: BeerModel, rating: RatingModel, taste: TasteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            beerRepository.addRating(beer, rating, taste)
        }
    }

    /*private val _allBeers = MutableStateFlow<List<BeerModel>>(emptyList())
    val allBeers = _allBeers.asStateFlow()


    init {
        viewModelScope.launch {
            val list = withContext(kotlinx.coroutines.Dispatchers.IO) {
                beerRepository.getAllBeers()
            }
            _allBeers.value = list
        }
    }*/
}