package com.example.beer.ui.rating

import FilterBeerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.beer.data.model.BeerModel
import com.example.beer.data.model.RatingModel
import com.example.beer.ui.popups.AddBeerDialog
import com.example.beer.ui.popups.AddRatingDialog
import com.example.beer.ui.popups.EditBeerDialogue
import com.example.beer.ui.searchbar.CustomizableSearchBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RatingTabScreen(viewModel: RatingTabViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val beers by viewModel.filteredBeers.collectAsState()
    val currentFilters by viewModel.filters.collectAsState()
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    var showFilterDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showOptionsDialog by remember { mutableStateOf(false) }
    var showEditRatingDialog by remember { mutableStateOf(false) }
    var selectedBeer by remember { mutableStateOf<BeerModel?>(null) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
                onSearch = { softwareKeyboardController?.hide() },
                searchResults = beers.map { it.beer.name },
                onResultClick = { selectedName ->
                    viewModel.onSearchQueryChange(selectedName)
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search for a beer...") }
            )

            FilledIconButton(
                onClick = { showFilterDialog = true },
                modifier = Modifier.size(48.dp) // standard touch target size
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter"
                )
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(beers) { beer ->
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .clickable(onClick = {
                                selectedBeer = beer.beer
                                showOptionsDialog = true
                            }),
                    )
                    {
                        BeerRatingItem(
                            beer = beer.beer,
                            rating = beer.rating
                        )
                    }

                }
            }
        }
    }

    if (showFilterDialog) {
        FilterBeerDialog(
            currentFilters,
            onDismiss = { showFilterDialog = false },
            onSearch = { minR, maxR, minT, maxT, minL, maxL, minD, maxD, aft, bit, mou, swe ->
                viewModel.applyFilters(
                    minR, maxR, minT, maxT, minL, maxL, minD, maxD, aft, bit, mou, swe
                )
                showFilterDialog = false
            }
        )
    }
    if (showAddDialog) {
        AddBeerDialog(
            beer = selectedBeer,
            onDismiss = { showAddDialog = false },
            onSave = { beer ->
                viewModel.addBeer(
                    beer
                )
                showAddDialog = false
            }
        )
    }
    if (showEditRatingDialog) {
        AddRatingDialog(
            onDismiss = { showEditRatingDialog = false },
            onSave = { rating, taste ->
                viewModel.addRating(
                    selectedBeer!!, rating, taste
                )
                showEditRatingDialog = false
            }
        )
    }
    if (showOptionsDialog) {
        EditBeerDialogue(
            onDismiss = { showOptionsDialog = false },
            onEditBeer = {
                showAddDialog = true
                showOptionsDialog = false
            },
            onEditRating = {
                showEditRatingDialog = true
                showOptionsDialog = false
            },
            onDeleteBeer = {
                viewModel.deleteBeer(selectedBeer!!)
                showOptionsDialog = false
            }
        )
    }
}

@Composable
fun BeerRatingItem(beer: BeerModel, rating: RatingModel) {
    val dateFormatter = remember { SimpleDateFormat("yyyy", Locale.getDefault()) }
    val formattedYear = dateFormatter.format(Date(beer.createdAt))

    // Card with 0 shape to match the full-width divider look in the mockup
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(0)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFFFFB300)),
                    contentAlignment = Alignment.Center
                ) {
                    if (beer.imageURI?.isEmpty() != false) {
                        Text(
                            text = "IMG",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    } else {
                        AsyncImage(
                            model = beer.imageURI,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = beer.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = beer.producer,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = beer.type.styleName,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // 3. Right Side Rating (Star + Number)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFF2A900),
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = String.format("%.1f", rating.overallRating),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFF2A900),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
        }
    }
}