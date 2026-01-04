package com.example.beer.ui.rating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beer.interfaces.BeerRepository
import com.example.beer.interfaces.RatingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RatingTabViewModel @Inject constructor(
    private val ratingRepository: RatingRepository,
    private val beerRepository: BeerRepository
) : ViewModel() {
    val allBeers = beerRepository.getAllBeers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val filteredBeers = combine(allBeers, _searchQuery) { beers, query ->
        val onlyRated = beers.filter { it.ratingId != null }
        if (query.isBlank()) {
            onlyRated
        } else {
            onlyRated.filter { it.name.contains(query, ignoreCase = true) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }
}