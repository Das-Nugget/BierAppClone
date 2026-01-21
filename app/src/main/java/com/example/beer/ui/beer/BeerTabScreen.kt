package com.example.beer.ui.beer

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.beer.data.model.BeerModel
import com.example.beer.data.model.RatedBeer
import com.example.beer.ui.popups.AddBeerPopup
import com.example.beer.ui.popups.AddRatingDialog
import com.example.beer.ui.popups.BeerOptionsPopup
import com.example.beer.ui.popups.ConfirmationPopup
import com.example.beer.ui.searchbar.CustomizableSearchBar
import com.example.beer.ui.theme.beerAmber
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "BeerTabScreen"

@Composable
fun BeerTabScreen(viewModel: BeerTabViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val beers by viewModel.filteredBeers.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var showOptionsDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedBeer by remember { mutableStateOf<RatedBeer?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var showCancelConfirmation by remember { mutableStateOf(false) }
    var showEditRatingDialog by remember { mutableStateOf(false) }

    /**
     * pendingCancelAction stores a lambda of the dialog-closing logic.
     * This allows us to trigger the 'Cancel Confirmation' popup from multiple sources
     * and know which specific dialog to close upon confirmation.
     */
    var pendingCancelAction by remember { mutableStateOf({}) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomizableSearchBar(
                query = searchQuery,
                onQueryChange = {
                    Log.d(TAG, "Search query changed: $it")
                    viewModel.onSearchQueryChange(it)
                },
                onSearch = { Log.i(TAG, "Search executed for: $searchQuery") },
                searchResults = beers.map { it.beer.name },
                onResultClick = { selectedName ->
                    Log.d(TAG, "Search result clicked: $selectedName")
                    viewModel.onSearchQueryChange(selectedName)
                },
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                placeholder = { Text("Search for a beer...") }
            )

            FilledIconButton(
                onClick = {
                    Log.d(TAG, "Add button clicked")
                    showAddDialog = true
                },
                modifier = Modifier.size(56.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = beerAmber,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Beer",
                )
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(beers) { beer ->
                Box(modifier = Modifier.clickable {
                    Log.d(TAG, "Beer selected: ${beer.beer.name}")
                    selectedBeer = beer
                    showOptionsDialog = true
                }) {
                    BeerItem(beer = beer.beer, themeColor = beerAmber)
                }
            }
        }
    }

    if (showOptionsDialog && selectedBeer != null) {
        BeerOptionsPopup(
            onDismiss = { showOptionsDialog = false },
            onEditBeer = {
                Log.d(TAG, "Option: Edit Beer selected")
                showOptionsDialog = false
                showEditDialog = true
            },
            onEditRating = {
                Log.d(TAG, "Option: Edit Rating selected")
                showOptionsDialog = false
                showEditRatingDialog = true
            },
            onDeleteBeer = {
                Log.d(TAG, "Option: Delete Beer selected")
                showOptionsDialog = false
                showDeleteConfirmation = true
            }
        )
    }

    if (showDeleteConfirmation) {
        ConfirmationPopup(
            text = "Are you sure you want to delete?",
            onConfirm = {
                Log.i(TAG, "Deleting beer: ${selectedBeer?.beer?.name}")
                selectedBeer?.let { viewModel.deleteBeer(it.beer) }
                showDeleteConfirmation = false
                selectedBeer = null
            },
            onDismiss = { showDeleteConfirmation = false }
        )
    }

    if (showEditDialog && selectedBeer != null) {
        AddBeerPopup(
            beerToEdit = selectedBeer?.beer,
            onDismiss = {
                pendingCancelAction = { showEditDialog = false }
                showCancelConfirmation = true
            },
            onSave = { updatedBeer ->
                Log.i(TAG, "Updating beer: ${updatedBeer.name}")
                viewModel.updateBeer(updatedBeer)
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
                Log.i(TAG, "Saving rating for beer: ${selectedBeer?.beer?.name}")
                viewModel.addRating(selectedBeer!!.beer, rating, taste)
                showEditRatingDialog = false
            }
        )
    }

    if (showAddDialog) {
        AddBeerPopup(
            onDismiss = {
                pendingCancelAction = { showAddDialog = false }
                showCancelConfirmation = true
            },
            onSave = { newBeer ->
                Log.i(TAG, "Adding new beer: ${newBeer.name}")
                viewModel.addBeer(newBeer)
                showAddDialog = false
            }
        )
    }

    if (showCancelConfirmation) {
        ConfirmationPopup(
            text = "Are you sure you want to cancel?",
            onConfirm = {
                Log.d(TAG, "Cancel confirmed")
                pendingCancelAction()
                showCancelConfirmation = false
            },
            onDismiss = { showCancelConfirmation = false }
        )
    }
}

@Composable
fun BeerItem(beer: BeerModel, themeColor: Color) {
    val dateFormatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val formattedDate = dateFormatter.format(Date(beer.createdAt))

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
                Box(modifier = Modifier.size(80.dp).background(themeColor)) {
                    if (beer.imageURI != null) {
                        Image(
                            painter = rememberAsyncImagePainter(beer.imageURI),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("IMG", color = Color.White, modifier = Modifier.align(Alignment.Center))
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

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

                Text(
                    text = formattedDate,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
        }
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}