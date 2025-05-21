package com.example.carapp.screens.Inspector

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil3.compose.rememberAsyncImagePainter
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.assets.AssetHelper
import com.example.carapp.assets.cardColor
import com.example.carapp.screens.AddImage
import com.example.carapp.screens.getToken
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InspectionimageSliderCard(
    selectedImages: MutableList<String>,
    showError: Boolean,
    modifier: Modifier = Modifier
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
                        InspectionaddImage(selectedImages)
                    } else {
                        InspectionpageSlider(selectedImages, selectedImages)
                    }
                }
            }
        }
    }
}


@Composable
fun InspectionaddImage(sliderList: MutableList<String>) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { sliderList.add(it.toString()) }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
            .clickable { launcher.launch("image/*") },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 2.dp,
            color = Color.Gray,
//            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        ),
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
                "Tap to select images",
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Composable
fun InspectionpageSlider(
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

@Composable
fun InputField(
    inputFields: Map<String, MutableState<String>>,
    iconMapping: Map<String, Int>,
    dropdownOptions: Map<String, List<String>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        inputFields.keys.forEach { fieldName ->
            val textFieldValue = inputFields[fieldName]?.value ?: ""
            var expanded by remember { mutableStateOf(false) }
            val suggestions = dropdownOptions[fieldName] ?: emptyList()

            Box(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = fieldName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = { newValue ->
                            inputFields[fieldName]?.value = newValue
                        },
                        placeholder = { Text("Enter $fieldName") },
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .clickable { expanded = true }
                            .padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1976D2),
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color(0xFF1976D2)
                        ),
                        leadingIcon = {
                            iconMapping[fieldName]?.let { iconResId ->
                                Image(
                                    painter = painterResource(id = iconResId),
                                    contentDescription = "$fieldName Icon",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                                    contentDescription = if (expanded) "Collapse" else "Expand"
                                )
                            }
                        }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        suggestions.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    inputFields[fieldName]?.value = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/*
@Composable
fun BasicInformationCard(
    inputFields: Map<String, MutableState<String>>,
    dropdownOptions: Map<String, List<String>>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val basicInfoFields = listOf("Car Name", "Car Model", "Body color", "Company")
    val basicInfoIcons = mapOf(
        "Car Name" to R.drawable.car_icon,
        "Car Model" to R.drawable.car_icon,
        "Body color" to R.drawable.color_bucket_icon,
        "Company" to R.drawable.company
    )

    // Track each field value individually
    val carName by inputFields["Car Name"] ?: remember { mutableStateOf("") }
    val carModel by inputFields["Car Model"] ?: remember { mutableStateOf("") }
    val bodyColor by inputFields["Body color"] ?: remember { mutableStateOf("") }
    val company by inputFields["Company"] ?: remember { mutableStateOf("") }

    // Convert current values to JSON whenever they change
    val currentBasicInfo = remember(carName, carModel, bodyColor, company) {
        BasicInformation(
            carName = carName,
            carModel = carModel,
            bodyColor = bodyColor,
            company = company
        )
    }

    val inspectionData = LocalInspectionData.current

    LaunchedEffect(currentBasicInfo) {
        inspectionData.basicInformation.value = currentBasicInfo

    }
    // JSON OF BASIC INFO
//    LaunchedEffect(currentBasicInfo) {
//        val gson = Gson()
//        val json = gson.toJson(currentBasicInfo)
//        println("Basic Information JSON: $json")
//    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Basic Information",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.size(24.dp)
                )
            }

            if (expanded) {
                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    basicInfoFields.forEach { fieldName ->
                        val textFieldValue = inputFields[fieldName]?.value ?: ""
                        var dropdownExpanded by remember { mutableStateOf(false) }
                        val suggestions = dropdownOptions[fieldName] ?: emptyList()

                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                text = fieldName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            OutlinedTextField(
                                value = textFieldValue,
                                onValueChange = { newValue ->
                                    inputFields[fieldName]?.value = newValue
                                },
                                placeholder = { Text("Enter $fieldName") },
                                textStyle = TextStyle(fontSize = 16.sp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { dropdownExpanded = true },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF1976D2),
                                    unfocusedBorderColor = Color.Gray,
                                    cursorColor = Color(0xFF1976D2)
                                ),
                                leadingIcon = {
                                    basicInfoIcons[fieldName]?.let { iconResId ->
                                        Image(
                                            painter = painterResource(id = iconResId),
                                            contentDescription = "$fieldName Icon",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                },
                                trailingIcon = {
                                    IconButton(onClick = { dropdownExpanded = !dropdownExpanded }) {
                                        Icon(
                                            imageVector = if (dropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                                            contentDescription = if (dropdownExpanded) "Collapse" else "Expand"
                                        )
                                    }
                                }
                            )

                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                            ) {
                                suggestions.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            inputFields[fieldName]?.value = item
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
*/

/*@Composable
fun BasicInformationCard(
    inputFields: Map<String, MutableState<String>>,
    dropdownOptions: Map<String, List<String>>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val basicInfoFields = listOf("Car Name", "Car Model", "Body color", "Company")
    val basicInfoIcons = mapOf(
        "Car Name" to R.drawable.car_icon,
        "Car Model" to R.drawable.car_icon,
        "Body color" to R.drawable.color_bucket_icon,
        "Company" to R.drawable.company
    )

    val inspectionData = LocalInspectionData.current

    // Trigger recomposition and updates using derivedStateOf
    val currentBasicInfo by remember {
        derivedStateOf {
            BasicInformation(
                carName = inputFields["Car Name"]?.value ?: "",
                carModel = inputFields["Car Model"]?.value ?: "",
                bodyColor = inputFields["Body color"]?.value ?: "",
                company = inputFields["Company"]?.value ?: ""
            )
        }
    }
    LaunchedEffect(currentBasicInfo) {
        Log.d("test","@$currentBasicInfo")
        inspectionData.basicInformation.value = currentBasicInfo

    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Basic Information",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.size(24.dp)
                )
            }

            if (expanded) {
                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)

                Column(modifier = Modifier.padding(16.dp)) {
                    basicInfoFields.forEach { fieldName ->
                        val textFieldState = inputFields[fieldName] ?: remember { mutableStateOf("") }
                        var dropdownExpanded by remember { mutableStateOf(false) }
                        val suggestions = dropdownOptions[fieldName] ?: emptyList()

                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                text = fieldName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            OutlinedTextField(
                                value = textFieldState.value,
                                onValueChange = { newValue ->
                                    textFieldState.value = newValue
                                },
                                placeholder = { Text("Enter $fieldName") },
                                textStyle = TextStyle(fontSize = 16.sp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { dropdownExpanded = true },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF1976D2),
                                    unfocusedBorderColor = Color.Gray,
                                    cursorColor = Color(0xFF1976D2)
                                ),
                                leadingIcon = {
                                    basicInfoIcons[fieldName]?.let { iconResId ->
                                        Image(
                                            painter = painterResource(id = iconResId),
                                            contentDescription = "$fieldName Icon",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                },
                                trailingIcon = {
                                    IconButton(onClick = { dropdownExpanded = !dropdownExpanded }) {
                                        Icon(
                                            imageVector = if (dropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                                            contentDescription = if (dropdownExpanded) "Collapse" else "Expand"
                                        )
                                    }
                                }
                            )

                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                            ) {
                                suggestions.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            textFieldState.value = item
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}*/
@Composable
fun BasicInformationCard(
    inputFields: Map<String, MutableState<String>>,
    dropdownOptions: Map<String, List<String>>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val basicInfoFields = listOf("Car Name", "Car Model", "Body color", "Company")
    val basicInfoIcons = mapOf(
        "Car Name" to R.drawable.car_icon,
        "Car Model" to R.drawable.car_icon,
        "Body color" to R.drawable.color_bucket_icon,
        "Company" to R.drawable.company
    )

    val inspectionData = LocalInspectionData.current

    val currentBasicInfo by remember {
        derivedStateOf {
            BasicInformation(
                carName = inputFields["Car Name"]?.value ?: "",
                carModel = inputFields["Car Model"]?.value ?: "",
                bodyColor = inputFields["Body color"]?.value ?: "",
                company = inputFields["Company"]?.value ?: ""
            )
        }
    }

    LaunchedEffect(currentBasicInfo) {
        inspectionData.basicInformation.value = currentBasicInfo
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Basic Information",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.size(24.dp)
                )
            }

            if (expanded) {
                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)

                Column(modifier = Modifier.padding(16.dp)) {
                    basicInfoFields.forEach { fieldName ->
                        val textFieldState = inputFields[fieldName] ?: remember { mutableStateOf("") }
                        val suggestions = dropdownOptions[fieldName] ?: emptyList()
                        var dropdownExpanded by remember { mutableStateOf(false) }
                        var fieldWidth by remember { mutableStateOf(0) }

                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                text = fieldName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                    .clickable { dropdownExpanded = true }
                                    .onGloballyPositioned { coordinates ->
                                        fieldWidth = coordinates.size.width
                                    }
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        basicInfoIcons[fieldName]?.let { iconResId ->
                                            Image(
                                                painter = painterResource(id = iconResId),
                                                contentDescription = "$fieldName Icon",
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                        }
                                        Text(
                                            text = textFieldState.value.ifEmpty { "Select $fieldName" },
                                            color = if (textFieldState.value.isEmpty()) Color.Gray else Color.Black,
                                            fontSize = 16.sp
                                        )
                                    }
                                    Icon(
                                        imageVector = if (dropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                                        contentDescription = null
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false },
                                modifier = Modifier
                                    .background(Color.White)
                                    .width(with(LocalDensity.current) { fieldWidth.toDp() }
                                )
                            ) {
                                suggestions.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            textFieldState.value = item
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TechnicalSpecificationsCard(
    inputFields: Map<String, MutableState<String>>,
    onTechnicalSpecUpdate: (TechnicalSpecifications) -> Unit,
    dropdownOptions: Map<String, List<String>>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val techSpecFields = listOf(
        "KMs Driven",
        "Condition",
        "Variant (auto/manual)",
        "Fuel type",
        "Assembly",
        "Engine Capacity"
    )

    val techSpecIcons = mapOf(
        "KMs Driven" to R.drawable.speed_icon,
        "Condition" to R.drawable.termsconditions,
        "Variant (auto/manual)" to R.drawable.varient,
        "Fuel type" to R.drawable.fueltype,
        "Assembly" to R.drawable.assembly,
        "Engine Capacity" to R.drawable.enginecapacity
    )

    val kmsDriven by inputFields["KMs Driven"] ?: remember { mutableStateOf("") }
    val condition by inputFields["Condition"] ?: remember { mutableStateOf("") }
    val variant by inputFields["Variant (auto/manual)"] ?: remember { mutableStateOf("") }
    val fuelType by inputFields["Fuel type"] ?: remember { mutableStateOf("") }
    val assembly by inputFields["Assembly"] ?: remember { mutableStateOf("") }
    val engineCapacity by inputFields["Engine Capacity"] ?: remember { mutableStateOf("") }

    val currentTechSpecs = remember(kmsDriven, condition, variant, fuelType, assembly, engineCapacity) {
        TechnicalSpecifications(
            kmsDriven = kmsDriven,
            condition = condition,
            variant = variant,
            fuelType = fuelType,
            assembly = assembly,
            engineCapacity = engineCapacity
        )
    }

    LaunchedEffect(currentTechSpecs) {
        onTechnicalSpecUpdate(currentTechSpecs)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Technical Specifications",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.size(24.dp)
                )
            }

            if (expanded) {
                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    techSpecFields.forEach { fieldName ->
                        val textFieldValue = inputFields[fieldName]?.value ?: ""
                        var dropdownExpanded by remember { mutableStateOf(false) }
                        val suggestions = dropdownOptions[fieldName] ?: emptyList()

                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                text = fieldName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            var dropdownExpanded by remember { mutableStateOf(false) }
                            var textFieldSize by remember { mutableStateOf(IntSize.Zero) }
                            val suggestions = dropdownOptions[fieldName] ?: emptyList()

                            Box {
                                OutlinedTextField(
                                    value = textFieldValue,
                                    onValueChange = { },
                                    enabled = false, // non-editable
                                    placeholder = { Text("Select $fieldName") },
                                    textStyle = TextStyle(fontSize = 16.sp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .onGloballyPositioned { coordinates ->
                                            textFieldSize = coordinates.size
                                        }
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                        .clickable { dropdownExpanded = true },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF1976D2),
                                        unfocusedBorderColor = Color.Gray,
                                        cursorColor = Color(0xFF1976D2),
                                        disabledTextColor = Color.Black,
                                        disabledContainerColor = Color.White
                                    ),
                                    leadingIcon = {
                                        techSpecIcons[fieldName]?.let { iconResId ->
                                            Image(
                                                painter = painterResource(id = iconResId),
                                                contentDescription = "$fieldName Icon",
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            dropdownExpanded = !dropdownExpanded
                                        }) {
                                            Icon(
                                                imageVector = if (dropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                                                contentDescription = if (dropdownExpanded) "Collapse" else "Expand"
                                            )
                                        }
                                    }
                                )

                                DropdownMenu(
                                    expanded = dropdownExpanded,
                                    onDismissRequest = { dropdownExpanded = false },
                                    modifier = Modifier
                                        .background(Color.White)
                                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                                ) {
                                    suggestions.forEach { suggestion ->
                                        DropdownMenuItem(
                                            text = { Text(text = suggestion) },
                                            onClick = {
                                                inputFields[fieldName]?.value = suggestion
                                                dropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


/*
@Composable
fun TechnicalSpecificationsCard(
    inputFields: Map<String, MutableState<String>>,
    onTechnicalSpecUpdate: (TechnicalSpecifications) -> Unit,
    dropdownOptions: Map<String, List<String>>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val techSpecFields = listOf(
        "KMs Driven",
        "Condition",
        "Variant (auto/manual)",
        "Fuel type",
        "Assembly",
        "Engine Capacity"
    )

    val techSpecIcons = mapOf(
        "KMs Driven" to R.drawable.speed_icon,
        "Condition" to R.drawable.termsconditions,
        "Variant (auto/manual)" to R.drawable.varient,
        "Fuel type" to R.drawable.fueltype,
        "Assembly" to R.drawable.assembly,
        "Engine Capacity" to R.drawable.enginecapacity
    )

    // Track each field value individually
    val kmsDriven by inputFields["KMs Driven"] ?: remember { mutableStateOf("") }
    val condition by inputFields["Condition"] ?: remember { mutableStateOf("") }
    val variant by inputFields["Variant (auto/manual)"] ?: remember { mutableStateOf("") }
    val fuelType by inputFields["Fuel type"] ?: remember { mutableStateOf("") }
    val assembly by inputFields["Assembly"] ?: remember { mutableStateOf("") }
    val engineCapacity by inputFields["Engine Capacity"] ?: remember { mutableStateOf("") }

    // Convert current values to JSON whenever they change
    val currentTechSpecs = remember(kmsDriven, condition, variant, fuelType, assembly, engineCapacity) {
        TechnicalSpecifications(
            kmsDriven = kmsDriven,
            condition = condition,
            variant = variant,
            fuelType = fuelType,
            assembly = assembly,
            engineCapacity = engineCapacity
        )
    }

    val inspectionData = LocalInspectionData.current

//    LaunchedEffect(currentTechSpecs) {
//        inspectionData.technicalSpecifications.value = currentTechSpecs
//        logCompleteInspection(inspectionData)
//    }
    LaunchedEffect(currentTechSpecs) {
        onTechnicalSpecUpdate(currentTechSpecs)
    }

//    LaunchedEffect(currentTechSpecs) {
//        val gson = Gson()
//        val json = gson.toJson(currentTechSpecs)
//        println("Technical Specifications JSON: $json")
//    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Technical Specifications",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.size(24.dp)
                )
            }

            if (expanded) {
                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    techSpecFields.forEach { fieldName ->
                        val textFieldValue = inputFields[fieldName]?.value ?: ""
                        var dropdownExpanded by remember { mutableStateOf(false) }
                        val suggestions = dropdownOptions[fieldName] ?: emptyList()

                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                text = fieldName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            OutlinedTextField(
                                value = textFieldValue,
                                onValueChange = { newValue ->
                                    inputFields[fieldName]?.value = newValue
                                },
                                placeholder = { Text("Enter $fieldName") },
                                textStyle = TextStyle(fontSize = 16.sp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { dropdownExpanded = true },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF1976D2),
                                    unfocusedBorderColor = Color.Gray,
                                    cursorColor = Color(0xFF1976D2)
                                ),
                                leadingIcon = {
                                    techSpecIcons[fieldName]?.let { iconResId ->
                                        Image(
                                            painter = painterResource(id = iconResId),
                                            contentDescription = "$fieldName Icon",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                },
                                trailingIcon = {
                                    IconButton(onClick = { dropdownExpanded = !dropdownExpanded }) {
                                        Icon(
                                            imageVector = if (dropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                                            contentDescription = if (dropdownExpanded) "Collapse" else "Expand"
                                        )
                                    }
                                }
                            )

                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                            ) {
                                suggestions.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            inputFields[fieldName]?.value = item
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
*/










@Composable
fun CustomDetailCard(partName: String) {
    var imagesExpanded by remember { mutableStateOf(false) }
    val selectedImages = remember { mutableStateListOf<String>() }
    val imagesError = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { imagesExpanded = !imagesExpanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Add images",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = if (imagesExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                    contentDescription = if (imagesExpanded) "Collapse" else "Expand"
                )
            }

            if (imagesExpanded) {
                InspectionCard(
                    selectedImages = selectedImages,
                    showError = imagesError.value,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}




@Composable
fun InspectionRatings(ratings: MutableMap<String, Float>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Inspection Ratings", fontSize = 25.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(12.dp))

        // Calculate the average rating
        val averageRating = ratings.values.average()

        // Convert current values to JSON whenever they change
        val currentRatings = remember(ratings.values.toList()) {
            InspectionRatingsData(
                engineCondition = ratings["Engine Condition"] ?: 50f,
                bodyCondition = ratings["Body Condition"] ?: 50f,
                clutchCondition = ratings["Clutch Condition"] ?: 50f,
                steeringCondition = ratings["Steering Condition"] ?: 50f,
                brakesCondition = ratings["Brakes Condition"] ?: 50f,
                suspensionCondition = ratings["Suspension Condition"] ?: 50f,
                acCondition = ratings["Ac Condition"] ?: 50f,
                electricSystemCondition = ratings["Electric System Condition"] ?: 50f,
                averageRating = averageRating.toFloat()
            )
        }
        val inspectionData = LocalInspectionData.current


        LaunchedEffect(currentRatings) {
            inspectionData.inspectionRatings.value = currentRatings
            logCompleteInspection(inspectionData)
        }
//        LaunchedEffect(currentRatings) {
//            val gson = Gson()
//            val json = gson.toJson(currentRatings)
//            println("Inspection Ratings JSON: $json")
//        }

        // CircularRatingIndicator(averageRating.toFloat())

        Spacer(modifier = Modifier.height(16.dp))

        ratings.keys.forEach { item ->
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(text = item, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Rating: ${ratings[item]?.toInt()}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue
                )
                Slider(
                    value = ratings[item] ?: 50f,
                    onValueChange = { newValue -> ratings[item] = newValue },
                    valueRange = 0f..100f,
                    steps = 100,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

fun updateStatus(context: Context, carId: String, status: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    val url = "${TestApi.update_status}$carId/"
    Log.d("API_DEBUG", "Request URL: $url")

    val token = getToken(context)
    Log.d("API_DEBUG", "Token: $token")
    val client = OkHttpClient()
    val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    val jsonBody = JSONObject().apply {
        put("status", status)
    }.toString()

    val requestBody = jsonBody.toRequestBody(jsonMediaType)

    val request = Request.Builder()
        .url(url)
        .patch(requestBody)
        .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onFailure(e.message ?: "Unknown error")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onFailure(response.message)
            }
        }
    })
}

fun convertImageToBase64(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val byteArray = inputStream?.readBytes()
    val base64String = if (byteArray != null) {
        Base64.encodeToString(byteArray, Base64.NO_WRAP)
    } else {
        ""
    }
    val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
    val formattedBase64 = "data:$mimeType;base64,$base64String"
    Log.e("Base64", "Formatted Base64: $formattedBase64")
    return formattedBase64
}

@Composable
fun InspectionCard(
    selectedImages: MutableList<String>,
    showError: Boolean,
    modifier: Modifier = Modifier
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
                        InspectedImage(selectedImages)
                    } else {
                        InspectedSlider(selectedImages, selectedImages)
                    }
                }
            }
        }
    }
}

@Composable
fun InspectedImage(sliderList: MutableList<String>) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { sliderList.add(it.toString()) }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
            .clickable { launcher.launch("image/*") },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 2.dp,
            color = Color.Gray,
//            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        ),
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
                "Tap to select images",
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun InspectedSlider(
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

@Composable
fun ExpandableCards(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.size(24.dp)
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                content()
            }
        }
    }
}


@Composable
fun InspectionItemCards(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            Color(0xFFF5F5F5),
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "View details"
            )
        }
    }
}



/*


@Composable
fun ImageUploadCards(
    imageUrls: List<String>,
    cardName: String
) {
    var expanded by remember { mutableStateOf(true) }
    val selectedImagesCard1 = remember { mutableStateListOf<String>() }
    val imagesError = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Images",
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.size(24.dp)
                )
            }

            if (expanded) {
                Inspectiods(
                    selectedImages = selectedImagesCard1,
                    showError = imagesError.value,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    cardName = cardName
                )
            }
        }
    }
}

@Composable
fun Inspectiods(
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
                        Inspecties(selectedImages, cardName)
                    } else {
                        Inspectirs(selectedImages, selectedImages, cardName)
                    }
                }
            }
        }
    }
}

@Composable
fun Inspecties(
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

@Composable
fun Inspectirs(
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

*/

@Composable
fun ImageUploadCards(
    imageUrls: List<String>,
    cardName: String,
    selectedImages: MutableList<String>,
    onImagesUpdated: (List<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }
    val imagesError = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Images",
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.size(24.dp)
                )
            }

            if (expanded) {
                Inspectiods(
                    selectedImages = selectedImages,
                    showError = imagesError.value,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    cardName = cardName,
                    onImagesChanged = { updatedImages ->
                        onImagesUpdated(updatedImages)
                    }
                )
            }
        }
    }
}

@Composable
fun Inspectiods(
    selectedImages: MutableList<String>,
    showError: Boolean,
    modifier: Modifier = Modifier,
    cardName: String,
    onImagesChanged: (List<String>) -> Unit
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
                        Inspecties(
                            cardName = cardName,
                            onImageAdded = { newImage ->
                                val updated = selectedImages.toMutableList().apply { add(newImage) }
                                onImagesChanged(updated)
                            }
                        )
                    } else {
                        Inspectirs(
                            pages = selectedImages,
                            sliderList = selectedImages,
                            cardName = cardName,
                            onImagesChanged = onImagesChanged
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Inspecties(
    cardName: String,
    onImageAdded: (String) -> Unit
) {
    val context = LocalContext.current
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCameras(context) { uri ->
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
            val fileName = "Gallery_${cardName.replace(" ", "_")}_Inspector_Id_App${System.currentTimeMillis()}.png"
            val savedPath = AssetHelper.saveImageToTempAssets(context, it, fileName)
            onImageAdded(savedPath)
        }
    }

    val launcherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempImageUri != null) {
            tempImageUri?.let { uri ->
                val fileName = "Camera_${cardName.replace(" ", "_")}_Inspector_Id_App${System.currentTimeMillis()}.png"
                val savedPath = AssetHelper.saveImageToTempAssets(context, uri, fileName)
                onImageAdded(savedPath)
            }
        }
    }

    fun handleCameraClick() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                launchCameras(context) { uri ->
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
fun launchCameras(
    context: Context,
    onImageCaptured: (Uri) -> Unit
) {
    val photoFile = createImageFile(context)
    val photoUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        photoFile
    )
    onImageCaptured(photoUri)
}
fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "PNG_${timeStamp}_"
    val storageDir = context.getExternalFilesDir("Pictures")

    return File.createTempFile(
        imageFileName,
        ".png",
        storageDir
    ).apply {
        parentFile?.mkdirs()
    }
}
@Composable
fun Inspectirs(
    pages: MutableList<String>,
    sliderList: MutableList<String>,
    cardName: String,
    onImagesChanged: (List<String>) -> Unit
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
                        onClick = {
                            val updated = pages.toMutableList().apply { removeAt(page) }
                            onImagesChanged(updated)
                        },
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
                            launchCameras(context) { uri ->
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
                            val fileName = "Gallery_${cardName.replace(" ", "_")}_Inspector_Id_App${System.currentTimeMillis()}.png"
                            val savedPath = AssetHelper.saveImageToTempAssets(context, it, fileName)
                            val updated = sliderList.toMutableList().apply { add(savedPath) }
                            onImagesChanged(updated)
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
                                val fileName = "Camera_${cardName.replace(" ", "_")}_Inspector_Id_App${System.currentTimeMillis()}.png"
                                val savedPath = AssetHelper.saveImageToTempAssets(context, uri, fileName)
                                val updated = sliderList.toMutableList().apply { add(savedPath) }
                                onImagesChanged(updated)
                                scope.launch {
                                    pagerState.scrollToPage(pagerState.pageCount - 1)
                                }
                            }
                        }
                    }

                    fun handleCameraClick() {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                                launchCameras(context) { uri -> // Updated to match new signature
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
