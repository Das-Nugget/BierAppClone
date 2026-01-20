package com.example.beer.ui.popups

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun BeerOptionsPopup(
    onDismiss: () -> Unit,
    onEditBeer: () -> Unit,
    onEditRating: () -> Unit,
    onDeleteBeer: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                TextButton(onClick = onEditBeer) {
                    Text("Edit Beer", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0066CC))
                }
                TextButton(onClick = onEditRating) {
                    Text("Edit Rating", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0066CC))
                }
                TextButton(onClick = onDeleteBeer) {
                    Text("Delete Beer", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0066CC))
                }
            }
        }
    }
}