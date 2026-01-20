package com.example.beer.ui.beer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.example.beer.ui.popups.AddBeerPopup
import com.example.beer.ui.popups.BeerOptionsPopup
import com.example.beer.ui.popups.ConfirmationPopup
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import com.example.beer.ui.theme.beerAmber
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Alignment
import com.example.beer.ui.searchbar.CustomizableSearchBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beer.data.model.BeerModel
import java.text.SimpleDateFormat
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material3.AlertDialogDefaults.containerColor
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun BeerTabScreen(viewModel: BeerTabViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val beers by viewModel.filteredBeers.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var showOptionsDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedBeer by remember { mutableStateOf<BeerModel?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var showCancelConfirmation by remember { mutableStateOf(false) }
    var pendingCancelAction by remember { mutableStateOf({}) }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Hintergrund für den ganzen Screen
    ) {
        // --- TOPBAR / SEARCH BEREICH ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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

            // Der gelbe Plus-Button aus deinem Mockup
            FilledIconButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.size(56.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = beerAmber,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add, // Import: androidx.compose.material.icons.filled.Add
                    contentDescription = "Add Beer",
                )
            }
        }

        // --- LISTE ---
        // Die LazyColumn nimmt nun den restlichen Platz ein, ohne die Suchleiste zu überlappen
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(beers) { beer ->
                Box(modifier = Modifier.clickable {
                    selectedBeer = beer
                    showOptionsDialog = true
                }) {
                    BeerItem(beer = beer, themeColor = beerAmber)
                }
            }
        }
    }
        // Dialog 1: Auswahl (Edit/Delete)
        if (showOptionsDialog && selectedBeer != null) {
            BeerOptionsPopup(
                onDismiss = { showOptionsDialog = false },
                onEditBeer = {
                    showOptionsDialog = false
                    showEditDialog = true
                },
                onEditRating = { /* Noch nicht implementiert */ },
                onDeleteBeer = {
                    showOptionsDialog = false
                    showDeleteConfirmation = true
                }
            )
        }

        if (showDeleteConfirmation) {
            ConfirmationPopup(
                text = "Are you sure you want to delete?",
                onConfirm = {
                    selectedBeer?.let { viewModel.deleteBeer(it) }
                    showDeleteConfirmation = false
                    selectedBeer = null
                },
                onDismiss = { showDeleteConfirmation = false }
            )
        }

        // Dialog 2: Bearbeiten (Nutzt das angepasste AddBeerPopup)
        if (showEditDialog && selectedBeer != null) {
            AddBeerPopup(
                beerToEdit = selectedBeer,
                onDismiss = {
                    pendingCancelAction = {showEditDialog = false }
                    showCancelConfirmation = true
                },
                onSave = { updatedBeer ->
                    viewModel.updateBeer(updatedBeer)
                    showEditDialog = false
                }
            )
        }

        // Dialog 3: Neu Erstellen
        if (showAddDialog) {
            AddBeerPopup(
                onDismiss = {
                    pendingCancelAction = {showAddDialog = false }
                    showCancelConfirmation = true
                },
                onSave = { newBeer ->
                    viewModel.addBeer(newBeer)
                    showAddDialog = false
                }
            )
        }

        if (showCancelConfirmation) {
            ConfirmationPopup(
                text = "Are you sure you want to cancel?",
                onConfirm = {
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
                // 1. Spalte: Bild-Platzhalter
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

                // 3. Spalte: Datum ganz rechts
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
