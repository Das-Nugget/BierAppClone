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

private const val TAG = "ConfirmationPopup"

@Composable
fun ConfirmationPopup(
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 32.dp),
                    color = Color.Black
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = {
                        Log.i(TAG, "User confirmed action: $text")
                        onConfirm()
                    }) {
                        Text(
                            text = "YES",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0066CC)
                        )
                    }

                    /**
                     * Dismissing the popup typically resets the pending action state
                     * in the parent screen without executing the logic.
                     */
                    TextButton(onClick = {
                        Log.d(TAG, "User dismissed/canceled action: $text")
                        onDismiss()
                    }) {
                        Text(
                            text = "NO",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0066CC)
                        )
                    }
                }
            }
        }
    }
}