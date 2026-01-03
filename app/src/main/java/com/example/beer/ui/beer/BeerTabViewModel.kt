package com.example.beer.ui.beer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beer.data.model.BeerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.beer.interfaces.BeerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
