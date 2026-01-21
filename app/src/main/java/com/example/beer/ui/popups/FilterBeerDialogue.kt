import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.beer.data.enums.Aftertaste
import com.example.beer.data.enums.Bitterness
import com.example.beer.data.enums.Mouthfeel
import com.example.beer.data.enums.Sweetness
import com.example.beer.ui.popups.EnumTasteDropDown
import com.example.beer.ui.popups.FilterRangeRow
import com.example.beer.ui.rating.FilterState

@Composable
fun FilterBeerDialog(
    filters: FilterState,
    onDismiss: () -> Unit,
    onSearch: (Double, Double, Double, Double, Double, Double, Double, Double, Aftertaste?, Bitterness?, Mouthfeel?, Sweetness?) -> Unit,
) {
    // Internal state for the dialogue inputs
    var minRating by remember { mutableDoubleStateOf(filters.minRating) }
    var maxRating by remember { mutableDoubleStateOf(filters.maxRating) }

    var minTaste by remember { mutableDoubleStateOf(filters.minTaste) }
    var maxTaste by remember { mutableDoubleStateOf(filters.maxTaste) }

    var minLook by remember { mutableDoubleStateOf(filters.minLook) }
    var maxLook by remember { mutableDoubleStateOf(filters.maxLook) }

    var minDrinkability by remember { mutableDoubleStateOf(filters.minDrinkability) }
    var maxDrinkability by remember { mutableDoubleStateOf(filters.maxDrinkability) }


    var selectedAftertaste by remember { mutableStateOf<Aftertaste?>(filters.aftertaste) }
    var selectedBitterness by remember { mutableStateOf<Bitterness?>(filters.bitterness) }
    var selectedMouthfeel by remember { mutableStateOf<Mouthfeel?>(filters.mouthfeel) }
    var selectedSweetness by remember { mutableStateOf<Sweetness?>(filters.sweetness) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Settings", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Numeric Range Row: Rating
                FilterRangeRow(label = "Rating", startValue = minRating, endValue = maxRating,
                    onStartChange = { minRating = it }, onEndChange = { maxRating = it })

                FilterRangeRow(label = "Taste", startValue = minTaste, endValue = maxTaste,
                    onStartChange = { minTaste = it }, onEndChange = { maxTaste = it })

                FilterRangeRow(label = "Look", startValue = minLook, endValue = maxLook,
                    onStartChange = { minLook = it }, onEndChange = { maxLook = it })

                FilterRangeRow(label = "Drinkability", startValue = minDrinkability, endValue = maxDrinkability,
                    onStartChange = { minDrinkability = it }, onEndChange = { maxDrinkability = it })


                HorizontalDivider()

                // Taste Category Dropdowns
                Text("Taste", style = MaterialTheme.typography.titleMedium)


                EnumTasteDropDown(
                    label = "Aftertaste",
                    selected = selectedAftertaste,
                    options = Aftertaste.entries.toTypedArray(),
                    onSelected = { selectedAftertaste = it },
                    displayMapper = { it?.description ?: "None"}
                )

                EnumTasteDropDown(
                    label = "Bitterness",
                    selected = selectedBitterness,
                    options = Bitterness.entries.toTypedArray(),
                    onSelected = { selectedBitterness = it },
                    displayMapper = { it?.description ?: "None"}
                )

                EnumTasteDropDown(
                    label = "Mouthfeel",
                    selected = selectedMouthfeel,
                    options = Mouthfeel.entries.toTypedArray(),
                    onSelected = { selectedMouthfeel = it },
                    displayMapper = { it?.description ?: "None"}
                )

                EnumTasteDropDown(
                    label = "Sweetness",
                    selected = selectedSweetness,
                    options = Sweetness.entries.toTypedArray(),
                    onSelected = { selectedSweetness = it },
                    displayMapper = { it?.description ?: "None"}
                )

            }
        },
        confirmButton = {
            TextButton(onClick = { onSearch(minRating, maxRating, minTaste, maxTaste, minLook, maxLook, minDrinkability, maxDrinkability,
                selectedAftertaste, selectedBitterness, selectedMouthfeel, selectedSweetness) }) {
                Text("SEARCH")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = {
                    minRating = 0.0
                    maxRating = 5.0
                    minTaste = 0.0
                    maxTaste = 5.0

                    minLook = 0.0
                    maxLook = 5.0

                    minDrinkability = 0.0
                    maxDrinkability = 5.0

                    selectedAftertaste = null
                    selectedBitterness = null
                    selectedMouthfeel = null
                    selectedSweetness = null
                }) { Text("RESET") }
                TextButton(onClick = onDismiss) { Text("CANCEL") }
            }
        }
    )
}





