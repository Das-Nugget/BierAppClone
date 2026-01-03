package com.example.beer.ui.rating

import androidx.lifecycle.ViewModel
import com.example.beer.interfaces.RatingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RatingTabViewModel @Inject constructor(
    private val ratingRepository: RatingRepository
) : ViewModel() {


}