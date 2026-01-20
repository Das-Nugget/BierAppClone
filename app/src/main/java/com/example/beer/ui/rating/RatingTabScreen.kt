package com.example.beer.ui.rating

import FilterBeerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Star
import com.example.beer.ui.theme.beerAmber
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beer.data.enums.Aftertaste
import com.example.beer.data.enums.Bitterness
import com.example.beer.data.enums.Mouthfeel
import com.example.beer.data.enums.Sweetness
import com.example.beer.data.model.BeerModel
import com.example.beer.data.model.RatingModel
import com.example.beer.interfaces.RatingRepository
import com.example.beer.ui.beer.BeerItem
import com.example.beer.ui.searchbar.CustomizableSearchBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RatingTabScreen(viewModel: RatingTabViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val beers by viewModel.filteredBeers.collectAsState()
    val ratings by viewModel.allRatings.collectAsState()
    val ratingsMap = remember(ratings) { ratings.associateBy { it.id } }

    var showFilterDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between bar and button
        ) {
            CustomizableSearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) },
                onSearch = { /* Handle search */ },
                searchResults = beers.map { it.name },
                onResultClick = { selectedName ->
                    viewModel.onSearchQueryChange(selectedName)
                },
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                placeholder = { Text("Search for a beer...") }
            )

            FilledIconButton(
                onClick = { showFilterDialog = true},
                modifier = Modifier.size(56.dp), // standard touch target size
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = beerAmber,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter"
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(beers) { beer ->
                val rating = ratingsMap[beer.ratingId]
                RatedBeerItem(beer = beer, rating = rating, themeColor = beerAmber)
            }
        }

        if (showFilterDialog) {
            FilterBeerDialog(
                onDismiss = { showFilterDialog = false },
                onSearch = { minR, maxR, minT, maxT, minL, maxL, minD, maxD, aft, bit, mou, swe ->
                    viewModel.applyFilters(
                        minR, maxR, minT, maxT, minL, maxL, minD, maxD, aft, bit, mou, swe
                    )
                    showFilterDialog = false
                }
            )
        }
    }
}

@Composable
fun RatedBeerItem(beer: BeerModel, rating: RatingModel?, themeColor: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Spalte: Bild-Platzhalter
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(themeColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "IMG",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 2. Spalte: Informationen
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = beer.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = beer.producer,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // 3. Spalte: Overall Rating ganz rechts
                Column {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = themeColor
                    )
                    Text(
                        text = rating?.overallRating.let { "%.1f".format(it) } ?: "-",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
