import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.beer.data.enums.Aftertaste
import com.example.beer.data.enums.Bitterness
import com.example.beer.data.enums.Mouthfeel
import com.example.beer.data.enums.Sweetness
import com.example.beer.ui.popups.EnumTasteDropDown
import com.example.beer.ui.popups.NumericUnderlinedInputField
import com.example.beer.ui.rating.FilterState

private const val TAG = "FilterBeerDialog"

@Composable
fun FilterBeerDialog(
    filters: FilterState,
    onDismiss: () -> Unit,
    onSearch: (Double, Double, Double, Double, Double, Double, Double, Double, Aftertaste?, Bitterness?, Mouthfeel?, Sweetness?) -> Unit,
) {
    /**
     * We maintain internal mutable state for the dialog inputs.
     * This decouples the 'live' UI changes from the actual filter state in the ViewModel,
     * allowing users to discard changes by clicking CANCEL or apply them via SEARCH.
     */
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
                Text("Taste", style = MaterialTheme.typography.titleMedium)

                EnumTasteDropDown(
                    label = "Aftertaste",
                    selected = selectedAftertaste,
                    options = Aftertaste.entries.toTypedArray(),
                    onSelected = {
                        Log.v(TAG, "Filter changed - Aftertaste: $it")
                        selectedAftertaste = it
                    },
                    displayMapper = { it?.description ?: "None" }
                )

                EnumTasteDropDown(
                    label = "Bitterness",
                    selected = selectedBitterness,
                    options = Bitterness.entries.toTypedArray(),
                    onSelected = {
                        Log.v(TAG, "Filter changed - Bitterness: $it")
                        selectedBitterness = it
                    },
                    displayMapper = { it?.description ?: "None" }
                )

                EnumTasteDropDown(
                    label = "Mouthfeel",
                    selected = selectedMouthfeel,
                    options = Mouthfeel.entries.toTypedArray(),
                    onSelected = {
                        Log.v(TAG, "Filter changed - Mouthfeel: $it")
                        selectedMouthfeel = it
                    },
                    displayMapper = { it?.description ?: "None" }
                )

                EnumTasteDropDown(
                    label = "Sweetness",
                    selected = selectedSweetness,
                    options = Sweetness.entries.toTypedArray(),
                    onSelected = {
                        Log.v(TAG, "Filter changed - Sweetness: $it")
                        selectedSweetness = it
                    },
                    displayMapper = { it?.description ?: "None" }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                Log.i(TAG, "Search triggered with current filter settings")
                onSearch(
                    minRating, maxRating, minTaste, maxTaste, minLook, maxLook,
                    minDrinkability, maxDrinkability, selectedAftertaste,
                    selectedBitterness, selectedMouthfeel, selectedSweetness
                )
            }) {
                Text("SEARCH")
            }
        },
        dismissButton = {
            Row {
                /**
                 * Reset logic restores all internal state variables to default values (0.0 to 5.0).
                 * Note that this does not trigger an immediate search; user must still click SEARCH.
                 */
                TextButton(onClick = {
                    Log.d(TAG, "Filters reset to defaults")
                    minRating = 0.0; maxRating = 5.0
                    minTaste = 0.0; maxTaste = 5.0
                    minLook = 0.0; maxLook = 5.0
                    minDrinkability = 0.0; maxDrinkability = 5.0
                    selectedAftertaste = null
                    selectedBitterness = null
                    selectedMouthfeel = null
                    selectedSweetness = null
                }) { Text("RESET") }

                TextButton(onClick = {
                    Log.d(TAG, "Filter dialog dismissed")
                    onDismiss()
                }) { Text("CANCEL") }
            }
        }
    )
}

@Composable
fun FilterRangeRow(
    label: String,
    startValue: Double,
    endValue: Double,
    onStartChange: (Double) -> Unit,
    onEndChange: (Double) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.weight(1f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            NumericUnderlinedInputField(
                value = startValue,
                onValueChange = onStartChange
            )

            Text(" - ", modifier = Modifier.padding(horizontal = 4.dp))
            NumericUnderlinedInputField(
                value = endValue,
                onValueChange = onEndChange
            )
        }
    }
}