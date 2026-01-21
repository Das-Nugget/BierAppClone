package com.example.beer.ui.rating

import FilterBeerDialog
import android.util.Log
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
import com.example.beer.data.model.RatedBeer
import com.example.beer.data.model.RatingModel
import com.example.beer.ui.popups.AddBeerPopup
import com.example.beer.ui.popups.AddRatingDialog
import com.example.beer.ui.popups.BeerOptionsPopup
import com.example.beer.ui.popups.ConfirmationPopup
import com.example.beer.ui.searchbar.CustomizableSearchBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "RatingTabScreen"

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
    var selectedBeer by remember { mutableStateOf<RatedBeer?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var showCancelConfirmation by remember { mutableStateOf(false) }

    /* pendingCancelAction stores a lambda of the UI state change that was
       interrupted by the confirmation dialog, allowing us to resume the
       original dismissal logic after the user confirms.
    */
    var pendingCancelAction by remember { mutableStateOf({}) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomizableSearchBar(
                query = searchQuery,
                onQueryChange = {
                    Log.v(TAG, "Search query update: $it")
                    viewModel.onSearchQueryChange(it)
                },
                onSearch = {
                    Log.d(TAG, "Search triggered for: $searchQuery")
                    softwareKeyboardController?.hide()
                },
                searchResults = beers.map { it.beer.name },
                onResultClick = { selectedName ->
                    Log.d(TAG, "Search result selected: $selectedName")
                    viewModel.onSearchQueryChange(selectedName)
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search for a beer...") }
            )

            FilledIconButton(
                onClick = {
                    Log.d(TAG, "Filter dialog opened")
                    showFilterDialog = true
                },
                modifier = Modifier.size(48.dp)
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
                                Log.d(TAG, "Beer item clicked: ${beer.beer.name}")
                                selectedBeer = beer
                                showOptionsDialog = true
                            }),
                    )
                    {
                        BeerRatingItem(
                            beer = beer.beer,
                            rating = beer.rating!!
                        )
                    }

                }
            }
        }
    }

    if (showFilterDialog) {
        FilterBeerDialog(
            currentFilters,
            onDismiss = {
                Log.d(TAG, "Filter dialog dismissed")
                showFilterDialog = false
            },
            onSearch = { minR, maxR, minT, maxT, minL, maxL, minD, maxD, aft, bit, mou, swe ->
                Log.i(TAG, "Applying filters...")
                viewModel.applyFilters(
                    minR, maxR, minT, maxT, minL, maxL, minD, maxD, aft, bit, mou, swe
                )
                showFilterDialog = false
            }
        )
    }

    if (showOptionsDialog && selectedBeer != null) {
        BeerOptionsPopup(
            onDismiss = { showOptionsDialog = false },
            onEditBeer = {
                Log.d(TAG, "Option selected: Edit Beer")
                showOptionsDialog = false
                showEditDialog = true
            },
            onEditRating = {
                Log.d(TAG, "Option selected: Edit Rating")
                showOptionsDialog = false
                showEditRatingDialog = true
            },
            onDeleteBeer = {
                Log.d(TAG, "Option selected: Delete Beer")
                showOptionsDialog = false
                showDeleteConfirmation = true
            }
        )
    }

    if (showDeleteConfirmation) {
        ConfirmationPopup(
            text = "Are you sure you want to delete?",
            onConfirm = {
                Log.w(TAG, "Deleting beer: ${selectedBeer?.beer?.name}")
                selectedBeer?.let { viewModel.deleteBeer(it.beer) }
                showDeleteConfirmation = false
                selectedBeer = null
            },
            onDismiss = {
                Log.d(TAG, "Delete confirmation cancelled")
                showDeleteConfirmation = false
            }
        )
    }

    if (showEditDialog && selectedBeer != null) {
        AddBeerPopup(
            beerToEdit = selectedBeer?.beer,
            onDismiss = {
                Log.d(TAG, "Edit dialog cancellation requested")
                pendingCancelAction = { showEditDialog = false }
                showCancelConfirmation = true
            },
            onSave = { updatedBeer ->
                Log.i(TAG, "Updating beer details: ${updatedBeer.name}")
                viewModel.addBeer(updatedBeer)
                showEditDialog = false
            }
        )
    }

    if (showEditRatingDialog) {
        AddRatingDialog(
            rating = selectedBeer?.rating,
            tasteModel = selectedBeer?.taste,
            onDismiss = { showEditRatingDialog = false },
            onSave = { rating, taste ->
                Log.i(TAG, "Saving rating update for: ${selectedBeer?.beer?.name}")
                viewModel.addRating(
                    selectedBeer!!.beer, rating, taste
                )
                showEditRatingDialog = false
            }
        )
    }

    if (showAddDialog) {
        AddBeerPopup(
            onDismiss = {
                Log.d(TAG, "Add dialog cancellation requested")
                pendingCancelAction = { showAddDialog = false }
                showCancelConfirmation = true
            },
            onSave = { newBeer ->
                Log.i(TAG, "Saving new beer: ${newBeer.name}")
                viewModel.addBeer(newBeer)
                showAddDialog = false
            }
        )
    }

    if (showCancelConfirmation) {
        ConfirmationPopup(
            text = "Are you sure you want to cancel?",
            onConfirm = {
                Log.d(TAG, "Cancel confirmed via popup")
                pendingCancelAction()
                showCancelConfirmation = false
            },
            onDismiss = {
                Log.d(TAG, "Cancel confirmation dismissed (staying in dialog)")
                showCancelConfirmation = false
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