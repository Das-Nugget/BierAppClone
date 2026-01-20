package com.example.beer.ui.popups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EditBeerDialogue(
    onEditBeer: () -> Unit,
    onEditRating: () -> Unit,
    onDeleteBeer: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        // leaving buttons empty to use a custom column of actions
        confirmButton = {},
        dismissButton = {},
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                TextButton(onClick = { onEditBeer(); onDismiss() }) {
                    Text("Edit Beer", style = MaterialTheme.typography.headlineSmall, color = Color(0xFF0066CC))
                }
                TextButton(onClick = { onEditRating(); onDismiss() }) {
                    Text("Edit Rating", style = MaterialTheme.typography.headlineSmall, color = Color(0xFF0066CC))
                }
                TextButton(onClick = { onDeleteBeer(); onDismiss() }) {
                    Text("Delete Beer", style = MaterialTheme.typography.headlineSmall, color = Color(0xFF0066CC))
                }

                Spacer(modifier = Modifier.size(16.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("CANCEL", style = MaterialTheme.typography.labelLarge, color = Color(0xFF0066CC))
                }
            }
        }
    )
}