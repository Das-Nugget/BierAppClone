package com.example.beer.ui.beer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beer.data.enums.BeerType
import com.example.beer.data.model.BeerModel
import com.example.beer.data.model.RatingModel
import com.example.beer.data.model.TasteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.beer.interfaces.BeerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class BeerTabViewModel @Inject constructor(
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
        if (query.isBlank()) {
            beers
        } else {
            beers.filter { it.name.contains(query, ignoreCase = true) }
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
            beerRepository.upsertBeer(beer)
        }
    }
    fun deleteBeer(beer: BeerModel){
        viewModelScope.launch {
            beerRepository.deleteBeer(beer)
        }
    }
    fun addRating(beer: BeerModel, rating: RatingModel, taste: TasteModel) {
        viewModelScope.launch {
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
