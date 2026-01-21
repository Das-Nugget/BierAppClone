package com.example.beer.ui.popups

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

private const val TAG = "BeerOptionsPopup"

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
                TextButton(onClick = {
                    Log.d(TAG, "Edit Beer option selected")
                    onEditBeer()
                }) {
                    Text(
                        text = "Edit Beer",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0066CC)
                    )
                }

                TextButton(onClick = {
                    Log.d(TAG, "Edit Rating option selected")
                    onEditRating()
                }) {
                    Text(
                        text = "Edit Rating",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0066CC)
                    )
                }

                /**
                 * Delete action is typically followed by a secondary confirmation
                 * dialog in the parent screen to prevent accidental data loss.
                 */
                TextButton(onClick = {
                    Log.d(TAG, "Delete Beer option selected")
                    onDeleteBeer()
                }) {
                    Text(
                        text = "Delete Beer",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0066CC)
                    )
                }
            }
        }
    }
}