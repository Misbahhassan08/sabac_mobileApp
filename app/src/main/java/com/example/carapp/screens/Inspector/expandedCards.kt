package com.example.carapp.screens.Inspector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card

import androidx.compose.material3.Icon

import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.carapp.R
import com.example.carapp.assets.AssetHelper
import com.example.carapp.models.InspectionModel
import com.example.carapp.screens.AddImage
import com.example.carapp.screens.getToken
import kotlinx.coroutines.launch
import java.io.File
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.window.Dialog
import com.google.gson.JsonObject
import java.io.FileOutputStream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carapp.assets.cardColor
import com.example.carapp.assets.redcolor


/*
@Composable
fun TestCase(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    val selectedImages = remember { mutableStateListOf<String>() }
    val imagesError = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val viewModel: UploadViewModel = viewModel()
//    var uploadResults by remember { mutableStateOf<List<String>?>(null) }
    var uploadResults by remember { mutableStateOf<List<Pair<String, JsonObject?>>?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header row with title and expand/collapse icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Test Case",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            if (expanded) {
                Inspectiod(
                    selectedImages = selectedImages,
                    showError = imagesError.value,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                if (selectedImages.isNotEmpty()) {
                    viewModel.uploadImages(selectedImages) { results ->
                        uploadResults = results
                        // Print results to console
                        results.forEach { result ->
                            println("Upload Result: $result")
                        }
                        // Clear temp assets after upload
                        AssetHelper.clearTempAssets(context)
                    }
                } else {
                    Toast.makeText(context, "No images to upload", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = redcolor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Submit report", fontSize = 16.sp)
        }
    }
}
*/
//@Composable
/*fun TestCase(navController: NavController) {
    var expandedCard1 by remember { mutableStateOf(false) }
    var expandedCard2 by remember { mutableStateOf(false) }
    val selectedImagesCard1 = remember { mutableStateListOf<String>() }
    val selectedImagesCard2 = remember { mutableStateListOf<String>() }
    val imagesError = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val viewModel: UploadViewModel = viewModel()
    var uploadResults by remember { mutableStateOf<List<Pair<String, JsonObject?>>?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = { expandedCard1 = !expandedCard1 }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Test Case",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { expandedCard1 = !expandedCard1 }) {
                        Icon(
                            imageVector = if (expandedCard1) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                            contentDescription = if (expandedCard1) "Collapse" else "Expand"
                        )
                    }
                }

                if (expandedCard1) {
                    Inspectiod(
                        selectedImages = selectedImagesCard1,
                        showError = imagesError.value,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        cardName = "Test Case"
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Test Case 2",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { expandedCard2 = !expandedCard2 }) {
                        Icon(
                            imageVector = if (expandedCard2) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                            contentDescription = if (expandedCard2) "Collapse" else "Expand"
                        )
                    }
                }

                if (expandedCard2) {
                    Inspectiod(
                        selectedImages = selectedImagesCard2,
                        showError = imagesError.value,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        cardName = "Test Case2"
                    )
                }
            }
        }

        Button(
            onClick = {
                val allImages = selectedImagesCard1 + selectedImagesCard2
                if (allImages.isNotEmpty()) {
                    viewModel.uploadImages(allImages) { results ->
                        uploadResults = results
                        results.forEach { result ->
                            println("Upload Result: $result")
                        }
                        AssetHelper.clearTempAssets(context)
                    }
                } else {
                    Toast.makeText(context, "No images to upload", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = redcolor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Submit report", fontSize = 16.sp)
        }
    }
}

@Composable
fun Inspectiod(
    selectedImages: MutableList<String>,
    showError: Boolean,
    modifier: Modifier = Modifier,
    cardName: String
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .background(cardColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Add Images",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = if (showError && selectedImages.isEmpty()) MaterialTheme.colorScheme.error else Color.Unspecified
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = if (showError && selectedImages.isEmpty()) MaterialTheme.colorScheme.error else Color.Gray
                )
            }

            if (expanded) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                Column {
                    if (showError && selectedImages.isEmpty()) {
                        Text(
                            text = "At least one image is required",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    if (selectedImages.isEmpty()) {
                        Inspectie(selectedImages, cardName)
                    } else {
                        Inspectir(selectedImages, selectedImages, cardName)
                    }
                }
            }
        }
    }
}


@Composable
fun Inspectie(
    sliderList: MutableList<String>,
    cardName: String
) {
    val context = LocalContext.current
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera(context, sliderList) { uri ->
                tempImageUri = uri
            }
        } else {
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    val launcherGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
//            val fileName = "Gallery_Bonnet_id_App${System.currentTimeMillis()}.png"
            val fileName = "Gallery_${cardName.replace(" ", "_")}_Inspector_Id_App${System.currentTimeMillis()}.png"
            Log.d("Renamed", "Gallery image selected, original URI: $uri")
            Log.d("Renamed", "New filename will be: $fileName")

            val savedPath = AssetHelper.saveImageToTempAssets(context, it, fileName)
            sliderList.add(savedPath)

            Log.d("Renamed", "Image added to sliderList at index ${sliderList.size - 1}")
        }
    }

    val launcherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempImageUri != null) {
            tempImageUri?.let { uri ->
//                val fileName = "Camera_Bonnet_id_App_${System.currentTimeMillis()}.png"
                val fileName = "Camera_${cardName.replace(" ", "_")}_Inspector_Id_App${System.currentTimeMillis()}.png"
                Log.d("Renamed", "Camera image ready to save as: $fileName")

                val savedPath = AssetHelper.saveImageToTempAssets(context, uri, fileName)
                sliderList.add(savedPath)

                Log.d("Renamed", "Camera image saved and added to sliderList")
            }
        }
    }

    fun handleCameraClick() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                launchCamera(context, sliderList) { uri ->
                    tempImageUri = uri
                    launcherCamera.launch(uri)
                }
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
            .clickable {
                showImagePickerDialog(context, launcherGallery, ::handleCameraClick)
            },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, Color.Gray),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.camera_icon),
                contentDescription = "Add photo",
                tint = Color.Gray,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Add photo", color = Color.Gray)
            Text(
                "Tap to select or capture images",
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

fun launchCamera(
    context: Context,
    sliderList: MutableList<String>,
    onUriReady: (Uri) -> Unit
) {
    val imageFile = File.createTempFile("IMG_", ".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )
    onUriReady(uri)
}

fun showImagePickerDialog(
    context: Context,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
    onCamera: () -> Unit
) {
    AlertDialog.Builder(context).apply {
        setTitle("Select Image")
        setItems(arrayOf("Camera", "Gallery")) { _, which ->
            when (which) {
                0 -> onCamera()
                1 -> galleryLauncher.launch("image/*")
            }
        }
        setNegativeButton("Cancel", null)
    }.show()
}

*//*
@Composable
fun Inspectir(
    pages: MutableList<String>,
    sliderList: MutableList<String>
) {
    val pagerState = rememberPagerState { pages.size + 1 }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp) // Reduced height for better fit
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (page < pages.size) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        val painter = rememberAsyncImagePainter(model = pages[page])
                        Image(
                            painter = painter,
                            contentDescription = "Page $page",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    IconButton(
                        onClick = { pages.removeAt(page) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(36.dp)
                            .background(Color.Red, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove Image",
                            tint = Color.White
                        )
                    }
                } else {
                    AddImage(sliderList)
                }
            }
        }

        // Navigation arrows
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(pagerState.currentPage - 1)
                    }
                },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x26000000))
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "", tint = Color.White)
            }
            IconButton(
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(pagerState.currentPage + 1)
                    }
                },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x26000000))
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "", tint = Color.White)
            }
        }
    }
}

*//*

@Composable
fun Inspectir(
    pages: MutableList<String>,
    sliderList: MutableList<String>,
    cardName: String
) {
    val pagerState = rememberPagerState { pages.size + 1 }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (page < pages.size) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        val painter = rememberAsyncImagePainter(model = pages[page])
                        Image(
                            painter = painter,
                            contentDescription = "Page $page",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    IconButton(
                        onClick = { pages.removeAt(page) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(36.dp)
                            .background(Color.Red, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove Image",
                            tint = Color.White
                        )
                    }
                } else {
                    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

                    val cameraPermissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission()
                    ) { isGranted ->
                        if (isGranted) {
                            launchCamera(context, sliderList) { uri ->
                                tempImageUri = uri
                            }
                        } else {
                            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
                        }
                    }

                    val launcherGallery = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()
                    ) { uri: Uri? ->
                        uri?.let {
//                            val fileName = "Gallery_Bonnet_id_App${System.currentTimeMillis()}.png"
                            val fileName = "Gallery_${cardName.replace(" ", "_")}_Inspector_Id_App${System.currentTimeMillis()}.png"
                            val savedPath = AssetHelper.saveImageToTempAssets(context, it, fileName)
                            sliderList.add(savedPath)
                            scope.launch {
                                pagerState.scrollToPage(pagerState.pageCount - 1)
                            }
                        }
                    }

                    val launcherCamera = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.TakePicture()
                    ) { success ->
                        if (success && tempImageUri != null) {
                            tempImageUri?.let { uri ->
//                                val fileName = "Camera_Bonnet_id_App_${System.currentTimeMillis()}.png"
                                val fileName = "Camera_${cardName.replace(" ", "_")}_Inspector-Id_App_${System.currentTimeMillis()}.png"
                                val savedPath = AssetHelper.saveImageToTempAssets(context, uri, fileName)
                                sliderList.add(savedPath)
                                scope.launch {
                                    pagerState.scrollToPage(pagerState.pageCount - 1)
                                }
                            }
                        }
                    }

                    fun handleCameraClick() {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                                launchCamera(context, sliderList) { uri ->
                                    tempImageUri = uri
                                    launcherCamera.launch(uri)
                                }
                            }
                            else -> {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clickable {
                                showImagePickerDialog(context, launcherGallery, ::handleCameraClick)
                            },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(2.dp, Color.Gray),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.camera_icon),
                                contentDescription = "Add photo",
                                tint = Color.Gray,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Add photo", color = Color.Gray)
                            Text(
                                "Tap to select or capture images",
                                color = Color.Gray,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        if (pages.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x26000000))
                ) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "", tint = Color.White)
                }
                IconButton(
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x26000000))
                ) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "", tint = Color.White)
                }
            }
        }
    }
}*/


@Composable
fun InspectionScreen() {
    val customParts = InspectionReportUtils.Custom_Card_Parts
    val conditionCategories = InspectionReportUtils.ConditionCatogaries

    LazyColumn {
        items(customParts) { part ->
            conditionCategories[part]?.let { subCategories ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = part,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        subCategories.forEach { (category, options) ->
                            SubCategoryCard(part, category, options)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubCategoryCard(part: String, category: String, options: List<String>) {
    var selectedOption by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = selectedOption == option,
                        onClick = { selectedOption = option }
                    )
                    Text(text = option, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}


 */


/*

@Composable
fun InspectionReportScreen() {
    val expandedGroups = remember { mutableStateMapOf<String, Boolean>() }
    val showDialogForPart = remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        InspectionReportUtils.Body_Parts.forEach { (groupName, partsList) ->
            item {
                ExpandableCard(
                    title = groupName,
                    isExpanded = expandedGroups[groupName] ?: false,
                    onToggle = {
                        expandedGroups[groupName] = !(expandedGroups[groupName] ?: false)
                    }
                ) {
                    partsList.forEach { part ->
                        SubExpandableCard(
                            partName = part,
                            onClick = {
                                showDialogForPart.value = part
                            }
                        )
                    }
                }
            }
        }
    }

    // Dialog
    showDialogForPart.value?.let { selectedPart ->
        ConditionDialog(
            partName = selectedPart,
            conditionCategories = InspectionReportUtils.ConditionCatogaries[selectedPart] ?: emptyList(),
            onDismiss = { showDialogForPart.value = null }
        )
    }
}


@Composable
fun ExpandableCard(
    title: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = title, fontWeight = FontWeight.Bold)
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
            if (isExpanded) {
                content()
            }
        }
    }
}


@Composable
fun SubExpandableCard(
    partName: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Text(
            text = partName,
            modifier = Modifier.padding(12.dp)
        )
    }
}


@Composable
fun ConditionDialog(
    partName: String,
    conditionCategories: List<Pair<String, List<String>>>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Inspection for $partName",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                conditionCategories.forEach { (category, options) ->
                    var selectedOption by remember { mutableStateOf<String?>(null) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = category,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            options.forEach { option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedOption = option }
                                        .padding(vertical = 4.dp)
                                ) {
                                    RadioButton(
                                        selected = selectedOption == option,
                                        onClick = { selectedOption = option }
                                    )
                                    Text(text = option)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Done")
                }
            }
        }
    }
}
*/


@Composable
fun TestCase(navController: NavController) {
    var expandedCard1 by remember { mutableStateOf(false) }
    var expandedCard2 by remember { mutableStateOf(false) }
    val selectedImagesCard1 = remember { mutableStateListOf<String>() }
    val selectedImagesCard2 = remember { mutableStateListOf<String>() }
    val imagesError = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val viewModel: UploadViewModel = viewModel()
    var uploadResults by remember { mutableStateOf<List<Pair<String, JsonObject?>>?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = { expandedCard1 = !expandedCard1 }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Test Case",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { expandedCard1 = !expandedCard1 }) {
                        Icon(
                            imageVector = if (expandedCard1) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                            contentDescription = if (expandedCard1) "Collapse" else "Expand"
                        )
                    }
                }

                if (expandedCard1) {
                    Inspectiod(
                        selectedImages = selectedImagesCard1,
                        showError = imagesError.value,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        cardName = "Test Case"
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Test Case 2",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { expandedCard2 = !expandedCard2 }) {
                        Icon(
                            imageVector = if (expandedCard2) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                            contentDescription = if (expandedCard2) "Collapse" else "Expand"
                        )
                    }
                }

                if (expandedCard2) {
                    Inspectiod(
                        selectedImages = selectedImagesCard2,
                        showError = imagesError.value,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        cardName = "Test Case2"
                    )
                }
            }
        }

        Button(
            onClick = {
                val allImages = selectedImagesCard1 + selectedImagesCard2
                if (allImages.isNotEmpty()) {
                    viewModel.uploadImages(allImages) { results ->
                        uploadResults = results
                        results.forEach { result ->
                            println("Upload Result: $result")
                        }
                        AssetHelper.clearTempAssets(context)
                    }
                } else {
                    Toast.makeText(context, "No images to upload", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = redcolor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Submit report", fontSize = 16.sp)
        }
    }
}

@Composable
fun Inspectiod(
    selectedImages: MutableList<String>,
    showError: Boolean,
    modifier: Modifier = Modifier,
    cardName: String
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .background(cardColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Add Images",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = if (showError && selectedImages.isEmpty()) MaterialTheme.colorScheme.error else Color.Unspecified
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = if (showError && selectedImages.isEmpty()) MaterialTheme.colorScheme.error else Color.Gray
                )
            }

            if (expanded) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                Column {
                    if (showError && selectedImages.isEmpty()) {
                        Text(
                            text = "At least one image is required",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    if (selectedImages.isEmpty()) {
                        Inspectie(selectedImages, cardName)
                    } else {
                        Inspectir(selectedImages, selectedImages, cardName)
                    }
                }
            }
        }
    }
}


@Composable
fun Inspectie(
    sliderList: MutableList<String>,
    cardName: String
) {
    val context = LocalContext.current
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera(context, sliderList) { uri ->
                tempImageUri = uri
            }
        } else {
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    val launcherGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
//            val fileName = "Gallery_Bonnet_id_App${System.currentTimeMillis()}.png"
            val fileName = "Gallery_${cardName.replace(" ", "_")}_Inspector_Id_App${System.currentTimeMillis()}.png"
            Log.d("Renamed", "Gallery image selected, original URI: $uri")
            Log.d("Renamed", "New filename will be: $fileName")

            val savedPath = AssetHelper.saveImageToTempAssets(context, it, fileName)
            sliderList.add(savedPath)

            Log.d("Renamed", "Image added to sliderList at index ${sliderList.size - 1}")
        }
    }

    val launcherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempImageUri != null) {
            tempImageUri?.let { uri ->
//                val fileName = "Camera_Bonnet_id_App_${System.currentTimeMillis()}.png"
                val fileName = "Camera_${cardName.replace(" ", "_")}_Inspector_Id_App${System.currentTimeMillis()}.png"
                Log.d("Renamed", "Camera image ready to save as: $fileName")

                val savedPath = AssetHelper.saveImageToTempAssets(context, uri, fileName)
                sliderList.add(savedPath)

                Log.d("Renamed", "Camera image saved and added to sliderList")
            }
        }
    }

    fun handleCameraClick() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                launchCamera(context, sliderList) { uri ->
                    tempImageUri = uri
                    launcherCamera.launch(uri)
                }
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
            .clickable {
                showImagePickerDialog(context, launcherGallery, ::handleCameraClick)
            },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, Color.Gray),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.camera_icon),
                contentDescription = "Add photo",
                tint = Color.Gray,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Add photo", color = Color.Gray)
            Text(
                "Tap to select or capture images",
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

fun launchCamera(
    context: Context,
    sliderList: MutableList<String>,
    onUriReady: (Uri) -> Unit
) {
    val imageFile = File.createTempFile("IMG_", ".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )
    onUriReady(uri)
}

fun showImagePickerDialog(
    context: Context,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
    onCamera: () -> Unit
) {
    AlertDialog.Builder(context).apply {
        setTitle("Select Image")
        setItems(arrayOf("Camera", "Gallery")) { _, which ->
            when (which) {
                0 -> onCamera()
                1 -> galleryLauncher.launch("image/*")
            }
        }
        setNegativeButton("Cancel", null)
    }.show()
}

/*
@Composable
fun Inspectir(
    pages: MutableList<String>,
    sliderList: MutableList<String>
) {
    val pagerState = rememberPagerState { pages.size + 1 }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp) // Reduced height for better fit
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (page < pages.size) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        val painter = rememberAsyncImagePainter(model = pages[page])
                        Image(
                            painter = painter,
                            contentDescription = "Page $page",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    IconButton(
                        onClick = { pages.removeAt(page) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(36.dp)
                            .background(Color.Red, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove Image",
                            tint = Color.White
                        )
                    }
                } else {
                    AddImage(sliderList)
                }
            }
        }

        // Navigation arrows
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(pagerState.currentPage - 1)
                    }
                },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x26000000))
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "", tint = Color.White)
            }
            IconButton(
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(pagerState.currentPage + 1)
                    }
                },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x26000000))
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "", tint = Color.White)
            }
        }
    }
}

*/

@Composable
fun Inspectir(
    pages: MutableList<String>,
    sliderList: MutableList<String>,
    cardName: String
) {
    val pagerState = rememberPagerState { pages.size + 1 }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (page < pages.size) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        val painter = rememberAsyncImagePainter(model = pages[page])
                        Image(
                            painter = painter,
                            contentDescription = "Page $page",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    IconButton(
                        onClick = { pages.removeAt(page) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(36.dp)
                            .background(Color.Red, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove Image",
                            tint = Color.White
                        )
                    }
                } else {
                    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

                    val cameraPermissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission()
                    ) { isGranted ->
                        if (isGranted) {
                            launchCamera(context, sliderList) { uri ->
                                tempImageUri = uri
                            }
                        } else {
                            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
                        }
                    }

                    val launcherGallery = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()
                    ) { uri: Uri? ->
                        uri?.let {
//                            val fileName = "Gallery_Bonnet_id_App${System.currentTimeMillis()}.png"
                            val fileName = "Gallery_${cardName.replace(" ", "_")}_Inspector_Id_App${System.currentTimeMillis()}.png"
                            val savedPath = AssetHelper.saveImageToTempAssets(context, it, fileName)
                            sliderList.add(savedPath)
                            scope.launch {
                                pagerState.scrollToPage(pagerState.pageCount - 1)
                            }
                        }
                    }

                    val launcherCamera = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.TakePicture()
                    ) { success ->
                        if (success && tempImageUri != null) {
                            tempImageUri?.let { uri ->
//                                val fileName = "Camera_Bonnet_id_App_${System.currentTimeMillis()}.png"
                                val fileName = "Camera_${cardName.replace(" ", "_")}_Inspector-Id_App_${System.currentTimeMillis()}.png"
                                val savedPath = AssetHelper.saveImageToTempAssets(context, uri, fileName)
                                sliderList.add(savedPath)
                                scope.launch {
                                    pagerState.scrollToPage(pagerState.pageCount - 1)
                                }
                            }
                        }
                    }

                    fun handleCameraClick() {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                                launchCamera(context, sliderList) { uri ->
                                    tempImageUri = uri
                                    launcherCamera.launch(uri)
                                }
                            }
                            else -> {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clickable {
                                showImagePickerDialog(context, launcherGallery, ::handleCameraClick)
                            },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(2.dp, Color.Gray),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.camera_icon),
                                contentDescription = "Add photo",
                                tint = Color.Gray,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Add photo", color = Color.Gray)
                            Text(
                                "Tap to select or capture images",
                                color = Color.Gray,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        if (pages.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x26000000))
                ) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "", tint = Color.White)
                }
                IconButton(
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x26000000))
                ) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "", tint = Color.White)
                }
            }
        }
    }
}

