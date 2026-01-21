package com.example.beer.ui.popups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.beer.data.enums.BeerType


@Composable
fun AttributeStringInputField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
) {
    var textState by remember(value) { mutableStateOf(value)}
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.weight(0.3f))
        Column(modifier = Modifier.weight(0.7f),
            horizontalAlignment = Alignment.CenterHorizontally) {
            // Numeric Input for Value
            BasicTextField(
                value = textState,
                onValueChange = {newValue ->
                    textState = newValue
                    onChange(newValue)
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun AttributeDoubleInputField(
    label: String,
    value: Double,
    onChange: (Double) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.weight(1f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Numeric Input for Value
            NumericUnderlinedInputField(
                value = value,
                onValueChange = onChange
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Enum<T>> EnumTasteDropDown(
    label: String,
    selected: T?, // Accept null
    options: Array<T>,
    onSelected: (T?) -> Unit, // Allow passing null back
    displayMapper: (T?) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.weight(1f))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.width(180.dp)
        ) {
            OutlinedTextField(
                value = displayMapper(selected),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier.menuAnchor(),
                textStyle = MaterialTheme.typography.bodyMedium
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                // 1. Add an explicit "None" option at the top
                DropdownMenuItem(
                    text = { Text("None") },
                    onClick = {
                        onSelected(null)
                        expanded = false
                    }
                )

                // 2. The rest of the enum entries
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = displayMapper(option)) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
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
            // Numeric Input for Start Value
            NumericUnderlinedInputField(
                value = startValue,
                onValueChange = onStartChange
            )

            Text(" - ", modifier = Modifier.padding(horizontal = 4.dp))

            // Numeric Input for End Value
            NumericUnderlinedInputField(
                value = endValue,
                onValueChange = onEndChange
            )
        }
    }
}

@Composable
fun NumericUnderlinedInputField(
    value: Double,
    onValueChange: (Double) -> Unit
) {
    // We maintain a local string state so the user can type decimal points/commas
    var textState by remember(value) { mutableStateOf(value.toString().replace(".", ",")) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BasicTextField(
            value = textState,
            onValueChange = { newValue ->
                // Allow the user to type numeric characters and decimal separators
                val filteredValue = newValue.replace(".", ",")
                textState = filteredValue

                // Attempt to parse and notify the parent only if it's a valid Double
                filteredValue.replace(",", ".").toDoubleOrNull()?.let { parsedDouble ->
                    onValueChange(parsedDouble)
                }
            },
            modifier = Modifier.width(45.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        // Underline style from your graphic
        HorizontalDivider(
            modifier = Modifier.width(45.dp),
            thickness = 1.dp,
            color = Color.Gray
        )
    }
}