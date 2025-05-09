package com.example.carapp.screens.Inspector

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.carapp.R
import com.example.carapp.screens.redcolor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.internal.connection.RouteSelector

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostTestCase(navController: NavController) {

    var selections = remember { mutableStateMapOf<String, Map<String, String>>() }
    val partImages = remember { mutableStateMapOf<String, MutableList<String>>() }

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
        "Price (PKR)" to listOf(""),
        "Location" to listOf("Karachi", "Lahore", "Islamabad", "Rawalpindi", "Faisalabad"),
        "Registered in" to listOf("Punjab", "Sindh", "KPK", "Balochistan"),
        "Phone Number" to listOf("")
    )

    val inputFields = remember {
        dropdownOptions.keys.associateWith { mutableStateOf("") }
    }

    var carDetailsExpanded by remember { mutableStateOf(false) }
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
                        BodyPartsInspectionCardTest(
                            onSelectionsChanged = { updatedSelections ->
                                selections.clear()
                                selections.putAll(updatedSelections)
                            },
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
            Button(
                onClick = {
//                    val carDetails = inputFields.mapValues { it.value.value }
//                    val bodyPartsInspection = selections
//                    val images = partImages
//                    inspectionReport.value = InspectionReport(
//                        carDetails = carDetails,
//                        bodyPartsInspection = bodyPartsInspection,
//                        images = images
//                    )
//                    val jsonReport = Json.encodeToString(inspectionReport.value)
                    Log.d("mib", "&&&&&&   $selections")
                    val carDetails = inputFields.mapValues { it.value.value }
                    val bodyPartsInspection = selections.toMap() // Convert to immutable map
                    val images = partImages.mapValues { it.value.toList() }.toMap() // Convert to immutable map

                    val inspectionReport = InspectionReport(
                        carDetails = carDetails,
                        bodyPartsInspection = bodyPartsInspection,
                        images = images
                    )

                    val jsonReport = Json.encodeToString(inspectionReport)
                    Log.d("INSPECTION_REPORT", jsonReport)
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
}


@SuppressLint("RememberReturnType")
@Composable
fun BodyPartsInspectionCardTest(
    modifier: Modifier = Modifier,
    onSelectionsChanged: (Map<String, Map<String, String>>) -> Unit = {},
    onImagesChanged: (Map<String, List<String>>) -> Unit = {},
) {
    val carInspectionData = remember { CarInspectionData.carInspectionData }

    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogOptions by remember { mutableStateOf<Map<String, Map<String, Int>>>(emptyMap()) }
    val selectionss = remember { mutableStateMapOf<String, Map<String, String>>() }
    var bodyPartsExpanded by remember { mutableStateOf(false) }
    val selectedOptions = remember { mutableStateMapOf<String, MutableMap<String, String>>() }

    var currentPartName by remember { mutableStateOf("") }
    val partImages = remember { mutableStateMapOf<String, MutableList<String>>() }


    LaunchedEffect(selectionss) {
        Log.d("mib","*****$selectionss.toMap()")
        onSelectionsChanged(selectionss.toMap())
    }

    LaunchedEffect(partImages) {
        onImagesChanged(partImages.toMap())
    }
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
            confirmButton = {
                Button(onClick = {
                    val currentConditions = dialogOptions.mapValues { (category, _) ->
                        selectedOptions[currentPartName]?.get(category) ?: ""
                    }
                    Log.d("DIALOG_SAVE", "Saving selections for $currentPartName: $currentConditions")
                    selectionss[currentPartName] = currentConditions
                    showDialog = false
                    onSelectionsChanged(selectionss.toMap())
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



@Serializable
data class InspectionReport(
    val carDetails: Map<String, String>,
    val bodyPartsInspection: Map<String, Map<String, String>>,
    val images: Map<String, List<String>>
)