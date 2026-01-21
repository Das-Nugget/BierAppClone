package com.example.beer.ui.popups

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.beer.data.enums.Aftertaste
import com.example.beer.data.enums.Bitterness
import com.example.beer.data.enums.Mouthfeel
import com.example.beer.data.enums.Sweetness
import com.example.beer.data.model.RatingModel
import com.example.beer.data.model.TasteModel

private const val TAG = "AddRatingDialog"

@Composable
fun AddRatingDialog(
    rating: RatingModel? = null,
    tasteModel: TasteModel? = null,
    onDismiss: () -> Unit,
    onSave: (RatingModel, TasteModel) -> Unit,
) {
    var taste by remember(rating) { mutableIntStateOf(rating?.taste ?: 0) }
    var look by remember(rating) { mutableIntStateOf(rating?.look ?: 0) }
    var drinkability by remember(rating) { mutableIntStateOf(rating?.drinkability ?: 0) }
    var overallRating by remember(rating) { mutableDoubleStateOf(rating?.overallRating ?: 0.0) }

    var selectedSweetness by remember(tasteModel) { mutableStateOf(tasteModel?.sweetness) }
    var selectedBitterness by remember(tasteModel) { mutableStateOf(tasteModel?.bitterness) }
    var selectedAftertaste by remember(tasteModel) { mutableStateOf(tasteModel?.aftertaste) }
    var selectedMouthfeel by remember(tasteModel) { mutableStateOf(tasteModel?.mouthfeel) }
    var note by remember(tasteModel) { mutableStateOf(tasteModel?.note ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (rating == null) "Add Rating" else "Edit Rating") },
        text = {
            Column(
                Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Rating", style = MaterialTheme.typography.titleLarge)

                AttributeDoubleInputField("Taste", taste.toDouble()) { taste = it.toInt() }
                AttributeDoubleInputField("Look", look.toDouble()) { look = it.toInt() }
                AttributeDoubleInputField("Drinkability", drinkability.toDouble()) { drinkability = it.toInt() }
                AttributeDoubleInputField("Overall Rating", overallRating) { overallRating = it }

                Text("Taste", style = MaterialTheme.typography.titleMedium)

                EnumTasteDropDown(
                    label = "Aftertaste",
                    selected = selectedAftertaste,
                    options = Aftertaste.entries.toTypedArray(),
                    onSelected = {
                        Log.v(TAG, "Aftertaste selected: $it")
                        selectedAftertaste = it
                    },
                    displayMapper = { it?.description ?: "None" }
                )

                EnumTasteDropDown(
                    label = "Bitterness",
                    selected = selectedBitterness,
                    options = Bitterness.entries.toTypedArray(),
                    onSelected = {
                        Log.v(TAG, "Bitterness selected: $it")
                        selectedBitterness = it
                    },
                    displayMapper = { it?.description ?: "None" }
                )

                EnumTasteDropDown(
                    label = "Mouthfeel",
                    selected = selectedMouthfeel,
                    options = Mouthfeel.entries.toTypedArray(),
                    onSelected = {
                        Log.v(TAG, "Mouthfeel selected: $it")
                        selectedMouthfeel = it
                    },
                    displayMapper = { it?.description ?: "None" }
                )

                EnumTasteDropDown(
                    label = "Sweetness",
                    selected = selectedSweetness,
                    options = Sweetness.entries.toTypedArray(),
                    onSelected = {
                        Log.v(TAG, "Sweetness selected: $it")
                        selectedSweetness = it
                    },
                    displayMapper = { it?.description ?: "None" }
                )

                AttributeStringInputField("Note", note) { note = it }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                Log.d(TAG, "Confirm clicked. Processing result models.")

                /**
                 * If an existing model exists, we use .copy() to preserve the ID and other metadata.
                 * Otherwise, we instantiate a new model with an ID of 0 for database insertion.
                 */
                val resultRating = rating?.copy(
                    taste = taste,
                    look = look,
                    drinkability = drinkability,
                    overallRating = overallRating
                ) ?: RatingModel(
                    id = 0,
                    taste = taste,
                    look = look,
                    drinkability = drinkability,
                    overallRating = overallRating
                )

                val resultTaste = tasteModel?.copy(
                    sweetness = selectedSweetness,
                    bitterness = selectedBitterness,
                    aftertaste = selectedAftertaste,
                    mouthfeel = selectedMouthfeel,
                    note = note
                ) ?: TasteModel(
                    id = 0,
                    sweetness = selectedSweetness,
                    bitterness = selectedBitterness,
                    aftertaste = selectedAftertaste,
                    mouthfeel = selectedMouthfeel,
                    note = note
                )

                Log.i(TAG, "Saving rating: $resultRating and taste: $resultTaste")
                onSave(resultRating, resultTaste)
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                Log.d(TAG, "Cancel clicked")
                onDismiss()
            }) {
                Text("CANCEL")
            }
        }
    )
}