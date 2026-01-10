package com.example.beer.ui.rating

import FilterBeerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.beer.data.enums.Aftertaste
import com.example.beer.data.enums.Bitterness
import com.example.beer.data.enums.Mouthfeel
import com.example.beer.data.enums.Sweetness
import com.example.beer.ui.searchbar.CustomizableSearchBar

@Composable
fun RatingTabScreen(viewModel: RatingTabViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val beers by viewModel.filteredBeers.collectAsState()
    var showFilterDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Text("Beers:", modifier = Modifier.padding(bottom = 8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(beers) { beer ->
                    Text(
                        text = beer.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
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
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search for a beer...") }
            )

            FilledIconButton(
                onClick = { showFilterDialog = true},
                modifier = Modifier.size(48.dp) // standard touch target size
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter"
                )
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

