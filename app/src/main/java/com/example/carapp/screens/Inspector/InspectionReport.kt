package com.example.carapp.screens.Inspector


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.assets.AssetHelper
import com.example.carapp.models.InspectionModel
import com.example.carapp.screens.AddImage
import com.example.carapp.screens.CustomAlertDialog
import com.example.carapp.screens.ImageSliderCard
import com.example.carapp.screens.PageSlider
import com.example.carapp.screens.cardColor
import com.example.carapp.screens.getToken
import com.example.carapp.screens.getUserId
import com.example.carapp.screens.redcolor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.lang.Thread.State
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

var jsonvar = mutableStateMapOf<String, MutableMap<String, String>>()

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Inspectionreport(navController: NavController, id: String) {
    Log.d("CAR_ID", "CARRR--------$id")
    val selectedImages = remember { mutableStateListOf<String>() }
    val imagesError = remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var availableTimes by remember { mutableStateOf<List<String>>(emptyList()) }
    val latestTechSpecs = remember { mutableStateOf<TechnicalSpecifications?>(null) }


    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current
    systemUiController.isStatusBarVisible = false

    val dropdownOptions = mapOf(
        "Car Name" to listOf("Toyota", "Honda", "Suzuki", "BMW", "Mercedes"),
        "Car Model" to listOf("2015", "2016", "2017", "2018", "2019"),
        "Body color" to listOf("White", "Black", "Blue", "Red", "Silver"),
        "Company" to listOf("Toyota", "Honda", "Suzuki", "BMW", "Mercedes"),
        "KMs Driven" to listOf("10,000", "Up to-20,000", "Up to-30,000", "Up to-40,000", "Up to-50,000"),
        "Variant (auto/manual)" to listOf("Automatic", "Manual"),
        "Engine Capacity" to listOf("1000", "1300", "1500", "1800", "2000"),
        "Condition" to listOf("New", "Used", "Excellent", "Good", "Fair"),
        "Fuel type" to listOf("Petrol", "Diesel", "Hybrid", "Electric"),
        "Assembly" to listOf("Local", "Imported"),
//        "Price (PKR)" to listOf(""),
//        "Location" to listOf("Karachi", "Lahore", "Islamabad", "Rawalpindi", "Faisalabad"),
//        "Registered in" to listOf("Punjab", "Sindh", "KPK", "Balochistan"),
//        "Phone Number" to listOf("")
    )

    val inputFields = remember {
        dropdownOptions.keys.associateWith { mutableStateOf("") }
    }

    var carDetailsExpanded by remember { mutableStateOf(false) }
    var inspectionDetailsExpanded by remember { mutableStateOf(false) }
    var imagesExpanded by remember { mutableStateOf(false) }
    val bodyPartConditions = remember { mutableStateMapOf<String, String>() }
    val inspectionData = remember { InspectionDataState() }

    LaunchedEffect(
        inspectionData.basicInformation.value,
        inspectionData.technicalSpecifications.value,
        inspectionData.bodyPartsInspection,
//        inspectionData.inspectionRatings.value
    ) {
        val completeData = inspectionData.toCompleteInspection()
        if (completeData != null) {
            Log.d("DATA_VERIFICATION", "Basic Info: ${completeData.basicInformation != null}")
            Log.d("DATA_VERIFICATION", "Tech Specs: ${completeData.technicalSpecifications != null}")
            Log.d("DATA_VERIFICATION", "Body Parts: ${completeData.bodyPartsInspection.isNotEmpty()}")
//            Log.d("DATA_VERIFICATION", "Ratings: ${completeData.inspectionRatings != null}")

            val gson = GsonBuilder().setPrettyPrinting().create()
            Log.d("COMPLETE_DATA", gson.toJson(completeData))
        } else {
            Log.d("DATA_VERIFICATION", "Incomplete data - missing:")
            if (inspectionData.basicInformation.value == null) Log.d("MISSING", "Basic Info")
            if (inspectionData.technicalSpecifications.value == null) Log.d("MISSING", "Tech Specs")
//            if (inspectionData.inspectionRatings.value == null) Log.d("MISSING", "Ratings")
            if (inspectionData.bodyPartsInspection.isEmpty()) Log.d("MISSING", "Body Parts")
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.background(redcolor),
                title = {
                    Text(
                        "Inspection Report",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
            )
        },
        bottomBar = {},
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { carDetailsExpanded = !carDetailsExpanded }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Car Details",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = if (carDetailsExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                            contentDescription = if (carDetailsExpanded) "Collapse" else "Expand"
                        )
                    }

                    if (carDetailsExpanded) {
                        Divider(color = Color.LightGray, thickness = 1.dp)

                        BasicInformationCard(
                            inputFields = inputFields,
                            dropdownOptions = dropdownOptions
                        )
                        TechnicalSpecificationsCard(
                            inputFields = inputFields,
                            onTechnicalSpecUpdate = { updatedSpecs ->
                                latestTechSpecs.value = updatedSpecs
                            },
                            dropdownOptions = dropdownOptions
                        )

                        /*BodyPartsInspectionCard(
                            onConditionSelected = { partName, condition ->
                                bodyPartConditions[partName] = condition
                            },
                            inspectionData = inspectionData
                        )*/
                        BodyPartsInspectionCard(
                            onConditionSelected = { partName, conditions ->

                                inspectionData.updateBodyParts(partName, conditions)
                                Log.d("CONDITION_SELECTED", "Part: $partName, Conditions: $conditions")
                            },
                            inspectionData = inspectionData
                        )

                        val remainingFields = dropdownOptions.keys.filterNot {
                            it in listOf(
                                "Car Name", "Car Model", "Body color", "Company",  // Basic info
                                "KMs Driven", "Condition", "Variant (auto/manual)", // Tech specs
                                "Fuel type", "Assembly", "Engine Capacity"
                            )
                        }
                        if (remainingFields.isNotEmpty()) {
                            val iconMapping = mapOf(
                                "Location" to R.drawable.location_icon,
                                "Registered in" to R.drawable.register_icon,
                                "KMs Driven" to R.drawable.speed_icon,
                                "Condition" to R.drawable.termsconditions,
                                "Variant (auto/manual)" to R.drawable.varient,
                                "Fuel type" to R.drawable.fueltype,
                                "Assembly" to R.drawable.assembly,
                                "Engine Capacity" to R.drawable.enginecapacity,
                                "Price (PKR)" to R.drawable.description_icon,
                                "Phone Number" to R.drawable.call
                            )

                            InputField(
                                inputFields = inputFields.filterKeys { it in remainingFields },
                                iconMapping = iconMapping,
                                dropdownOptions = dropdownOptions.filterKeys { it in remainingFields }
                            )
                        }
                    }
                }
            }

            val ratingOptions = listOf(
                "Engine Condition", "Body Condition", "Clutch Condition",
                "Steering Condition", "Brakes Condition", "Suspension Condition",
                "Ac Condition", "Electric System Condition"
            )

            val ratings = remember {
                mutableStateMapOf<String, Float>().apply {
                    ratingOptions.forEach { this[it] = 50f }
                }
            }
//
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp, vertical = 8.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//            ) {
//                Column {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable { inspectionDetailsExpanded = !inspectionDetailsExpanded }
//                            .padding(16.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                            text = "Inspection Details",
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                        Icon(
//                            imageVector = if (inspectionDetailsExpanded)Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
//                            contentDescription = if (inspectionDetailsExpanded) "Collapse" else "Expand"
//                        )
//                    }
//
//                    if (inspectionDetailsExpanded) {
//                        Divider(color = Color.LightGray, thickness = 1.dp)
//                        InspectionRatings(ratings)
//                    }
//                }
//            }

            // Images Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                            text = "Inspection Images",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = if (imagesExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                            contentDescription = if (imagesExpanded) "Collapse" else "Expand"
                        )
                    }

                    if (imagesExpanded) {
                        InspectionimageSliderCard(
                            selectedImages = selectedImages,
                            showError = imagesError.value,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
            var isSubmitting by remember { mutableStateOf(false) }
            // if in Submition of report got error check this its original
            // in new code added loadin animation inside the button
            // Submit Button
           /* Button(
                onClick = {
                    Log.d("PRE_SUBMIT_DEBUG", "All body parts before submission:")
                    inspectionData.bodyPartsInspection.forEach { (part, conditions) ->
                        Log.d("PRE_SUBMIT_DEBUG", "$part: $conditions")
                    }
                    getToken(context)?.let { token ->
                        latestTechSpecs.value?.let { specs ->
                            Log.d("TechSpecs", "Specs found: $specs")
                            inspectionData.technicalSpecifications.value = specs
                            logCompleteInspection(inspectionData)
                            Log.d("TechSpecs", "Inspection data updated and logged.")
                        }

//                        inspectionData.updateBasicInformation(inputFields)
//                        inspectionData.updateTechnicalSpecifications(inputFields)
                        inspectionData.updateBasicInformation(inputFields)
                        inspectionData.updateTechnicalSpecifications(inputFields)

                        // Debug log to verify all data
                        Log.d("SUBMISSION_DATA", "inspectionData: ${inspectionData}")
                        Log.d("SUBMISSION_DATA", "Basic Info: ${inspectionData.basicInformation.value}")
                        Log.d("SUBMISSION_DATA", "Tech Specs: ${inspectionData.technicalSpecifications.value}")
//                        Log.d("SUBMISSION_DATA", "Ratings: ${inspectionData.inspectionRatings.value}")

                        Log.d("SUBMISSION_DATA", "Body Parts: ${inspectionData.bodyPartsInspection.toMap()}")
                        updateStatus(
                            context = context,
                            carId = id,
                            status = "await_approval",
                            onSuccess = {
                                update_status_inspection(context, id) { success ->
                                    if (success) {
                                        postInspectionReport(
                                            context = context,
                                            navController = navController,
                                            id = id,
                                            inspectionData = inspectionData,
                                            token = token
                                        )
                                    } else {
                                        Log.e("API_ERROR", "Failed to POST empty body to inspection endpoint.")
                                    }
                                }
                            },
                            onFailure = { errorMessage ->
                                Log.e("API_ERROR", "Failed to update status: ")
                            }

                        )
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
            }*/
            Button(
                onClick = {
                    isSubmitting = true // Start loading

                    getToken(context)?.let { token ->
                        latestTechSpecs.value?.let { specs ->
                            inspectionData.technicalSpecifications.value = specs
                            logCompleteInspection(inspectionData)
                        }

                        inspectionData.updateBasicInformation(inputFields)
                        inspectionData.updateTechnicalSpecifications(inputFields)

                        updateStatus(
                            context = context,
                            carId = id,
                            status = "await_approval",
                            onSuccess = {
                                update_status_inspection(context, id) { success ->
                                    if (success) {
                                        postInspectionReport(
                                            context = context,
                                            navController = navController,
                                            id = id,
                                            inspectionData = inspectionData,
                                            token = token
                                        )
                                    } else {
                                        Log.e("API_ERROR", "Failed to POST empty body to inspection endpoint.")
                                    }
                                    isSubmitting = false // Stop loading
                                }
                            },
                            onFailure = { errorMessage ->
                                Log.e("API_ERROR", "Failed to update status: $errorMessage")
                                isSubmitting = false // Stop loading
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = redcolor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = !isSubmitting // Disable button while loading
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .size(20.dp)
                    )
                } else {
                    Text("Submit report", fontSize = 16.sp)
                }
            }
        }
    }
}

fun update_status_inspection(context: Context, id: String, onComplete: (Boolean) -> Unit) {
    val client = OkHttpClient()
    val token = getToken(context)
    val url = "${TestApi.update_status_inspection}$id/"

    val requestBody = "".toRequestBody("application/json".toMediaType())

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("update_status_inspection", "API call failed: ${e.message}")
            onComplete(false)
        }

        override fun onResponse(call: Call, response: Response) {
            if (!response.isSuccessful) {
                Log.e("update_status_inspection", "API error: ${response.code}")
            } else {
                Log.d("update_status_inspection", "API success: ${response.code}")
            }
            onComplete(response.isSuccessful)
        }
    })
}



@SuppressLint("RememberReturnType")
@Composable
fun BodyPartsInspectionCard(
    modifier: Modifier = Modifier,
    onConditionSelected: (String, Map<String, String>) -> Unit,
    inspectionData: InspectionDataState
) {
    val carInspectionData = remember { CarInspectionData.carInspectionData }

    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogOptions by remember { mutableStateOf<Map<String, Map<String, Int>>>(emptyMap()) }
//    val selectedOptions = remember { mutableStateMapOf<String, Map<String, String>>() }
//    var currentPartName by remember { mutableStateOf("") }
    val selections = remember { mutableStateMapOf<String, Map<String, String>>() }
    var bodyPartsExpanded by remember { mutableStateOf(false) }
    val selectedOptions = remember { mutableStateMapOf<String, MutableMap<String, String>>() }

    var currentPartName by remember { mutableStateOf("") }
    val partImages = remember { mutableStateMapOf<String, MutableList<String>>() }

    val inspectionSelections = remember(selectedOptions) {
        derivedStateOf { selectedOptions.toMap() }
    }

    jsonvar.clear()
    jsonvar.putAll(selectedOptions)

    /*
        val inspectionData = LocalInspectionData.current
        LaunchedEffect(selections) {
            selections.forEach { (partName, conditions) ->
                onConditionSelected(partName, conditions)
                inspectionData.updateBodyParts(partName, conditions)
            }
        }
        // Update inspection data whenever selections change
        LaunchedEffect(selectedOptions) {
            Log.d("LAUNCHED_EFFECT", "selectedOptions triggered: $selectedOptions")
            selectedOptions.forEach { (partName, conditions) ->
                Log.d("LAUNCHED_EFFECT", "Calling onConditionSelected for $partName with $conditions")
                onConditionSelected(partName, conditions)
            }
        }*/
//    LaunchedEffect(selectedOptions) {
//        inspectionData.bodyPartsInspection.clear()
//        inspectionData.bodyPartsInspection.putAll(selectedOptions)
//        Log.d("BODY_PARTS_DEBUG", "Updated body parts: ${inspectionData.bodyPartsInspection}")
//    }

//    LaunchedEffect(selectedOptions) {
//        inspectionData.updateBodyParts(selectedOptions.toString())
//    }

//    LaunchedEffect(inspectionSelections.value) {
//        Log.d("INSPECTION_SELECTIONS", "Final selections: ${inspectionSelections.value}")
//        inspectionData.bodyPartsInspection.clear()
//        inspectionData.bodyPartsInspection.putAll(inspectionSelections.value)
//        logCompleteInspection(inspectionData)
//    }

    // 4. Convert to JSON whenever selections change
    LaunchedEffect(jsonvar.toMap().values) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val json = gson.toJson(jsonvar.toMap().values)

        // Print in both Logcat and System Console
        Log.d("Selected", "=== Body Parts Inspection Selections ===")
        Log.d("Selected", json)
        Log.d("Selected", "=======================================")

        println("=== Body Parts Inspection Selections ===")
        println(json)
        println("=======================================")

        // Additional debug
        Log.d("Selected", "Current part: $currentPartName")
        Log.d("Selected", "All selections:")
        inspectionSelections.value.forEach { (part, selections) ->
            Log.i("Selected", "JSON $part: $selections")
        }
    }

//    // 4. Convert to JSON whenever selections change
//    LaunchedEffect(inspectionSelections.value) {
//        val gson = GsonBuilder().setPrettyPrinting().create()
//        val json = gson.toJson(inspectionSelections.value)
//        Log.d("Selected","=== Body Parts Inspection Selections ===")
//        println(json)
//        Log.d("Selected","=======================================")
//
//        // Additional debug
//        Log.d("Selected","Current part: $currentPartName")
//        Log.d("Selected","All selections:")
//        inspectionSelections.value.forEach { (part, selections) ->
//            Log.i("Selected"," JSON $part: $selections")
//        }
//    }

    Card(

        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier
            .clickable { bodyPartsExpanded = !bodyPartsExpanded }
            .padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Body Parts Inspection",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(
                    onClick = { bodyPartsExpanded = !bodyPartsExpanded },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (bodyPartsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (bodyPartsExpanded) "Collapse" else "Expand"
                    )
                }
            }

            if (bodyPartsExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                carInspectionData["Car Detail"]?.get("Body Parts Inspection")?.let { bodyParts ->
                    Column {
                        bodyParts.forEach { (partCategory, partData) ->
                            Spacer(modifier = Modifier.height(8.dp))
                            ExpandableCards(
                                title = partCategory,
                                modifier = Modifier.padding(bottom = 8.dp),

                                ) {
                                Column {
                                    partData.forEach { (partName, partDetails) ->
                                        Spacer(modifier = Modifier.height(4.dp))
                                        InspectionItemCards(
                                            title = partName,
                                            onClick = {
                                                dialogTitle = partName
                                                currentPartName = partName // Store the current part name
                                                if (partDetails is Map<*, *>) {
                                                    val optionsMap = mutableMapOf<String, Map<String, Int>>()

                                                    partDetails.forEach { (detailName, detailValue) ->
                                                        if (detailValue is Map<*, *>) {
                                                            val options = detailValue.mapKeys { it.key.toString() }
                                                                .mapValues { it.value.toString().toInt() }
                                                            optionsMap[detailName.toString()] = options
                                                        }
                                                    }

                                                    dialogOptions = optionsMap

                                                    // Initialize with default selections if not already set
                                                    if (!selectedOptions.containsKey(partName)) {
                                                        val initialSelections = mutableMapOf<String, String>()
                                                        optionsMap.forEach { (category, options) ->
                                                            initialSelections[category] = options.entries.firstOrNull { it.value == 1 }?.key ?: ""
                                                        }
                                                        selectedOptions[partName] = initialSelections
                                                    }
                                                }
                                                showDialog = true
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

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(dialogTitle) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                    val imageUrls = remember(currentPartName) {
                        val bodyParts = (carInspectionData["Car Detail"] as? Map<String, *>)?.get("Body Parts Inspection") as? Map<String, *>
                        bodyParts?.values?.mapNotNull { partData ->
                            (partData as? Map<String, *>)?.entries?.firstOrNull { (key, _) ->
                                key == currentPartName
                            }?.let { (_, details) ->
                                (details as? Map<String, *>)?.get("img_urls") as? List<String>
                            }
                        }?.firstOrNull { !it.isNullOrEmpty() } ?: emptyList()
                    }

                    val shouldShowImageCard = imageUrls.isNotEmpty() ||
                            (currentPartName in listOf("Bonnet", "Front Driver Door", "Front Passenger Door",
                                "Rear Right Door", "Rear Right Fender", "Rear Left Fender",
                                "Stearing Wheel", "Dashboard", "Front Driver Seat",
                                "Front Passenger Seat", "Rear Seats", "Roof",
                                "Boot Floor"))

                    if (shouldShowImageCard) {
                        ImageUploadCards(
                            imageUrls = imageUrls,
                            cardName = currentPartName,
                            selectedImages = partImages.getOrPut(currentPartName) { mutableStateListOf() },
                            onImagesUpdated = { images ->
                                partImages[currentPartName] = images.toMutableStateList()
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    LaunchedEffect(Unit) {
                        if (!selectedOptions.containsKey(currentPartName)) {
                            selectedOptions[currentPartName] = mutableMapOf<String, String>().apply {
                                dialogOptions.forEach { (category, options) ->
                                    put(category, options.entries.firstOrNull { it.value == 1 }?.key ?: "")
                                }
                            }
                        }
                    }

                    dialogOptions.forEach { (category, options) ->
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )

                        options.forEach { (option, _) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val currentSelections = selectedOptions[currentPartName]?.toMutableMap() ?: mutableMapOf()
                                        currentSelections[category] = option
                                        selectedOptions[currentPartName] = currentSelections
                                    }
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = selectedOptions[currentPartName]?.get(category) == option,
                                    onClick = {
                                        val currentSelections = selectedOptions[currentPartName]?.toMutableMap() ?: mutableMapOf()
                                        currentSelections[category] = option
                                        selectedOptions[currentPartName] = currentSelections
                                    }
                                )
                                Text(
                                    text = option,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            },
//            confirmButton = {
//                Button(onClick = { showDialog = false }) {
//                    Text("Save")
//                }
//            },
            confirmButton = {
                Button(onClick = {
                    val currentConditions = dialogOptions.mapValues { (category, _) ->
                        selectedOptions[currentPartName]?.get(category) ?: ""
                    }
                    Log.d("DIALOG_SAVE", "Saving selections for $currentPartName: $currentConditions")
                    selections[currentPartName] = currentConditions
                    showDialog = false
                    // Update inspection data with current selections
//                    inspectionData.updateBodyParts(
//                        currentPartName,
//                        selectedOptions[currentPartName] ?: emptyMap()
//                    )
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}




fun logCompleteInspection(inspectionData: InspectionDataState) {
    inspectionData.toCompleteInspection()?.let { completeData ->
        val gson = GsonBuilder().setPrettyPrinting().create()
        val json = gson.toJson(completeData)
        Log.d("CompleteInspection", "Full Inspection Data:\n$json")
    }
}



fun postInspectionReport(
    context: Context,
    navController: NavController,
    id: String,
    inspectionData: InspectionDataState,
    token: String,
) {

    val gson = GsonBuilder().setPrettyPrinting().create()
Log.d("insp1","*****************************${inspectionData.bodyPartsInspection.values}")
    Log.d("insp2","*****************************${jsonvar.toMap()}")
    // Convert body parts to proper JSON structure
    val bodyPartsJson = JSONObject().apply {
        inspectionData.bodyPartsInspection.forEach { (partName, conditions) ->
            val partJson = JSONObject()
            conditions.forEach { (key, value) ->
                partJson.put(key, value)
            }
            put(partName, partJson)
        }
    }

    Log.d("BODY_PARTS_DATA", bodyPartsJson.toString(2))

    // Convert other data classes to JSON
    val basicInfoJson = inspectionData.basicInformation.value?.let {
        JSONObject(gson.toJson(it))
    } ?: JSONObject()

    val techSpecsJson = inspectionData.technicalSpecifications.value?.let {
        JSONObject(gson.toJson(it))
    } ?: JSONObject()

//    val ratingsJson = inspectionData.inspectionRatings.value?.let {
//        JSONObject(gson.toJson(it))
//    } ?: JSONObject()

    val mybodyparts = jsonvar.toMap().let {
        JSONObject(gson.toJson(it))
    }
    // Final object with correct backend key names
    val jsonObjectForBackend = JSONObject().apply {
        put("basicInfo", basicInfoJson)
        put("techSpecs", techSpecsJson)
        put("bodyParts", mybodyparts)  // Now includes proper body parts data
//        put("ratings", ratingsJson)

    }

    // Wrap into final payload
    val payload = JSONObject().apply {
        put("saler_car", id.toInt())
        put("json_obj", jsonObjectForBackend)
    }

    // Debug log
    Log.d("FinalPayload", payload.toString(2))

    // Prepare request
    val requestBody = payload.toString().toRequestBody("application/json".toMediaTypeOrNull())
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(TestApi.post_inspection_report)
        .post(requestBody)
        .addHeader("Authorization", "Bearer $token")
        .addHeader("Content-Type", "application/json")
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Report Submitted Successfully", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                    context.getSharedPreferences("inspection_reports", Context.MODE_PRIVATE)
                        .edit().putBoolean("report_$id", true).apply()
                } else {
                    Toast.makeText(context, "Failed: $responseBody", Toast.LENGTH_LONG).show()
                    Log.e("API_ERROR", "Error response: $responseBody")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("API_ERROR", "Exception: ${e.message}")
            }
        }
    }
}


data class BodyPartInspection(
    val partName: String,
    val conditions: Map<String, Map<String, Int>>
)

data class CarInspection(
    val bodyParts: Map<String, List<BodyPartInspection>>
)