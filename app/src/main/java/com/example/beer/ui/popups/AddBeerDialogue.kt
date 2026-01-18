package com.example.beer.ui.popups


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.beer.data.enums.BeerType
import com.example.beer.data.model.BeerModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddBeerDialog(
    beer: BeerModel? = null,
    onDismiss: () -> Unit,
    onSave: (BeerModel) -> Unit,
) {
    var name by remember(beer) { mutableStateOf(beer?.name ?: "") }
    var producer by remember(beer) { mutableStateOf(beer?.producer ?: "") }
    var alcoholPercentage by remember(beer) { mutableDoubleStateOf(beer?.alcoholPercentage ?: 0.0) }
    var type by remember(beer) { mutableStateOf(beer?.type ?: BeerType.NULLTYPE) }
    var imageURI by remember(beer) { mutableStateOf(beer?.imageURI ?: "") }
    var price by remember(beer) { mutableDoubleStateOf(beer?.price ?: 0.0) }
    var note by remember(beer) { mutableStateOf(beer?.note ?: "") }

    // In BeerFormDialog
    val context = LocalContext.current

    // Launcher for picking from gallery (Photo Picker)
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val localUri = copyImageToInternalStorage(context, uri)
            if (localUri != null) {
                imageURI = localUri
            }
        }
    }

    // 1. Permission State (Using Accompanist is the standard way)
// Add implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

// 2. Camera Launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            imageURI = saveImageToInternalStorage(context, bitmap).toString()
        }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (beer == null) "Add Beer" else "Edit Beer") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Image Placeholder
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(1.dp, Color.Black)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageURI.isEmpty()) {
                            Text("IMG")
                        } else {
                            AsyncImage(
                                model = imageURI,
                                contentDescription = "Selected Beer Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }

                    // Upload Button
                    ImageActionButton(
                        icon = Icons.Default.Folder,
                        label = "Upload an image",
                        onClick = { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
                    )

                    // Camera Button
                    ImageActionButton(
                        icon = Icons.Default.PhotoCamera,
                        label = "Take a photo",
                        onClick = {
                            when {
                                cameraPermissionState.status.isGranted -> {
                                    cameraLauncher.launch(null)
                                }
                                else -> {
                                    cameraPermissionState.launchPermissionRequest()
                                }
                            }
                        }
                    )
                }

                Text("Attributes", style = MaterialTheme.typography.titleLarge)

                AttributeStringInputField("Name", name) { name = it }

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Type", modifier = Modifier.weight(0.3f))
                    Box(modifier = Modifier.weight(0.7f)) {
                        BeerTypeSelector(type) { type = it }
                    }
                }

                AttributeStringInputField("Producer", producer) { producer = it }
                AttributeDoubleInputField("Alcohol Percentage", alcoholPercentage) { alcoholPercentage = it }
                AttributeDoubleInputField("price", price) { price = it }
                AttributeStringInputField("note", note) { note = it }
            }
        },
        confirmButton = {
            TextButton(onClick = { val result = beer?.
            copy(
                name = name,
                producer = producer,
                alcoholPercentage = alcoholPercentage,
                type = type,
                price = price,
                note = note,
                imageURI = imageURI
            )
                ?: // Create brand new object for adding
                BeerModel(
                    name = name,
                    producer = producer,
                    alcoholPercentage = alcoholPercentage,
                    type = type,
                    price = price,
                    note = note,
                    imageURI = imageURI,
                    id = 0,
                    createdAt = System.currentTimeMillis(),
                    ratingId = null,
                    tasteId = null
                )
                onSave(result) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("CANCEL") }
        }
    )
}









