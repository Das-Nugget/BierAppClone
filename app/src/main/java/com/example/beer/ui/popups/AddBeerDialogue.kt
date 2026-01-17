package com.example.beer.ui.popups

import NumericUnderlinedInputField
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import coil.compose.AsyncImage
import com.example.beer.data.enums.BeerType
import com.example.beer.data.model.BeerModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.io.FileOutputStream

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

                AttributeStringInputField("Name", "") { name = it }

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

                AttributeStringInputField("Producer", "") { producer = it }
                AttributeDoubleInputField("Alcohol Percentage", 0.0) { alcoholPercentage = it }
                AttributeDoubleInputField("price", 0.0) { price = it }
                AttributeStringInputField("note", "") { note = it }
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

@Composable
fun ImageActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.size(height = 100.dp, width = 110.dp),
        contentPadding = PaddingValues(4.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(36.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeerTypeSelector(
    selectedType: BeerType?,
    onTypeSelected: (BeerType) -> Unit
) {
    var searchQuery by remember(selectedType) { mutableStateOf(selectedType?.styleName ?: "") }
    var expanded by remember { mutableStateOf(false) }

    val filteredOptions = remember(searchQuery) {
        BeerType.entries.filter {
            it.styleName.contains(searchQuery, ignoreCase = true)
        }.sortedBy { it.styleName }
    }

    // We use a Box to anchor the menu correctly
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                expanded = it.isNotEmpty()
            },
            label = { Text("Beer Style") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
            singleLine = true
        )

        // Using standard DropdownMenu for 'properties' support
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            // This is the magic: focusable = false allows the keyboard to stay active
            // and the cursor to stay in the TextField while you type
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            modifier = Modifier
                .fillMaxWidth(0.6f) // Match the width of the text field roughly
        ) {
            filteredOptions.take(10).forEach { option -> // Limit results for performance
                DropdownMenuItem(
                    text = { Text(option.styleName) },
                    onClick = {
                        searchQuery = option.styleName
                        onTypeSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun copyImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        // Create a unique filename
        val fileName = "beer_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        // Copy the data from the URI to your local file
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        // Return the absolute path or a file URI
        Uri.fromFile(file).toString()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun saveImageToInternalStorage(context: Context, bitmap: Bitmap): String? {
    return try {
        val fileName = "beer_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        }

        Uri.fromFile(file).toString()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}