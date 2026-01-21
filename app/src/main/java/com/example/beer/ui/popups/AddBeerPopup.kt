package com.example.beer.ui.popups

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.example.beer.data.enums.BeerType
import com.example.beer.data.model.BeerModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

private const val TAG = "AddBeerPopup"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddBeerPopup(
    beerToEdit: BeerModel? = null,
    onDismiss: () -> Unit,
    onSave: (BeerModel) -> Unit
) {
    var name by remember { mutableStateOf(beerToEdit?.name ?: "") }
    var producer by remember { mutableStateOf(beerToEdit?.producer ?: "") }
    var alcohol by remember { mutableStateOf(beerToEdit?.alcoholPercentage?.toString() ?: "") }
    var price by remember { mutableStateOf(beerToEdit?.price?.toString() ?: "") }
    var note by remember { mutableStateOf(beerToEdit?.note ?: "") }
    var selectedType by remember { mutableStateOf(beerToEdit?.type ?: BeerType.LAGER) }
    var expanded by remember { mutableStateOf(false) }
    var typeSearchQuery by remember { mutableStateOf(selectedType.styleName) }
    var imageUri by remember { mutableStateOf<Uri?>(beerToEdit?.imageURI?.let { Uri.parse(it) }) }

    val context = LocalContext.current

    /**
     * copyImageToInternalStorage ensures that images picked from the gallery
     * are persisted locally within the app's directory so the URI remains valid
     * even if the original source is moved or deleted.
     */
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            Log.d(TAG, "Image selected from gallery: $uri")
            val localUri = copyImageToInternalStorage(context, uri)
            if (localUri != null) {
                imageUri = localUri.toUri()
                Log.i(TAG, "Image successfully copied to internal storage: $imageUri")
            }
        }
    }

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            Log.d(TAG, "Photo captured successfully")
            val localUri = saveImageToInternalStorage(context, bitmap)
            imageUri = localUri?.toUri()
            Log.i(TAG, "Captured photo saved to: $imageUri")
        }
    }

    val filteredTypes = remember(typeSearchQuery) {
        BeerType.entries.filter {
            it.styleName.contains(typeSearchQuery, ignoreCase = true)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Beer Settings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.Black
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White)
                        }
                    }

                    Button(
                        onClick = {
                            Log.d(TAG, "Launching gallery picker")
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        modifier = Modifier.weight(1f).height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3), contentColor = Color.White),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Folder, contentDescription = null)
                            Text("Upload an image", fontSize = 10.sp)
                        }
                    }

                    Button(
                        onClick = {
                            Log.d(TAG, "Camera button clicked. Permission granted: ${cameraPermissionState.status.isGranted}")
                            when {
                                cameraPermissionState.status.isGranted -> {
                                    cameraLauncher.launch(null)
                                }

                                else -> {
                                    cameraPermissionState.launchPermissionRequest()
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3), contentColor = Color.White),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null)
                            Text("Take a photo", fontSize = 10.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Attributes", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                AttributeInput(label = "Name", value = name, onValueChange = { name = it })
                AttributeInput(label = "Producer", value = producer, onValueChange = { producer = it })

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Type", modifier = Modifier.weight(1f), color = Color.Gray)

                    /**
                     * ExposedDropdownMenuBox implements a searchable dropdown.
                     * We use a TextField to filter the enum entries based on user input.
                     */
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        modifier = Modifier.weight(2f)
                    ) {
                        TextField(
                            value = typeSearchQuery,
                            onValueChange = {
                                typeSearchQuery = it
                                expanded = true
                            },
                            readOnly = false,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            modifier = Modifier.menuAnchor(),
                            singleLine = true
                        )

                        if (filteredTypes.isNotEmpty()) {
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                filteredTypes.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(text = type.styleName) },
                                        onClick = {
                                            Log.v(TAG, "Beer type selected: ${type.styleName}")
                                            selectedType = type
                                            typeSearchQuery = type.styleName
                                            expanded = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                }
                            }
                        }
                    }
                }

                AttributeInput(
                    label = "Alcohol %",
                    value = alcohol,
                    onValueChange = { alcohol = it },
                    keyboardType = KeyboardType.Decimal
                )
                AttributeInput(
                    label = "Price",
                    value = price,
                    onValueChange = { price = it },
                    keyboardType = KeyboardType.Decimal
                )
                AttributeInput(label = "Note", value = note, onValueChange = { note = it })

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = {
                        Log.d(TAG, "Cancel button clicked")
                        onDismiss()
                    }) {
                        Text("CANCEL", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    TextButton(onClick = {
                        if (name.isNotBlank()) {
                            Log.i(TAG, "Saving beer: $name")
                            onSave(
                                BeerModel(
                                    id = beerToEdit?.id ?: 0,
                                    name = name,
                                    producer = producer,
                                    alcoholPercentage = alcohol.toDoubleOrNull() ?: 0.0,
                                    price = price.toDoubleOrNull() ?: 0.0,
                                    note = note,
                                    type = selectedType,
                                    imageURI = imageUri?.toString(),
                                    ratingId = beerToEdit?.ratingId,
                                    tasteId = beerToEdit?.tasteId,
                                    createdAt = beerToEdit?.createdAt ?: System.currentTimeMillis()
                                )
                            )
                        } else {
                            Log.w(TAG, "Save attempt failed: Name is blank")
                        }
                    }) {
                        Text("SAVE", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AttributeInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.weight(1f), color = Color.Gray)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(2f),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Black,
                focusedTextColor = Color.Black
            ),
            singleLine = true
        )
    }
}