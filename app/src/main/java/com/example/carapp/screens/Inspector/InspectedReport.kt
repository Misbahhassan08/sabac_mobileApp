package com.example.carapp.screens.Inspector

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.carapp.R
import com.example.carapp.models.CarListViewModelI
import com.example.carapp.models.InspectionModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape

import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign

import com.example.carapp.models.ViewReportModel
import com.example.carapp.screens.redcolor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch


/*
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewReportScreen(
    carId: String,
    navController: NavController,
    viewModel: ViewReportModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val context = LocalContext.current
    val report by viewModel.report.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(carId) {
        viewModel.fetchReport(carId, context)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Inspection Report") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                report?.let { reportData ->
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Car Name: ${reportData.carName}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Company: ${reportData.carCompany}", fontSize = 16.sp)
                        Text(text = "Model: ${reportData.model}", fontSize = 16.sp)
                        Text(text = "Color: ${reportData.color}", fontSize = 16.sp)
                        Text(text = "Condition: ${reportData.condition}", fontSize = 16.sp)
                        Text(text = "Fuel Type: ${reportData.fuelType}", fontSize = 16.sp)
                        Text(text = "Engine Capacity: ${reportData.engineCapacity} cc", fontSize = 16.sp)
                        Text(text = "Mileage: ${reportData.mileage} km", fontSize = 16.sp)
                        Text(text = "Chassis Number: ${reportData.chassisNumber}", fontSize = 16.sp)
                        Text(text = "Inspection Date: ${reportData.inspectionDate}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Blue)

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyRow {
//                            items(reportData.car_photos) { photoBase64 ->
//                                decodeBase64ToBitma(photoBase64)?.let {
//                                    Image(
//                                        bitmap = it.asImageBitmap(),
//                                        contentDescription = "Car Photo",
//                                        modifier = Modifier
//                                            .size(100.dp)
//                                            .padding(4.dp)
//                                            .clip(RoundedCornerShape(8.dp))
//                                            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp)),
//                                        contentScale = ContentScale.Crop
//                                    )
//                                }
//                            }
                        }
                    }
                } ?: Text("No report found.", modifier = Modifier.align(Alignment.Center), fontSize = 18.sp)
            }
        }
    }
}
*/

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewReportScree(
    salerCarId: String,
    navController: NavController,
    viewModel: ViewReportModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val context = LocalContext.current
    val report by viewModel.report.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
Log.d("ViewReportScreen"," reported Screen $salerCarId")
    LaunchedEffect(salerCarId) {
        viewModel.fetchReport(salerCarId, context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inspection Report", color = Color.White, fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = redcolor),
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
        ) {
            if (isLoading) {
                CustomAnimatedLoade(
                    modifier = Modifier.fillMaxSize(),
                    radius = 40.dp
                )
            } else {
//                report?.let { reportData ->
                report?.jsonObj?.let { inspectionData ->
                    Column(
                        modifier = Modifier
//                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(0.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = redcolor)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = inspectionData.basicInfo.carName,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                CarDetailRow("${inspectionData.basicInfo.company}", "")
//                                CarDetailRow("Name", "${inspectionData.basicInfo.carName} ${inspectionData.basicInfo}")
//                                CarDetailRow("Email", inspectionData.createdAt)
//                                CarDetailRow("Phone", inspectionData.inspectorId.toString())
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Car Photos
//                        if (reportData.car_photos.isNotEmpty()) {
//                            LazyRow(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.spacedBy(8.dp)
//                            ) {
//                                items(reportData.car_photos) { photoBase64 ->
//                                    decodeBase64ToBitmap(photoBase64)?.let {
//                                        Image(
//                                            bitmap = it.asImageBitmap(),
//                                            contentDescription = "Car Photo",
//                                            modifier = Modifier
//                                                .size(120.dp)
//                                                .clip(RoundedCornerShape(12.dp))
//                                                .border(2.dp, Color.Gray, RoundedCornerShape(12.dp)),
//                                            contentScale = ContentScale.Crop
//                                        )
//                                    }
//                                }
//                            }
//                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                        .background(Color(0xFF007ACC))
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "Car Information",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))

                                CarDetailRow("Car Name", inspectionData.basicInfo.carName)
                                CarDetailRow("Company", inspectionData.basicInfo.company)
                                CarDetailRow("Model", inspectionData.basicInfo.carModel)
                                CarDetailRow("Color", inspectionData.basicInfo.bodyColor)
//                                CarDetailRow("Condition", reportData.condition)
//                                CarDetailRow("Fuel Type", reportData.fuelType)
//                                CarDetailRow("Engine Capacity", "${reportData.engineCapacity} cc")
//                                CarDetailRow("Mileage", "${reportData.mileage} km")
//                                CarDetailRow("Chassis No.", reportData.chassisNumber)
//                                CarDetailRow("Inspection Date", reportData.inspectionDate)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF007ACC))
                                        .padding(12.dp)
                                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                ) {
                                    Text(
                                        text = "Component Conditions",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))

                                Column(modifier = Modifier.padding(16.dp)) {
                                    CarDetailRow("Assembly", inspectionData.techSpecs.assembly)
                                    CarDetailRow("Condition", inspectionData.techSpecs.condition)
                                    CarDetailRow("Engine Capacity", inspectionData.techSpecs.engineCapacity)
                                    CarDetailRow("Fuel Type", inspectionData.techSpecs.fuelType)
                                    CarDetailRow("KMs Driven", inspectionData.techSpecs.kmsDriven)
                                    CarDetailRow("Variant", inspectionData.techSpecs.variant)
//                                    ConditionItem("AC Condition", reportData.acCondition)
//                                    ConditionItem("Electrical Condition", reportData.electricalCondition)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Body parts Inspection
                        report?.jsonObj?.bodyParts?.let { bodyParts ->
                            BodyPartsInspectionCard(bodyParts)
                        }

                        /*Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Body Parts Inspection",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Column(modifier = Modifier.padding(16.dp)) {
                                    report?.jsonObj?.bodyParts?.let { bodyParts ->
                                        BodyPartsInspectionCard(bodyParts)
                                    }
                                }
                            }
                        }*/

                        Spacer(modifier = Modifier.height(16.dp))

                        // Estimated Price
                        /*Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFD1E8E2)) // Light Green Background
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Estimated Price",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00796B) // Dark Green
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
//                                    text = "$${reportData.inspectorId}",
                                    text = "$",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }*/
                    }
                } ?: Text(
                    "No report found.",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}




@Composable
fun BodyPartsInspectionCard(bodyParts: Map<String, Map<String, Map<String, Int>>>) {
    Log.d("UI", "Body_Parts...$bodyParts")
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF007ACC))
                    .padding(12.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            ) {
                Text(
                    text = "Body Parts Inspection",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))


            bodyParts.forEach { (sectionName, parts) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1A91DA))
                        .padding(10.dp)
                ) {
                    Text(
                        text = sectionName,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                /*parts.forEach { (partName, damages) ->

                    val nonZeroDamages = damages.filterValues { it != 0 }
                    if (nonZeroDamages.isNotEmpty()) {
                        Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFC6CCD1))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = partName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                        nonZeroDamages.forEach { (damageType, count) ->
                            Text(
                                text = "$damageType",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 10.dp, top = 2.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }*/
                parts.forEach { (partName, damages) ->
                    val nonZeroDamages = damages.filterValues { it != 0 }
                    if (nonZeroDamages.isNotEmpty()) {
                        // Part Name Section
                        Column(modifier = Modifier.padding(bottom = 8.dp)) {
                            // Part Name Header
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFC6CCD1))
                            ) {
                                Text(
                                    text = partName.replaceFirstChar { it.uppercase() },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            // Damage Types Table
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = Color.LightGray,
                                        shape = RectangleShape
                                    )
                                    .padding(top = 8.dp)
                            ) {
                                nonZeroDamages.entries.toList().forEachIndexed { index, (damageType, _) ->
                                    // Split damage type into parts
                                    val parts = damageType.split('-')
                                    val category = parts.first().replaceFirstChar { it.uppercase() }
                                    val type = parts.drop(1).joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 10.dp)
                                            .then(
                                                if (index < nonZeroDamages.size - 1) {
                                                    Modifier.drawBehind {
                                                        drawLine(
                                                            color = Color.LightGray.copy(alpha = 0.5f),
                                                            start = Offset(0f, size.height),
                                                            end = Offset(size.width, size.height),
                                                            strokeWidth = 1.dp.toPx()
                                                        )
                                                    }
                                                } else {
                                                    Modifier
                                                }
                                            ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = category,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.width(100.dp)
                                        )
                                        Text(
                                            text = type,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            modifier = Modifier.padding(start = 50.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}




/*
@Composable
fun BodyPartsInspectionCard(bodyParts: Map<String, Map<String, Map<String, Int>>>) {
    Log.d("UI", "Body_Parts...$bodyParts")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Title bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF007ACC))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Body Parts Inspection",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            bodyParts.forEach { (sectionName, parts) ->
                // Section Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1A91DA))
                        .padding(10.dp)
                ) {
                    Text(
                        text = sectionName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                parts.forEach { (partName, damages) ->
                    // Part Header (gray background)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE0E0E0))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = partName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }

                    damages.forEach { (damageType, count) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = damageType,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (count == 0) "Not specified" else count.toString(),
                                fontStyle = if (count == 0) FontStyle.Italic else FontStyle.Normal,
                                color = if (count == 0) Color.Gray else Color.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

*/

/*
@Composable
fun BodyPartsInspectionCard(bodyParts: Map<String, Map<String, Map<String, Int>>>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Top Title Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF007ACC)) // Blue color
                    .padding(12.dp)
            ) {
                Text(
                    text = "Body Parts Inspection",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            bodyParts.forEach { (sectionName, parts) ->
                // Section Title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1A91DA))
                        .padding(10.dp)
                ) {
                    Text(
                        text = sectionName,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(7.dp))
                parts.forEach { (partName, damages) ->
                    // Part Title Background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFC6CCD1))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = partName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Damage Types
                    listOf("Paint", "Seals", "Dents").forEach { key ->
                        val matching = damages.filterKeys { it.contains(key, ignoreCase = true) }
                        val detailText = if (matching.isEmpty()) {
                            "Not specified"
                        } else {
                            matching.entries.joinToString(", ") { (type, _) ->
                                type.substringAfter("-").trim()
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = key,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Text(
                                text = detailText,
                                fontStyle = if (detailText == "Not specified") FontStyle.Italic else FontStyle.Normal,
                                color = if (detailText == "Not specified") Color.Gray else Color.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
*/


@Composable
fun ConditionIte(label: String, value: Int) {
    val progressColor = when {
        value > 75 -> Color(0xFF4CAF50) // Green
        value > 50 -> Color(0xFFFFEB3B) // Yellow
        else -> Color(0xFFF44336) // Red
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular Indicator
            Box(
                modifier = Modifier.size(50.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = value / 100f,
                    color = progressColor,
                    modifier = Modifier.size(50.dp),
                    strokeWidth = 6.dp
                )
                Text(
                    text = "$value%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Condition Label and Linear Progress
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = value / 100f,
                    color = progressColor,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}




@Composable
fun CarDetailRo(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

fun decodeBase64ToBitm(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str.substringAfter(","), Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        Log.e("Image Decode", "Error decoding base64 image: ${e.message}")
        null
    }
}
