package com.example.carapp.screens


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.assets.AssetHelper
import com.example.carapp.screens.Inspector.Inspectiod
import com.example.carapp.screens.Inspector.Inspectir
import com.example.carapp.screens.Inspector.UploadViewModel
import com.example.carapp.screens.Inspector.UploadViewModel2
import com.example.carapp.screens.Inspector.showImagePickerDialog
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
//import kotlinx.serialization.json.JsonObject
//import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject

val cardColor = Color(0xFFEAECEE)
val redcolor = Color(0xFFE52020)

fun saveAndLogFormData(
    selectedOption: String,
    inputFields: Map<String, FieldState>,
    context: Context,
    imageUrls: List<String> = emptyList(),
    primaryPhone: String? = null,
    secondaryPhone: String? = null,
    date: String? = null,
    timeSlot: String? = null
) {
    // Get existing JSON data from SharedPreferences
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val existingJsonString = prefs.getString("saved_form_data", "{}") ?: "{}"

    val normalizedFieldMap = mapOf(
        "Car Name" to "car_name",
    "Company" to "company",
    "Car Model" to "year",
    "Engine Capacity" to "engine_size",
    "KMs Driven" to "milage",
    "Paint Condition" to "paint_condition",
    "Select Option Type" to "specs",
    "Select Specification" to "option_type"
    )


    try {
        // Parse existing JSON
        val existingJson = Json.parseToJsonElement(existingJsonString).jsonObject

        // Create a mutable map from existing JSON
        val mergedData = existingJson.toMutableMap()

        // Update with new values if they're not null
        primaryPhone?.let { mergedData["primaryPhone"] = JsonPrimitive(it) }
        secondaryPhone?.let { mergedData["secondaryPhone"] = JsonPrimitive(it) }
        date?.let { mergedData["availabilityId"] = JsonPrimitive(it) }
        timeSlot?.let { mergedData["timeSlot"] = JsonPrimitive(it) }

        // Ensure selectedOption is up to date
        mergedData["selectedOption"] = JsonPrimitive(selectedOption)

        // Add/update input fields
//        inputFields.forEach { (fieldName, fieldState) ->
//            mergedData[fieldName] = JsonPrimitive(fieldState.value.value)
//        }

        inputFields.forEach { (fieldName, fieldState) ->
            val key = normalizedFieldMap[fieldName] ?: fieldName
            mergedData[key] = JsonPrimitive(fieldState.value.value)
        }

        // Add/update image URLs if available
        if (imageUrls.isNotEmpty()) {
            mergedData["imageUrls"] =
                kotlinx.serialization.json.JsonArray(imageUrls.map { JsonPrimitive(it) })
        }

        // Convert back to JSON string
        val jsonString = Json { prettyPrint = true }.encodeToString(mergedData)

        // Log the merged data
        Log.d("FORM_DATA", "Merged form data:\n$jsonString")

        // Save back to SharedPreferences
        prefs.edit().putString("saved_form_data", jsonString).apply()

    } catch (e: Exception) {
        Log.e("FORM_DATA", "Error merging JSON data", e)
        // Fallback to creating new JSON if merge fails
        val formDataJson = buildJsonObject {
            put("selectedOption", selectedOption)
            inputFields.forEach { (fieldName, fieldState) ->
                put(fieldName, fieldState.value.value)
            }
            primaryPhone?.let { put("primaryPhone", it) }
            secondaryPhone?.let { put("secondaryPhone", it) }
            date?.let { put("availabilityId", it) }
            timeSlot?.let { put("timeSlot", it) }
            if (imageUrls.isNotEmpty()) {
                put("imageUrls",
                    kotlinx.serialization.json.JsonArray(imageUrls.map { JsonPrimitive(it) })
                )
            }
        }
        val jsonString = Json { prettyPrint = true }.encodeToString(formDataJson)
        prefs.edit().putString("saved_form_data", jsonString).apply()
    }
}

@SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarSellScreen(navController: NavController) {
    // State variables
    val selectedImages = remember { mutableStateListOf<String>() }
    var description by remember { mutableStateOf("") }
    val descriptionError = remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Self") }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val imagesError = remember { mutableStateOf(false) }
    val selectedImagesCard1 = remember { mutableStateListOf<String>() }
    val viewModel: UploadViewModel2 = viewModel()
    var uploadResults by remember { mutableStateOf<List<Pair<String, JsonObject?>>?>(null) }

    // System UI
    val systemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = false

    // Back press handling
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog = true
            }
        }
    }
    val context = LocalContext.current
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    LaunchedEffect(Unit) {
        backDispatcher?.addCallback(backCallback)
    }

    // Dialog for back press confirmation
    if (showDialog) {
        CustomAlertDialog(
            onDismiss = { showDialog = false },
            onConfirm = {
                showDialog = false
                navController.navigate("seller") { popUpTo("basicInfoScreen") { inclusive = true } }
            }
        )
    }

    // Form configuration
    val dropdownOptions = mapOf(
        // Company details (will be in separate card)
        "Company" to DropdownConfig(
            options = listOf("Toyota", "Honda", "Suzuki", "BMW", "Mercedes", "Tesla", "Audi"),
            inputType = InputType.Dropdown,
            isRequired = true
        ),
        "Car Name" to DropdownConfig(
            options = listOf("Toyota", "Honda", "Suzuki", "BMW", "Mercedes"),
            inputType = InputType.Dropdown,
            isRequired = true
        ),
        "Car Model" to DropdownConfig(
            options = listOf("2015", "2016", "2017", "2018", "2019"),
            inputType = InputType.Dropdown,
            isRequired = true
        ),
        "Paint Choices" to DropdownConfig(
            options = listOf("ORIGINAL PAINT", "PARTIAL REPAINT", "FULL REPAINT", "I DONT KNOW"),
            inputType = InputType.Dropdown,
            isRequired = true
        ),
        "KMs Driven" to DropdownConfig(
            options = listOf("10,000", "20,000", "30,000", "40,000", "50,000+"),
            inputType = InputType.Number,
            isRequired = true,
            validation = { it.matches(Regex("^[0-9,]+$")) }
        ),
        "Engine Capacity" to DropdownConfig(
            options = listOf("1000", "1300", "1500", "1800", "2000"),
            inputType = InputType.Number,
            isRequired = true,
            validation = { it.matches(Regex("^[0-9]+$")) }
        ),
        "Select Specification" to DropdownConfig(
            options = listOf("GCC SPECS", "NON SPECS", "I DONT KNOW"),
            inputType = InputType.Dropdown,
            isRequired = true
        ),
        "Select Option Type" to DropdownConfig(
            options = listOf("BASIC", "MID OPTION", "FULL OPTION", "I DONT KNOW"),
            inputType = InputType.Dropdown,
            isRequired = true
        ),
    )

    val iconMapping = mapOf(
        "Company" to R.drawable.company,
        "Car Name" to R.drawable.car_icon,
        "Car Model" to R.drawable.car_icon,
        "Location" to R.drawable.location_icon,
        "Registered in" to R.drawable.register_icon,
        "Body color" to R.drawable.color_bucket_icon,
        "KMs Driven" to R.drawable.speed_icon,
        "Condition" to R.drawable.termsconditions,
        "Transmission" to R.drawable.varient,
        "Fuel type" to R.drawable.fueltype,
        "Assembly" to R.drawable.assembly,
        "Engine Capacity" to R.drawable.enginecapacity,
        "Price (PKR)" to R.drawable.description_icon,
        "Phone Number" to R.drawable.call
    )

    val inputFields = remember {
        dropdownOptions.keys.associateWith {
            FieldState(
                value = mutableStateOf(""),
                error = mutableStateOf(false),
                errorMessage = mutableStateOf("")
            )
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(
                        redcolor
                    )
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "Book Free Appointments Now!",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                )

            }
        },
        bottomBar = {},
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Fixed header section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    StepProgressIndicatorss(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 7.dp, vertical = 4.dp),
                        stepCount = 3,
                        currentStep = 1,
                        titles = listOf("Car Detail", "User Detail", "Inspector Detail")
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        color = Color.Gray,
                        thickness = 3.dp
                    )
                }

                // Scrollable content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Image slider card
//                    ImageSliderCard(selectedImages = selectedImages)
//                    ImageSliderCard(
//                        selectedImages = selectedImages,
//                        showError = imagesError.value,
//                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                    )
                    Inspectiodseller(
                        selectedImages = selectedImagesCard1,
                        showError = imagesError.value,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        cardName = "Seller images"
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Radio button selection card
                    RadioButtonSelectionCard(
                        selectedOption = selectedOption,
                        onOptionSelected = { selectedOption = it }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Company details card (new)
                    CompanyDetailsCard(
                        inputFields = inputFields,
                        iconMapping = iconMapping,
                        dropdownOptions = dropdownOptions
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CarDetailsCard(
                        inputFields = inputFields,
                        iconMapping = iconMapping,
                        dropdownOptions = dropdownOptions
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CarTechnicalDetailsCard(
                        inputFields = inputFields,
                        iconMapping = iconMapping,
                        dropdownOptions = dropdownOptions
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier
                            .alpha(if (selectedOption == "Other") 0.5f else 1f)
                            .pointerInput(Unit) {
                                if (selectedOption == "Other") {
                                    detectTapGestures {}
                                }
                            }
                    ) {
                        ProfessionalInputFields(
                            inputFields = inputFields.filterKeys {
                                it !in listOf("Company",
                                    "Car Name",
                                    "Car Model",
                                    "Paint Choices",
                                    "Engine Capacity",
                                    "KMs Driven",
                                    "Select Specification",
                                    "Select Option Type", )
                            },
                            iconMapping = iconMapping,
                            dropdownOptions = dropdownOptions.filterKeys {
                                it !in listOf("Company",
                                    "Car Name",
                                    "Car Model",
                                    "Paint Choices",
                                    "Engine Capacity",
                                    "KMs Driven",
                                    "Select Specification",
                                    "Select Option Type")
                            }
                        )


                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp)
                        ) {
                            // Button loading animation
                            var isLoading by remember { mutableStateOf(false) }

//                            if (isLoading) {
//                                CircularProgressIndicator(
//                                    modifier = Modifier
//                                        .align(Alignment.CenterHorizontally)
//                                        .padding(16.dp),
//                                    color = redcolor
//                                )
//                            }

                            /*Button(
                                onClick = {
                                    Log.d("FORM_SUBMIT", "Button pressed - starting validation")

                                    if (selectedOption == "Self") {
                                        Log.d("FORM_SUBMIT", "Self option selected")

                                        // Reset all errors
                                        inputFields.forEach { (_, fieldState) ->
                                            fieldState.error.value = false
                                        }

                                        // Validate only input fields
                                        var hasErrors = false
                                        inputFields.forEach { (fieldName, fieldState) ->
                                            val config = dropdownOptions[fieldName]
                                            if (config?.isRequired == true && fieldState.value.value.isBlank()) {
                                                fieldState.error.value = true
                                                fieldState.errorMessage.value = "This field is required"
                                                hasErrors = true
                                            }
                                        }

                                        if (!hasErrors) {
                                            Log.d("FORM_SUBMIT", "All validations passed")

                                            // Get all images from temp assets
                                            val imageFiles = AssetHelper.getTempAssets(context)
                                                .filter { it.isFile && it.exists() }

                                            Log.d("IMAGE_DEBUG", "Found ${imageFiles.size} valid images to upload")

                                            if (imageFiles.isNotEmpty()) {
                                                // Upload images and wait for all URLs
                                                viewModel.uploadImages(imageFiles.map { it.absolutePath }) { uploadResults ->
                                                    val successfulUploads = uploadResults.filter {
                                                        it.second?.get("success")?.asBoolean == true
                                                    }

                                                    val imageUrls = successfulUploads.mapNotNull {
                                                        it.second?.get("image_url")?.asString
                                                    }

                                                    Log.d("IMAGE_URLS", "Successfully uploaded URLs: $imageUrls")

                                                    // Now save the complete form data with URLs
                                                    saveAndLogFormData(
                                                        selectedOption = selectedOption,
                                                        inputFields = inputFields,
                                                        context = context,
                                                        imageUrls = imageUrls
                                                    )

                                                    // Clear temp assets after successful save
                                                    AssetHelper.clearTempAssets(context)

                                                    Toast.makeText(
                                                        context,
                                                        "Submitted with ${imageUrls.size} image(s)",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                navController.navigate("info") {
                                                    popUpTo(navController.graph.startDestinationId) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            } else {
                                                Log.d("IMAGE_DEBUG", "No images found in temp assets")
                                                // Save without images
                                                saveAndLogFormData(
                                                    selectedOption = selectedOption,
                                                    inputFields = inputFields,
                                                    context = context,
                                                    imageUrls = emptyList()
                                                )
                                                Toast.makeText(context, "Form submitted without images", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(1.0f),
                                colors = ButtonDefaults.buttonColors(containerColor = redcolor),
                                shape = RoundedCornerShape(8.dp),
                                enabled = true
                            ) {
                                Text("Book Inspection", fontSize = 16.sp)
                            }*/
                            Button(
                                /*onClick = {
                                    Log.d("FORM_SUBMIT", "Button pressed - starting validation")

                                    if (selectedOption == "Self") {
                                        Log.d("FORM_SUBMIT", "Self option selected")

                                        isLoading = true // Start loader

                                        // Reset all errors
                                        inputFields.forEach { (_, fieldState) ->
                                            fieldState.error.value = false
                                        }

                                        // Validate input fields
                                        var hasErrors = false
                                        inputFields.forEach { (fieldName, fieldState) ->
                                            val config = dropdownOptions[fieldName]
                                            if (config?.isRequired == true && fieldState.value.value.isBlank()) {
                                                fieldState.error.value = true
                                                fieldState.errorMessage.value = "This field is required"
                                                hasErrors = true
                                            }
                                        }

                                        if (!hasErrors) {
                                            val imageFiles = AssetHelper.getTempAssets(context)
                                                .filter { it.isFile && it.exists() }

                                            Log.d("IMAGE_DEBUG", "Found ${imageFiles.size} valid images to upload")

                                            if (imageFiles.isNotEmpty()) {
                                                // Upload images
                                                viewModel.uploadImages(imageFiles.map { it.absolutePath }) { uploadResults ->
                                                    val successfulUploads = uploadResults.filter {
                                                        it.second?.get("success")?.asBoolean == true
                                                    }

                                                    val imageUrls = successfulUploads.mapNotNull {
                                                        it.second?.get("image_url")?.asString
                                                    }

                                                    Log.d("IMAGE_URLS", "Successfully uploaded URLs: $imageUrls")

                                                    saveAndLogFormData(
                                                        selectedOption = selectedOption,
                                                        inputFields = inputFields,
                                                        context = context,
                                                        imageUrls = imageUrls
                                                    )

                                                    AssetHelper.clearTempAssets(context)

                                                    Toast.makeText(
                                                        context,
                                                        "Submitted with ${imageUrls.size} image(s)",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                    isLoading = false
                                                    navController.navigate("info") {
                                                        popUpTo(navController.graph.startDestinationId) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                }
                                            } else {
                                                Log.d("IMAGE_DEBUG", "No images found in temp assets")
                                                isLoading = false
                                                Toast.makeText(
                                                    context,
                                                    "Please attach at least one image before submitting.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } else {
                                            isLoading = false
                                        }
                                    }
                                },*/
                                onClick = {
                                    Log.d("FORM_SUBMIT", "Button pressed - starting validation")

                                    if (selectedOption == "Self") {
                                        Log.d("FORM_SUBMIT", "Self option selected")

                                        isLoading = true // Start loader

                                        // Reset all errors
                                        inputFields.forEach { (_, fieldState) ->
                                            fieldState.error.value = false
                                        }

                                        // Validate input fields
                                        var hasErrors = false
                                        inputFields.forEach { (fieldName, fieldState) ->
                                            val config = dropdownOptions[fieldName]
                                            if (config?.isRequired == true && fieldState.value.value.isBlank()) {
                                                fieldState.error.value = true
                                                fieldState.errorMessage.value = "This field is required"
                                                hasErrors = true
                                            }
                                        }

                                        if (!hasErrors) {
                                            val imageFiles = AssetHelper.getTempAssets(context)
                                                .filter { it.isFile && it.exists() }

                                            Log.d("IMAGE_DEBUG", "Found ${imageFiles.size} valid images to upload")

                                            if (imageFiles.isNotEmpty()) {
                                                // Upload images
                                                viewModel.uploadImages(imageFiles.map { it.absolutePath }) { uploadResults ->
                                                    val successfulUploads = uploadResults.filter {
                                                        it.second?.get("success")?.asBoolean == true
                                                    }

                                                    val imageUrls = successfulUploads.mapNotNull {
                                                        it.second?.get("image_url")?.asString
                                                    }

                                                    Log.d("IMAGE_URLS", "Successfully uploaded URLs: $imageUrls")

                                                    saveAndLogFormData(
                                                        selectedOption = selectedOption,
                                                        inputFields = inputFields,
                                                        context = context,
                                                        imageUrls = imageUrls
                                                    )

                                                    AssetHelper.clearTempAssets(context)

                                                    Toast.makeText(
                                                        context,
                                                        "Submitted with ${imageUrls.size} image(s)",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                    isLoading = false
                                                    navController.navigate("info") {
                                                        popUpTo(navController.graph.startDestinationId) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                }
                                            } else {
                                                Log.d("IMAGE_DEBUG", "No images found in temp assets")
                                                isLoading = false
                                                Toast.makeText(
                                                    context,
                                                    "Please attach at least one image before submitting.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } else {
                                            isLoading = false
                                            // Show toast for validation failure
                                            Toast.makeText(
                                                context,
                                                "Please fill all fields",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = redcolor),
                                shape = RoundedCornerShape(8.dp),
                                enabled = !isLoading // Disable while loading
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.Red,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Book Inspection", fontSize = 16.sp)
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
fun ProfessionalInputFields(
    inputFields: Map<String, FieldState>,
    iconMapping: Map<String, Int>,
    dropdownOptions: Map<String, DropdownConfig>
) {
//    var currentlyFocusedField by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        inputFields.keys.forEach { fieldName ->
            val fieldState = inputFields[fieldName] ?: return@forEach
            val textFieldValue = fieldState.value.value
            var expanded by remember { mutableStateOf(false) }
            val config = dropdownOptions[fieldName]
            val suggestions = config?.options ?: emptyList()
            val isDropdown = config?.inputType == InputType.Dropdown
            val isRequired = config?.isRequired ?: false
            val validation = config?.validation

            // Define isNumericField inside the loop
            val isNumericField = fieldName in listOf("Price (PKR)", "Phone Number", "Engine Capacity", "KMs Driven")
            val numericRegex = Regex("^[0-9,]*$")

//            val showError = isRequired &&
//                    textFieldValue.isBlank() &&
//                    (currentlyFocusedField != null && currentlyFocusedField != fieldName)
//
//            LaunchedEffect(currentlyFocusedField) {
//                expanded = currentlyFocusedField == fieldName && isDropdown
//            }
            val showError = fieldState.error.value
            if (showError != fieldState.error.value) {
                fieldState.error.value = showError
                fieldState.errorMessage.value = if (showError) "This field is required" else ""
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = fieldName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (fieldState.error.value) MaterialTheme.colorScheme.error else Color.Gray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        if (isRequired) {
                            Text(
                                text = " *",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = { newValue ->
                            if (isNumericField && !numericRegex.matches(newValue)) {
                                fieldState.error.value = true
                                fieldState.errorMessage.value = "Only numbers are allowed"
                            } else {
                                fieldState.value.value = newValue
                                fieldState.error.value = false
                            }

                            // Apply additional validation logic if needed
                            if (validation != null && !validation(newValue)) {
                                fieldState.error.value = true
                                fieldState.errorMessage.value = "Invalid format"
                            } else {
                                fieldState.error.value = false
                            }
                        },
                        placeholder = { Text("Enter ${fieldName.lowercase()}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .clickable(enabled = isDropdown) { expanded = !expanded },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (fieldState.error.value) MaterialTheme.colorScheme.error
                            else Color(0xFF1976D2),
                            unfocusedBorderColor = if (fieldState.error.value) MaterialTheme.colorScheme.error
                            else Color.Gray,
                            cursorColor = Color(0xFF1976D2),
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error,
                            errorSupportingTextColor = MaterialTheme.colorScheme.error
                        ),
                        isError = fieldState.error.value,
                        supportingText = {
                            if (fieldState.error.value) {
                                Text(
                                    text = fieldState.errorMessage.value,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
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
                            if (isDropdown) {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(
                                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                                        else Icons.Default.ArrowDropDown,
                                        contentDescription = if (expanded) "Collapse" else "Expand"
                                    )
                                }
                            }
                        },
                        readOnly = isDropdown,
                        keyboardOptions = if (isNumericField) KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
                    )
                    if (fieldName == "Company") {
                        Spacer(modifier = Modifier.height(16.dp))
                        CompanyIconSelector(
                            selectedCompany = textFieldValue,
                            onCompanySelected = { company ->
                                fieldState.value.value = company
                                fieldState.error.value = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (isDropdown) {
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
                                        fieldState.value.value = item
                                        expanded = false
                                        fieldState.error.value = false
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


/*
@Composable
fun ProfessionalInputFields(
    inputFields: Map<String, FieldState>,
    iconMapping: Map<String, Int>,
    dropdownOptions: Map<String, DropdownConfig>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        inputFields.keys.forEach { fieldName ->
            val fieldState = inputFields[fieldName] ?: return@forEach
            val textFieldValue = fieldState.value.value
            var expanded by remember { mutableStateOf(false) }
            val config = dropdownOptions[fieldName]
            val suggestions = config?.options ?: emptyList()
            val isDropdown = config?.inputType == InputType.Dropdown
            val isRequired = config?.isRequired ?: false
            val validation = config?.validation
            val isNumericField = fieldName in listOf("Price (PKR)", "Phone Number", "Engine Capacity", "KMs Driven")
            val numericRegex = Regex("^[0-9,]*$")
            val showError = fieldState.error.value

            Box(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = fieldName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (fieldState.error.value) MaterialTheme.colorScheme.error else Color.Gray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        if (isRequired) {
                            Text(
                                text = " *",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }

                    Box {
                        OutlinedTextField(
                            value = textFieldValue,
                            onValueChange = { newValue ->
                                if (isNumericField && !numericRegex.matches(newValue)) {
                                    fieldState.error.value = true
                                    fieldState.errorMessage.value = "Only numbers are allowed"
                                } else {
                                    fieldState.value.value = newValue
                                    fieldState.error.value = false
                                }

                                if (validation != null && !validation(newValue)) {
                                    fieldState.error.value = true
                                    fieldState.errorMessage.value = "Invalid format"
                                } else {
                                    fieldState.error.value = false
                                }
                            },
                            placeholder = { Text("Enter ${fieldName.lowercase()}") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (fieldState.error.value) MaterialTheme.colorScheme.error
                                else Color(0xFF1976D2),
                                unfocusedBorderColor = if (fieldState.error.value) MaterialTheme.colorScheme.error
                                else Color.Gray,
                                cursorColor = Color(0xFF1976D2),
                                errorBorderColor = MaterialTheme.colorScheme.error,
                                errorLabelColor = MaterialTheme.colorScheme.error,
                                errorSupportingTextColor = MaterialTheme.colorScheme.error
                            ),
                            isError = fieldState.error.value,
                            supportingText = {
                                if (fieldState.error.value) {
                                    Text(
                                        text = fieldState.errorMessage.value,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
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
                                if (isDropdown) {
                                    Icon(
                                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                                        else Icons.Default.ArrowDropDown,
                                        contentDescription = if (expanded) "Collapse" else "Expand"
                                    )
                                }
                            },
                            readOnly = isDropdown,
                            keyboardOptions = if (isNumericField) KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number) else KeyboardOptions.Default
                        )

                        // Invisible clickable overlay for dropdown fields
                        if (isDropdown) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { expanded = !expanded }
                                    .alpha(0f)
                            )
                        }
                    }

                    if (fieldName == "Company") {
                        Spacer(modifier = Modifier.height(16.dp))
                        CompanyIconSelector(
                            selectedCompany = textFieldValue,
                            onCompanySelected = { company ->
                                fieldState.value.value = company
                                fieldState.error.value = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (isDropdown) {
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
//                                .fillMaxWidth()
                                .background(Color.White)
                                .widthIn(min = with(LocalDensity.current) {
                                    (LocalConfiguration.current.screenWidthDp.dp - 32.dp) }
                                )
                        ) {
                            suggestions.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        fieldState.value.value = item
                                        expanded = false
                                        fieldState.error.value = false
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
*/

@Composable
fun InputFieldWithDropdown(
    label: String,
    icon: Int,
    dropdownItems: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(selectedItem) }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newText ->
                textFieldValue = newText
                onItemSelected(newText)
            },
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = label
                )
            },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            dropdownItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        textFieldValue = item
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}


/*@Composable
fun InputFieldWithDropdown(
    label: String,
    icon: Int,
    dropdownItems: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(selectedItem) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { newText ->
                    textFieldValue = newText
                    onItemSelected(newText)
                },
                label = { Text(label) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = label
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded = !expanded }
                    .alpha(0f)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            dropdownItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        textFieldValue = item
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}*/

// ALL PERFECT THIS BELOW
/*@Composable
fun CompanyDetailsCard(
    inputFields: Map<String, FieldState>,
    iconMapping: Map<String, Int>,
    dropdownOptions: Map<String, DropdownConfig>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
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
            // Header row remains the same
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Car Details",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Color.Gray
                )
            }

            if (expanded) {
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Company Field
                    val companyField = inputFields["Company"] ?: return@Column
                    val companyConfig = dropdownOptions["Company"] ?: return@Column
                    var companyExpanded by remember { mutableStateOf(false) }
                    val showCompanyError = companyField.error.value

                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Label row remains the same
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Company",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (showCompanyError) MaterialTheme.colorScheme.error else Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            if (companyConfig.isRequired) {
                                Text(
                                    text = " *",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }

                        OutlinedTextField(
                            value = companyField.value.value,
                            onValueChange = { },
                            placeholder = { Text("Select company") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { companyExpanded = !companyExpanded },
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (showCompanyError) MaterialTheme.colorScheme.error
                                else Color(0xFF1976D2),
                                unfocusedBorderColor = if (showCompanyError) MaterialTheme.colorScheme.error
                                else Color.Gray,
                                errorBorderColor = MaterialTheme.colorScheme.error
                            ),
                            isError = showCompanyError,
                            supportingText = {
                                if (showCompanyError) {
                                    Text(
                                        text = companyField.errorMessage.value.ifEmpty { "This field is required" },
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(
                                        id = iconMapping["Company"] ?: R.drawable.company
                                    ),
                                    contentDescription = "Company",
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { companyExpanded = !companyExpanded }) {
                                    Icon(
                                        imageVector = if (companyExpanded) Icons.Default.KeyboardArrowUp
                                        else Icons.Default.ArrowDropDown,
                                        contentDescription = if (companyExpanded) "Collapse" else "Expand"
                                    )
                                }
                            },
                            readOnly = true
                        )

                        DropdownMenu(
                            expanded = companyExpanded,
                            onDismissRequest = { companyExpanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            companyConfig.options.forEachIndexed { index, item ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            companyField.value.value = item
                                            companyExpanded = false
                                            companyField.error.value = false
                                        }
                                    )
                                    if (index < companyConfig.options.size - 1) {
                                        Divider(
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            thickness = 0.5.dp,
                                            color = Color.LightGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    CompanyIconSelector(
                        selectedCompany = companyField.value.value,
                        onCompanySelected = { company ->
                            companyField.value.value = company
                            companyField.error.value = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Car Name Field - same changes as Company field
                    val carNameField = inputFields["Car Name"] ?: return@Column
                    val carNameConfig = dropdownOptions["Car Name"] ?: return@Column
                    var carNameExpanded by remember { mutableStateOf(false) }
                    val showCarNameError = carNameField.error.value

                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Label row remains the same
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Car Name",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (showCarNameError) MaterialTheme.colorScheme.error else Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            if (carNameConfig.isRequired) {
                                Text(
                                    text = " *",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }

                        OutlinedTextField(
                            value = carNameField.value.value,
                            onValueChange = { },
                            placeholder = { Text("Select car name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { carNameExpanded = !carNameExpanded },
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (showCarNameError) MaterialTheme.colorScheme.error
                                else Color(0xFF1976D2),
                                unfocusedBorderColor = if (showCarNameError) MaterialTheme.colorScheme.error
                                else Color.Gray,
                                errorBorderColor = MaterialTheme.colorScheme.error
                            ),
                            isError = showCarNameError,
                            supportingText = {
                                if (showCarNameError) {
                                    Text(
                                        text = carNameField.errorMessage.value.ifEmpty { "This field is required" },
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = iconMapping["Car Name"] ?: R.drawable.car_icon),
                                    contentDescription = "Car Name",
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { carNameExpanded = !carNameExpanded }) {
                                    Icon(
                                        imageVector = if (carNameExpanded) Icons.Default.KeyboardArrowUp
                                        else Icons.Default.ArrowDropDown,
                                        contentDescription = if (carNameExpanded) "Collapse" else "Expand"
                                    )
                                }
                            },
                            readOnly = true
                        )

                        DropdownMenu(
                            expanded = carNameExpanded,
                            onDismissRequest = { carNameExpanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            carNameConfig.options.forEachIndexed { index, item ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            carNameField.value.value = item
                                            carNameExpanded = false
                                            carNameField.error.value = false
                                        }
                                    )
                                    if (index < carNameConfig.options.size - 1) {
                                        Divider(
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            thickness = 0.5.dp,
                                            color = Color.LightGray
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Car Model Field - same changes as other fields
                    val carModelField = inputFields["Car Model"] ?: return@Column
                    val carModelConfig = dropdownOptions["Car Model"] ?: return@Column
                    var carModelExpanded by remember { mutableStateOf(false) }
                    val showCarModelError = carModelField.error.value

                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Label row remains the same
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Car Model",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (showCarModelError) MaterialTheme.colorScheme.error else Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            if (carModelConfig.isRequired) {
                                Text(
                                    text = " *",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }

                        OutlinedTextField(
                            value = carModelField.value.value,
                            onValueChange = {  },
                            placeholder = { Text("Select car model") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { carModelExpanded = !carModelExpanded },
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (showCarModelError) MaterialTheme.colorScheme.error
                                else Color(0xFF1976D2),
                                unfocusedBorderColor = if (showCarModelError) MaterialTheme.colorScheme.error
                                else Color.Gray,
                                errorBorderColor = MaterialTheme.colorScheme.error
                            ),
                            isError = showCarModelError,
                            supportingText = {
                                if (showCarModelError) {
                                    Text(
                                        text = carModelField.errorMessage.value.ifEmpty { "This field is required" },
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = iconMapping["Car Model"] ?: R.drawable.car_icon),
                                    contentDescription = "Car Model",
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { carModelExpanded = !carModelExpanded }) {
                                    Icon(
                                        imageVector = if (carModelExpanded) Icons.Default.KeyboardArrowUp
                                        else Icons.Default.ArrowDropDown,
                                        contentDescription = if (carModelExpanded) "Collapse" else "Expand"
                                    )
                                }
                            },
                            readOnly = true
                        )

                        DropdownMenu(
                            expanded = carModelExpanded,
                            onDismissRequest = { carModelExpanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            carModelConfig.options.forEachIndexed { index, item ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            carModelField.value.value = item
                                            carModelExpanded = false
                                            carModelField.error.value = false
                                        }
                                    )
                                    if (index < carModelConfig.options.size - 1) {
                                        Divider(
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            thickness = 0.5.dp,
                                            color = Color.LightGray
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
}*/
@Composable
fun CompanyDetailsCard(
    inputFields: Map<String, FieldState>,
    iconMapping: Map<String, Int>,
    dropdownOptions: Map<String, DropdownConfig>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
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
            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Car Details",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Color.Gray
                )
            }

            if (expanded) {
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Company Field
                    val companyField = inputFields["Company"] ?: return@Column
                    val companyConfig = dropdownOptions["Company"] ?: return@Column
                    var companyExpanded by remember { mutableStateOf(false) }
                    val showCompanyError = companyField.error.value

                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Label row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Company",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (showCompanyError) MaterialTheme.colorScheme.error else Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            if (companyConfig.isRequired) {
                                Text(
                                    text = " *",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }

                        // Text field with dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = companyField.value.value,
                                onValueChange = { },
                                placeholder = { Text("Select company") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { companyExpanded = true },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = if (showCompanyError) MaterialTheme.colorScheme.error
                                    else Color(0xFF1976D2),
                                    unfocusedBorderColor = if (showCompanyError) MaterialTheme.colorScheme.error
                                    else Color.Gray,
                                    errorBorderColor = MaterialTheme.colorScheme.error
                                ),
                                isError = showCompanyError,
                                supportingText = {
                                    if (showCompanyError) {
                                        Text(
                                            text = companyField.errorMessage.value.ifEmpty { "This field is required" },
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(
                                            id = iconMapping["Company"] ?: R.drawable.company
                                        ),
                                        contentDescription = "Company",
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (companyExpanded) Icons.Default.KeyboardArrowUp
                                        else Icons.Default.ArrowDropDown,
                                        contentDescription = if (companyExpanded) "Collapse" else "Expand",
                                        modifier = Modifier.clickable { companyExpanded = !companyExpanded }
                                    )
                                },
                                readOnly = true
                            )

                            DropdownMenu(
                                expanded = companyExpanded,
                                onDismissRequest = { companyExpanded = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                companyConfig.options.forEachIndexed { index, item ->
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = {
                                                companyField.value.value = item
                                                companyExpanded = false
                                                companyField.error.value = false
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        if (index < companyConfig.options.size - 1) {
                                            Divider(
                                                modifier = Modifier.padding(horizontal = 8.dp),
                                                thickness = 0.5.dp,
                                                color = Color.LightGray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        // Inside the Column for "Company"


                        /*Box(modifier = Modifier
                            .fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = companyField.value.value,
                                onValueChange = { },
                                placeholder = { Text("Select company") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned { coordinates ->
                                        textFieldSize = coordinates.size
                                    }
                                    .clickable { companyExpanded = !companyExpanded },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = if (showCompanyError) MaterialTheme.colorScheme.error else Color(0xFF1976D2),
                                    unfocusedBorderColor = if (showCompanyError) MaterialTheme.colorScheme.error else Color.Gray,
                                    errorBorderColor = MaterialTheme.colorScheme.error
                                ),
                                isError = showCompanyError,
                                readOnly = true,
                                supportingText = {
                                    if (showCompanyError) {
                                        Text(
                                            text = companyField.errorMessage.value.ifEmpty { "This field is required" },
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(
                                            id = iconMapping["Company"] ?: R.drawable.company
                                        ),
                                        contentDescription = "Company",
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (companyExpanded) Icons.Default.KeyboardArrowUp
                                        else Icons.Default.ArrowDropDown,
                                        contentDescription = if (companyExpanded) "Collapse" else "Expand"
                                    )
                                }
                            )
                            DropdownMenu(
                                expanded = companyExpanded,
                                onDismissRequest = { companyExpanded = false },
                                modifier = Modifier
                                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                            ) {
                                companyConfig.options.forEachIndexed { index, item ->
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = {
                                                companyField.value.value = item
                                                companyExpanded = false
                                                companyField.error.value = false
                                            }
                                        )
                                        if (index < companyConfig.options.size - 1) {
                                            Divider(
                                                modifier = Modifier.padding(horizontal = 8.dp),
                                                thickness = 0.5.dp,
                                                color = Color.LightGray
                                            )
                                        }
                                    }
                                }
                            }
                        }*/
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    CompanyIconSelector(
                        selectedCompany = companyField.value.value,
                        onCompanySelected = { company ->
                            companyField.value.value = company
                            companyField.error.value = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Car Name Field - same pattern as Company field
                    val carNameField = inputFields["Car Name"] ?: return@Column
                    val carNameConfig = dropdownOptions["Car Name"] ?: return@Column
                    var carNameExpanded by remember { mutableStateOf(false) }
                    val showCarNameError = carNameField.error.value

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Car Name",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (showCarNameError) MaterialTheme.colorScheme.error else Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            if (carNameConfig.isRequired) {
                                Text(
                                    text = " *",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = carNameField.value.value,
                                onValueChange = { },
                                placeholder = { Text("Select car name") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { carNameExpanded = true }
                                    .onGloballyPositioned { coordinates ->
                                        textFieldSize = coordinates.size
                                    }
                                    .clickable { companyExpanded = !companyExpanded },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = if (showCarNameError) MaterialTheme.colorScheme.error
                                    else Color(0xFF1976D2),
                                    unfocusedBorderColor = if (showCarNameError) MaterialTheme.colorScheme.error
                                    else Color.Gray,
                                    errorBorderColor = MaterialTheme.colorScheme.error
                                ),
                                isError = showCarNameError,
                                supportingText = {
                                    if (showCarNameError) {
                                        Text(
                                            text = carNameField.errorMessage.value.ifEmpty { "This field is required" },
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = iconMapping["Car Name"] ?: R.drawable.car_icon),
                                        contentDescription = "Car Name",
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (carNameExpanded) Icons.Default.KeyboardArrowUp
                                        else Icons.Default.ArrowDropDown,
                                        contentDescription = if (carNameExpanded) "Collapse" else "Expand",
                                        modifier = Modifier.clickable { carNameExpanded = !carNameExpanded }
                                    )
                                },
                                readOnly = true
                            )

                            DropdownMenu(
                                expanded = carNameExpanded,
                                onDismissRequest = { carNameExpanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                carNameConfig.options.forEachIndexed { index, item ->
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = {
                                                carNameField.value.value = item
                                                carNameExpanded = false
                                                carNameField.error.value = false
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        if (index < carNameConfig.options.size - 1) {
                                            Divider(
                                                modifier = Modifier.padding(horizontal = 8.dp),
                                                thickness = 0.5.dp,
                                                color = Color.LightGray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Car Model Field - same pattern as other fields
                    val carModelField = inputFields["Car Model"] ?: return@Column
                    val carModelConfig = dropdownOptions["Car Model"] ?: return@Column
                    var carModelExpanded by remember { mutableStateOf(false) }
                    val showCarModelError = carModelField.error.value

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Car Model",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (showCarModelError) MaterialTheme.colorScheme.error else Color.Gray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            if (carModelConfig.isRequired) {
                                Text(
                                    text = " *",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = carModelField.value.value,
                                onValueChange = {  },
                                placeholder = { Text("Select car model") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { carModelExpanded = true }
                                    .onGloballyPositioned { coordinates ->
                                        textFieldSize = coordinates.size
                                    }
                                    .clickable { companyExpanded = !companyExpanded },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = if (showCarModelError) MaterialTheme.colorScheme.error
                                    else Color(0xFF1976D2),
                                    unfocusedBorderColor = if (showCarModelError) MaterialTheme.colorScheme.error
                                    else Color.Gray,
                                    errorBorderColor = MaterialTheme.colorScheme.error
                                ),
                                isError = showCarModelError,
                                supportingText = {
                                    if (showCarModelError) {
                                        Text(
                                            text = carModelField.errorMessage.value.ifEmpty { "This field is required" },
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = iconMapping["Car Model"] ?: R.drawable.car_icon),
                                        contentDescription = "Car Model",
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (carModelExpanded) Icons.Default.KeyboardArrowUp
                                        else Icons.Default.ArrowDropDown,
                                        contentDescription = if (carModelExpanded) "Collapse" else "Expand",
                                        modifier = Modifier.clickable { carModelExpanded = !carModelExpanded }
                                    )
                                },
                                readOnly = true
                            )

                            DropdownMenu(
                                expanded = carModelExpanded,
                                onDismissRequest = { carModelExpanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                carModelConfig.options.forEachIndexed { index, item ->
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = {
                                                carModelField.value.value = item
                                                carModelExpanded = false
                                                carModelField.error.value = false
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        if (index < carModelConfig.options.size - 1) {
                                            Divider(
                                                modifier = Modifier.padding(horizontal = 8.dp),
                                                thickness = 0.5.dp,
                                                color = Color.LightGray
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
}
// BELOW IS COMPLETED AS PER REQ
@Composable
fun CarDetailsCard(
    inputFields: Map<String, FieldState>,
    iconMapping: Map<String, Int>,
    dropdownOptions: Map<String, DropdownConfig>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable {  }
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
            // Header row remains the same
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Additional Car Details",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Color.Gray
                )
            }

            if (expanded) {
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val fieldsInThisCard = listOf(
                        "Paint Choices",
                        "Engine Capacity",
                    )

                    fieldsInThisCard.forEach { fieldName ->
                        val fieldState = inputFields[fieldName] ?: return@forEach
                        val fieldConfig = dropdownOptions[fieldName] ?: return@forEach
                        var fieldExpanded by remember { mutableStateOf(false) }
                        val showFieldError = fieldState.error.value

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = fieldName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (showFieldError) MaterialTheme.colorScheme.error else Color.Gray,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                if (fieldConfig.isRequired) {
                                    Text(
                                        text = " *",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }

                           /* OutlinedTextField(
                                value = fieldState.value.value,
                                onValueChange = { newValue ->
                                    if (fieldConfig.inputType == InputType.Number) {
                                        if (newValue.matches(Regex("^[0-9]*$"))) {
                                            fieldState.value.value = newValue
                                            fieldState.error.value = false
                                        }
                                    } else {
                                        fieldState.value.value = newValue
                                        fieldState.error.value = false
                                    }
                                },
                                placeholder = { Text("Select $fieldName") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { fieldExpanded = fieldConfig.inputType == InputType.Dropdown },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = if (showFieldError) MaterialTheme.colorScheme.error
                                    else Color(0xFF1976D2),
                                    unfocusedBorderColor = if (showFieldError) MaterialTheme.colorScheme.error
                                    else Color.Gray,
                                    errorBorderColor = MaterialTheme.colorScheme.error
                                ),
                                isError = showFieldError,
                                supportingText = {
                                    if (showFieldError) {
                                        Text(
                                            text = fieldState.errorMessage.value.ifEmpty { "This field is required" },
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = iconMapping[fieldName] ?: R.drawable.male_icon),
                                        contentDescription = fieldName,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = {
                                    if (fieldConfig.inputType == InputType.Dropdown) {
                                        IconButton(onClick = { fieldExpanded = !fieldExpanded }) {
                                            Icon(
                                                imageVector = if (fieldExpanded) Icons.Default.KeyboardArrowUp
                                                else Icons.Default.ArrowDropDown,
                                                contentDescription = if (fieldExpanded) "Collapse" else "Expand"
                                            )
                                        }
                                    }
                                },
                                readOnly = fieldConfig.inputType == InputType.Dropdown,
                                keyboardOptions = if (fieldConfig.inputType == InputType.Number) {
                                    KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                } else {
                                    KeyboardOptions.Default
                                }
                            )

                            if (fieldConfig.inputType == InputType.Dropdown) {
                                DropdownMenu(
                                    expanded = fieldExpanded,
                                    onDismissRequest = { fieldExpanded = false },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    fieldConfig.options.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = {
                                                fieldState.value.value = item
                                                fieldExpanded = false
                                                fieldState.error.value = false
                                            }
                                        )
                                    }
                                }
                            }*/
                            Box(modifier = Modifier.fillMaxWidth()) {
                                var textFieldSize by remember { mutableStateOf(IntSize.Zero) }

                                OutlinedTextField(
                                    value = fieldState.value.value,
                                    onValueChange = {},
                                    placeholder = { Text("Select $fieldName") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .onGloballyPositioned { coordinates ->
                                            textFieldSize = coordinates.size
                                        }
                                        .clickable {
                                            if (fieldConfig.inputType == InputType.Dropdown) {
                                                fieldExpanded = true
                                            }
                                        },
                                    shape = RoundedCornerShape(8.dp),
                                    readOnly = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = if (showFieldError) MaterialTheme.colorScheme.error
                                        else Color(0xFF1976D2),
                                        unfocusedBorderColor = if (showFieldError) MaterialTheme.colorScheme.error
                                        else Color.Gray,
                                        errorBorderColor = MaterialTheme.colorScheme.error
                                    ),
                                    isError = showFieldError,
                                    supportingText = {
                                        if (showFieldError) {
                                            Text(
                                                text = fieldState.errorMessage.value.ifEmpty { "This field is required" },
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = iconMapping[fieldName] ?: R.drawable.male_icon),
                                            contentDescription = fieldName,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            fieldExpanded = !fieldExpanded
                                        }) {
                                            Icon(
                                                imageVector = if (fieldExpanded) Icons.Default.KeyboardArrowUp
                                                else Icons.Default.ArrowDropDown,
                                                contentDescription = if (fieldExpanded) "Collapse" else "Expand"
                                            )
                                        }
                                    }
                                )

                                DropdownMenu(
                                    expanded = fieldExpanded,
                                    onDismissRequest = { fieldExpanded = false },
                                    modifier = Modifier
                                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                                ) {
                                    fieldConfig.options.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = {
                                                fieldState.value.value = item
                                                fieldExpanded = false
                                                fieldState.error.value = false
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

/*@Composable
fun CarTechnicalDetailsCard(
    inputFields: Map<String, FieldState>,
    iconMapping: Map<String, Int>,
    dropdownOptions: Map<String, DropdownConfig>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
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
            // Header row remains the same
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Technical & Registration Details",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Color.Gray
                )
            }

            if (expanded) {
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val fieldsInThisCard = listOf(
                        "KMs Driven",
                        "Select Option Type",
                        "Select Specification"
                    )

                    fieldsInThisCard.forEach { fieldName ->
                        val fieldState = inputFields[fieldName] ?: return@forEach
                        val fieldConfig = dropdownOptions[fieldName] ?: return@forEach
                        var fieldExpanded by remember { mutableStateOf(false) }
                        val showFieldError = fieldState.error.value

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = fieldName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (showFieldError) MaterialTheme.colorScheme.error else Color.Gray,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                if (fieldConfig.isRequired) {
                                    Text(
                                        text = " *",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }

                            when (fieldName) {
                                "KMs Driven" -> {
                                    OutlinedTextField(
                                        value = fieldState.value.value,
                                        onValueChange = { newValue ->
                                            if (newValue.matches(Regex("^[0-9,]*$"))) {
                                                fieldState.value.value = newValue
                                                fieldState.error.value = false
                                            }
                                        },
                                        placeholder = { Text("Enter kilometers driven") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = if (showFieldError) MaterialTheme.colorScheme.error
                                            else Color(0xFF1976D2),
                                            unfocusedBorderColor = if (showFieldError) MaterialTheme.colorScheme.error
                                            else Color.Gray,
                                            errorBorderColor = MaterialTheme.colorScheme.error
                                        ),
                                        isError = showFieldError,
                                        supportingText = {
                                            if (showFieldError) {
                                                Text(
                                                    text = fieldState.errorMessage.value.ifEmpty { "This field is required" },
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        },
                                        leadingIcon = {
                                            Icon(
                                                painter = painterResource(id = iconMapping[fieldName] ?: R.drawable.male_icon),
                                                contentDescription = fieldName,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number
                                        )
                                    )
                                }

                                "Select Option Type" -> {
                                    Box {
                                        OutlinedTextField(
                                            value = fieldState.value.value,
                                            onValueChange = {},
                                            placeholder = { Text("Select option type") },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { fieldExpanded = true },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = if (showFieldError) MaterialTheme.colorScheme.error
                                                else Color(0xFF1976D2),
                                                unfocusedBorderColor = if (showFieldError) MaterialTheme.colorScheme.error
                                                else Color.Gray,
                                                errorBorderColor = MaterialTheme.colorScheme.error
                                            ),
                                            isError = showFieldError,
                                            supportingText = {
                                                if (showFieldError) {
                                                    Text(
                                                        text = fieldState.errorMessage.value.ifEmpty { "Please select an option type" },
                                                        color = MaterialTheme.colorScheme.error
                                                    )
                                                }
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    painter = painterResource(id = iconMapping[fieldName] ?: R.drawable.male_icon),
                                                    contentDescription = fieldName,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            },
                                            trailingIcon = {
                                                Icon(
                                                    imageVector = if (fieldExpanded) Icons.Default.KeyboardArrowUp
                                                    else Icons.Default.ArrowDropDown,
                                                    contentDescription = if (fieldExpanded) "Collapse" else "Expand"
                                                )
                                            },
                                            readOnly = true
                                        )

                                        DropdownMenu(
                                            expanded = fieldExpanded,
                                            onDismissRequest = { fieldExpanded = false },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            listOf("BASIC", "MID OPTION", "FULL OPTION", "I DONT KNOW").forEach { item ->
                                                DropdownMenuItem(
                                                    text = { Text(item) },
                                                    onClick = {
                                                        fieldState.value.value = item
                                                        fieldExpanded = false
                                                        fieldState.error.value = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }

                                "Select Specification" -> {
                                    Box {
                                        OutlinedTextField(
                                            value = fieldState.value.value,
                                            onValueChange = {},
                                            placeholder = { Text("Select specification") },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { fieldExpanded = true },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = if (showFieldError) MaterialTheme.colorScheme.error
                                                else Color(0xFF1976D2),
                                                unfocusedBorderColor = if (showFieldError) MaterialTheme.colorScheme.error
                                                else Color.Gray,
                                                errorBorderColor = MaterialTheme.colorScheme.error
                                            ),
                                            isError = showFieldError,
                                            supportingText = {
                                                if (showFieldError) {
                                                    Text(
                                                        text = fieldState.errorMessage.value.ifEmpty { "Please select a specification" },
                                                        color = MaterialTheme.colorScheme.error
                                                    )
                                                }
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    painter = painterResource(id = iconMapping[fieldName] ?: R.drawable.male_icon),
                                                    contentDescription = fieldName,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            },
                                            trailingIcon = {
                                                Icon(
                                                    imageVector = if (fieldExpanded) Icons.Default.KeyboardArrowUp
                                                    else Icons.Default.ArrowDropDown,
                                                    contentDescription = if (fieldExpanded) "Collapse" else "Expand"
                                                )
                                            },
                                            readOnly = true
                                        )

                                        DropdownMenu(
                                            expanded = fieldExpanded,
                                            onDismissRequest = { fieldExpanded = false },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            listOf("GCC SPECS", "NON SPECS", "I DONT KNOW").forEach { item ->
                                                DropdownMenuItem(
                                                    text = { Text(item) },
                                                    onClick = {
                                                        fieldState.value.value = item
                                                        fieldExpanded = false
                                                        fieldState.error.value = false
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
    }
}*/

@Composable
fun CarTechnicalDetailsCard(
    inputFields: Map<String, FieldState>,
    iconMapping: Map<String, Int>,
    dropdownOptions: Map<String, DropdownConfig>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
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
            // Header row remains the same
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Technical & Registration Details",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Color.Gray
                )
            }

            if (expanded) {
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val fieldsInThisCard = listOf(
                        "KMs Driven",
                        "Select Specification",
                        "Select Option Type",
                    )

                    fieldsInThisCard.forEach { fieldName ->
                        val fieldState = inputFields[fieldName] ?: return@forEach
                        val fieldConfig = dropdownOptions[fieldName] ?: return@forEach
                        var fieldExpanded by remember { mutableStateOf(false) }
                        val showFieldError = fieldState.error.value

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = fieldName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (showFieldError) MaterialTheme.colorScheme.error else Color.Gray,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                if (fieldConfig.isRequired) {
                                    Text(
                                        text = " *",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }

                            /*OutlinedTextField(
                                value = fieldState.value.value,
                                onValueChange = { newValue ->
                                    when (fieldConfig.inputType) {
                                        InputType.Number -> {
                                            if (newValue.matches(Regex("^[0-9,]*$"))) {
                                                fieldState.value.value = newValue
                                                fieldState.error.value = false
                                            }
                                        }
                                        else -> {
                                            fieldState.value.value = newValue
                                            fieldState.error.value = false
                                        }
                                    }
                                },
                                placeholder = {
                                    Text(
                                        when (fieldName) {
                                            "KMs Driven" -> "Enter kilometers driven"
                                            else -> "Enter $fieldName"
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = fieldConfig.inputType == InputType.Dropdown) {
                                        fieldExpanded = fieldConfig.inputType == InputType.Dropdown
                                    },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = if (showFieldError) MaterialTheme.colorScheme.error
                                    else Color(0xFF1976D2),
                                    unfocusedBorderColor = if (showFieldError) MaterialTheme.colorScheme.error
                                    else Color.Gray,
                                    errorBorderColor = MaterialTheme.colorScheme.error
                                ),
                                isError = showFieldError,
                                supportingText = {
                                    if (showFieldError) {
                                        Text(
                                            text = fieldState.errorMessage.value.ifEmpty { "This field is required" },
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = iconMapping[fieldName] ?: R.drawable.male_icon),
                                        contentDescription = fieldName,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = {
                                    if (fieldConfig.inputType == InputType.Dropdown) {
                                        IconButton(onClick = { fieldExpanded = !fieldExpanded }) {
                                            Icon(
                                                imageVector = if (fieldExpanded) Icons.Default.KeyboardArrowUp
                                                else Icons.Default.ArrowDropDown,
                                                contentDescription = if (fieldExpanded) "Collapse" else "Expand"
                                            )
                                        }
                                    }
                                },
                                readOnly = fieldConfig.inputType == InputType.Dropdown,
                                keyboardOptions = when {
                                    fieldName == "Phone Number" -> KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Phone
                                    )
                                    fieldConfig.inputType == InputType.Number -> KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number
                                    )
                                    else -> KeyboardOptions.Default
                                }
                            )
                            if (fieldConfig.inputType == InputType.Dropdown) {
                                DropdownMenu(
                                    expanded = fieldExpanded,
                                    onDismissRequest = { fieldExpanded = false },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    fieldConfig.options.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = {
                                                fieldState.value.value = item
                                                fieldExpanded = false
                                                fieldState.error.value = false
                                            }
                                        )
                                    }
                                }
                            }*/
                            var textFieldWidth by remember { mutableStateOf(0) }

                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    textFieldWidth = coordinates.size.width
                                }
                            ) {
                                OutlinedTextField(
                                    value = fieldState.value.value,
                                    onValueChange = { newValue ->
                                        when (fieldConfig.inputType) {
                                            InputType.Number -> {
                                                if (newValue.matches(Regex("^[0-9,]*$"))) {
                                                    fieldState.value.value = newValue
                                                    fieldState.error.value = false
                                                }
                                            }
                                            else -> {
                                                fieldState.value.value = newValue
                                                fieldState.error.value = false
                                            }
                                        }
                                    },
                                    placeholder = {
                                        Text(
                                            when (fieldName) {
                                                "KMs Driven" -> "Enter kilometers driven"
                                                else -> "Enter $fieldName"
                                            }
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(enabled = fieldConfig.inputType == InputType.Dropdown) {
                                            if (fieldConfig.inputType == InputType.Dropdown) {
                                                fieldExpanded = true
                                            }
                                        },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = if (showFieldError) MaterialTheme.colorScheme.error else Color(0xFF1976D2),
                                        unfocusedBorderColor = if (showFieldError) MaterialTheme.colorScheme.error else Color.Gray,
                                        errorBorderColor = MaterialTheme.colorScheme.error
                                    ),
                                    isError = showFieldError,
                                    supportingText = {
                                        if (showFieldError) {
                                            Text(
                                                text = fieldState.errorMessage.value.ifEmpty { "This field is required" },
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = iconMapping[fieldName] ?: R.drawable.male_icon),
                                            contentDescription = fieldName,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    trailingIcon = {
                                        if (fieldConfig.inputType == InputType.Dropdown) {
                                            IconButton(onClick = { fieldExpanded = !fieldExpanded }) {
                                                Icon(
                                                    imageVector = if (fieldExpanded) Icons.Default.KeyboardArrowUp
                                                    else Icons.Default.ArrowDropDown,
                                                    contentDescription = if (fieldExpanded) "Collapse" else "Expand"
                                                )
                                            }
                                        }
                                    },
                                    readOnly = fieldConfig.inputType == InputType.Dropdown,
                                    keyboardOptions = when {
                                        fieldName == "Phone Number" -> KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Phone
                                        )
                                        fieldConfig.inputType == InputType.Number -> KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number
                                        )
                                        else -> KeyboardOptions.Default
                                    }
                                )

                                if (fieldConfig.inputType == InputType.Dropdown) {
                                    DropdownMenu(
                                        expanded = fieldExpanded,
                                        onDismissRequest = { fieldExpanded = false },
                                        modifier = Modifier
                                            .width(with(LocalDensity.current) { textFieldWidth.toDp() })
                                    ) {
                                        fieldConfig.options.forEach { item ->
                                            DropdownMenuItem(
                                                text = { Text(item) },
                                                onClick = {
                                                    fieldState.value.value = item
                                                    fieldExpanded = false
                                                    fieldState.error.value = false
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
}

@Composable
fun CompanyIconSelector(
    selectedCompany: String,
    onCompanySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val companies = listOf(
        "Toyota" to R.drawable.toyota,
        "Nissan" to R.drawable.honda,
        "Ford" to R.drawable.ford,
        "Honda" to R.drawable.honda,
        "Mercedes" to R.drawable.mercedes,
        "Hyundai" to R.drawable.toyota
    )

    Column(modifier = modifier) {
        Text(
            text = "Select Brand:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // First row
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            companies.take(3).forEach { (company, iconRes) ->
                BrandCard(
                    company = company,
                    iconRes = iconRes,
                    isSelected = selectedCompany.equals(company, ignoreCase = true),
                    onSelected = onCompanySelected,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Second row
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            companies.drop(3).take(3).forEach { (company, iconRes) ->
                BrandCard(
                    company = company,
                    iconRes = iconRes,
                    isSelected = selectedCompany.equals(company, ignoreCase = true),
                    onSelected = onCompanySelected,
                    modifier = Modifier.weight(1f).background(Color.Transparent)
                )
            }
        }
    }
}

@Composable
fun ImageSliderCard(
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
                        AddImage(selectedImages)
                    } else {
                        PageSlider(selectedImages, selectedImages)
                    }
                }
            }
        }
    }
}
@Composable
fun BrandCard(
    company: String,
    iconRes: Int,
    isSelected: Boolean,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable { onSelected(company) },
        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.Transparent,
//            contentColor = LocalContentColor.current
//        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) Color(0xFF1976D2) else Color.LightGray.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(Color.Transparent)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = company,
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = company,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = LocalContentColor.current
            )
        }
    }
}

@Composable
fun PageSlider(
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
fun AddImage(sliderList: MutableList<String>) {
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

fun prepareRequestBodySeparate(
    context: Context,
    selectedImages: List<String>,
    inputFields: Map<String, MutableState<String>>,
    description: String
): JSONObject {
    val jsonObject = JSONObject()

    // Define a mapping from UI label to API key
    val keyMapping = mapOf(
        "Car Name" to "car_name",
        "Company" to "company",
        "Paint Choices" to "paint_condition",
        "Car Model" to "year",
        "KMs Driven" to "milage",
        "Engine Capacity" to "engine_size",
        "Select Option Type" to "option_type",
        "Select Specification" to "specs"
    )

    inputFields.forEach { (label, valueState) ->
        val apiKey = keyMapping[label] ?: label.replace(" ", "_").toLowerCase()
        val value = valueState.value

        when (apiKey) {
//            "milage" -> {
//
//                val numericValue = value.filter { it.isDigit() }
//                jsonObject.put(apiKey, numericValue.toIntOrNull() ?: 0)
//            }
            "engine_capacity" -> {
                val numericValue = value.filter { it.isDigit() }
                jsonObject.put(apiKey, numericValue.toIntOrNull() ?: 0)
            }
//            "demand" -> {
//                val numericValue = value.filter { it.isDigit() }
//                jsonObject.put(apiKey, numericValue)
//            }
            else -> {
                jsonObject.put(apiKey, value)
            }
        }
    }
//    jsonObject.put("phone_number", "03001234567")
//    jsonObject.put("chassis_number", "ASD123")
//    jsonObject.put("registry_number", "BGF369")

//    jsonObject.put("description", description)

//    val base64Images = JSONArray()
//    for (uriString in selectedImages) {
//        val base64String = convertImageToBase64(context, Uri.parse(uriString))
//        base64Images.put(base64String)
//    }
//    jsonObject.put("photos", base64Images)

    return jsonObject
}

fun sendPostRequest(context: Context, requestBody: JSONObject,navController: NavController) {
    val client = OkHttpClient()

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val body = requestBody.toString().toRequestBody(mediaType)
    Log.d("REQUEST_PAYLOAD", requestBody.toString())

    val token = getToken(context)

    val requestBuilder = Request.Builder()
        .url(TestApi.Post_Add)
        .post(body)

    token?.let {
        requestBuilder.addHeader("Authorization", "Bearer $it")
    }

    val request = requestBuilder.build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("API_Call", "Failed to send request: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseString = response.body?.string()
                Log.d("API_Call", "Response: $responseString")
                try {
                    val jsonResponse = JSONObject(responseString)
                    val salerCarId = jsonResponse.optInt("saler_car_id", -1)
                    saveSalerCarId(context, salerCarId)

                    (context as? Activity)?.runOnUiThread {
                        navController.navigate("info")
                    }
                } catch (e: JSONException) {
                    Log.e("API_Call", "JSON parsing error: ${e.message}")
                }
            } else {
                Log.e("API_Call", "Error: ${response.code}")
            }
        }
    })
}

fun convertImageToBase64(context: Context, imageUri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(imageUri)
    val byteArray = inputStream?.readBytes()
    inputStream?.close()
    return if (byteArray != null) {
        Base64.encodeToString(byteArray, Base64.DEFAULT)
    } else {
        ""
    }
}



fun saveSalerCarId(context: Context, salerCarId: Int) {
    val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("saler_car_id", salerCarId).apply()
    Log.d("Saler id", "IDDD$salerCarId")
}

fun getSalerCarId(context: Context): Int {
    val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    return prefs.getInt("saler_car_id", 0)
}

@Composable
fun CustomAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dialog icon
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = Color(0xFFFFA000), // Amber color for warning
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dialog title
                Text(
                    text = "Confirm Action",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Dialog message
                Text(
                    text = "Are you sure you want to leave this ad posting?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Cancel button
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF1976D2)
                        ),
                        border = BorderStroke(1.dp, Color(0xFF1976D2))
                    ) {
                        Text("No")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Confirm button
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1976D2),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Yes")
                    }
                }
            }
        }
    }
}


@Composable
fun StepProgressIndicatorss(
    modifier: Modifier = Modifier,
    stepCount: Int,
    currentStep: Int,
    titles: List<String>,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(stepCount) { index ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable {  }
            ) {
                // Step circle
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(
                            color = if (index == 0) Color.Red
                            else Color.Gray, // Other steps are gray
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (index + 1).toString(),
                        color = Color.White, // Text stays white for all steps
                        fontSize = 14.sp
                    )
                }

                // Step title
                Text(
                    text = titles.getOrElse(index) { "Step ${index + 1}" },
                    color = Color.DarkGray,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Connector line
            if (index < stepCount - 1) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .offset(y = (-13).dp)
                        .width(90.dp)
                        .background(color = Color.Gray) // Keep both lines gray
                )
            }
        }
    }
}


@Composable
fun RadioButtonSelectionCard(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
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
                    "Who is selling?",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }

            if (expanded) {
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(cardColor)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        RadioButton(
                            selected = selectedOption == "Self",
                            onClick = { onOptionSelected("Self") }
                        )
                        Text("Self", modifier = Modifier.padding(start = 8.dp))

                        Spacer(modifier = Modifier.width(16.dp))

                        RadioButton(
                            selected = selectedOption == "Other",
                            onClick = { onOptionSelected("Other") }
                        )
                        Text("Other", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 309, heightDp = 675)
@Composable
fun SliderPreview() {
    val navController = rememberNavController()
    CarSellScreen(navController = navController)
}


// Define input types
enum class InputType {
    Text,
    Number,
    Phone,
    Dropdown,
    Password
}

// Data class for field configuration
data class DropdownConfig(
    val options: List<String>,
    val inputType: InputType,
    val isRequired: Boolean = false,
    val validation: ((String) -> Boolean)? = null
)

// Data class to hold field state
data class FieldState(
    val value: MutableState<String>,
    val error: MutableState<Boolean>,
    val errorMessage: MutableState<String>
)


//fun saveAndLogFormData(
//    selectedOption: String,
//    inputFields: Map<String, FieldState>,
//    context: Context
//) {
//
//    val formDataJson = buildJsonObject {
//        put("selectedOption", selectedOption)
//
//        inputFields.forEach { (fieldName, fieldState) ->
//            put(fieldName, fieldState.value.value)
//        }
//    }
//
//    val jsonString = Json { prettyPrint = true }.encodeToString(formDataJson)
//
//    Log.d("FORM_DATA", "Saved form data:\n$jsonString")
//
//    // Save to SharedPreferences
//    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
//    prefs.edit().putString("saved_form_data", jsonString).apply()
//
//    Toast.makeText(context, "Form data saved!", Toast.LENGTH_SHORT).show()
//}


/*fun saveAndLogFormData(
    inputFields: Map<String, FieldState>,
    context: Context,
    viewModel: UploadViewModel2,
    selectedImages: List<String>,
    onComplete: (CarData) -> Unit = {}
) {
    // Create base car data object with empty photos
    val carData = CarData(
        car_name = inputFields["Car Name"]?.value?.value ?: "",
        company = inputFields["Company"]?.value?.value ?: "",
        year = inputFields["Car Model"]?.value?.value ?: "",
        engine_size = inputFields["Engine Capacity"]?.value?.value ?: "",
        milage = inputFields["KMs Driven"]?.value?.value ?: "",
        option_type = inputFields["Select Option Type"]?.value?.value ?: "",
        paint_condition = inputFields["Paint Choices"]?.value?.value ?: "",
        specs = inputFields["Select Specification"]?.value?.value ?: "",
        photos = emptyList()
    )

    // Handle image upload if images exist
    if (selectedImages.isNotEmpty()) {
        viewModel.uploadImages(selectedImages) { uploadResults ->
            // Extract successful upload URLs
//            val photoUrls = uploadResults.mapNotNull { (_, response) ->
//                response?.get("url")?.takeIf { it.isJsonPrimitive }?.jsonPrimitive?.contentOrNull
//            }
            val photoUrls = uploadResults.mapNotNull { (_, response) ->
                (response as? JsonObject)?.get("url")?.let { urlElement ->
                    if (urlElement.isJsonPrimitive) urlElement.jsonPrimitive.contentOrNull else null
                }
            }

            // Create final car data with photos
            val finalData = carData.copy(photos = photoUrls)

            // Convert to JSON and process
            val json = Json.encodeToString(finalData)
            processFinalJson(json, context)

            // Return through callback
            onComplete(finalData)
        }
    } else {
        // No images - process with empty photos
        val json = Json.encodeToString(carData)
        processFinalJson(json, context)
        onComplete(carData)
    }
}
*/

private fun processFinalJson(json: String, context: Context) {
    Log.d("CAR_DATA", "Final JSON:\n$json")

    // Save to SharedPreferences
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    prefs.edit().putString("car_form_data", json).apply()

    Toast.makeText(context, "Car data saved!", Toast.LENGTH_SHORT).show()
}

//fun getSavedFormData(context: Context): String? {
//    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
//    return prefs.getString("saved_form_data", null)
//}
//
//// Usage example:
//val savedData = getSavedFormData(context)
//if (savedData != null) {
//    Log.d("SAVED_DATA", "Retrieved data: $savedData")
//}


@Composable
fun rememberSharedPreferences(): SharedPreferences {
    val context = LocalContext.current
    return remember { PreferenceManager.getDefaultSharedPreferences(context) }
}

fun saveFormDataToPrefs(prefs: SharedPreferences, jsonData: String) {
    prefs.edit().putString("saved_car_form_data", jsonData).apply()
}

fun getFormDataFromPrefs(prefs: SharedPreferences): String? {
    return prefs.getString("saved_car_form_data", null)
}

@Composable
fun RetrieveSavedDataScreen() {
    val prefs = rememberSharedPreferences()
    val savedData = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        savedData.value = getFormDataFromPrefs(prefs)
    }

    Column {
        if (savedData.value != null) {
            Text("Saved Data:")
            Text(savedData.value!!)
            // You can parse this JSON back to objects when needed
        } else {
            Text("No saved data found")
        }
    }
}

@Composable
fun Inspectiodseller(
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
                        Inspectieseller(selectedImages, cardName)
                    } else {
                        Inspectirseller(selectedImages, selectedImages, cardName)
                    }
                }
            }
        }
    }
}


@Composable
fun Inspectieseller(
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
            val fileName = "Gallery_${cardName.replace(" ", "_")}_App${System.currentTimeMillis()}.png"
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
                val fileName = "Camera_${cardName.replace(" ", "_")}_App${System.currentTimeMillis()}.png"
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

fun showImagePickerDialogs(
    context: Context,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
    onCamera: () -> Unit
) {
    android.app.AlertDialog.Builder(context).apply {
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

@Composable
fun Inspectirseller(
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
                            com.example.carapp.screens.Inspector.launchCamera(
                                context,
                                sliderList
                            ) { uri ->
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
                            val fileName = "Gallery_${cardName.replace(" ", "_")}_App${System.currentTimeMillis()}.png"
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
                                val fileName = "Camera_${cardName.replace(" ", "_")}_App_${System.currentTimeMillis()}.png"
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
                                com.example.carapp.screens.Inspector.launchCamera(
                                    context,
                                    sliderList
                                ) { uri ->
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
                                showImagePickerDialogs(context, launcherGallery, ::handleCameraClick)
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

