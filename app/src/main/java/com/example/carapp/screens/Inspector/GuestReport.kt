package com.example.carapp.screens.Inspector

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.carapp.models.ViewReportModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewReportScreen(
    salerCarId: String,
    navController: NavController,
    viewModel: ViewReportModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    Log.d("ViewReportScreen", "............... reported Screen ")
    val context = LocalContext.current
    val report by viewModel.report.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    Log.d("ViewReportScreen"," reported Screen $salerCarId")
    LaunchedEffect(salerCarId) {
        viewModel.fetchReport(salerCarId, context)
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                report?.let { reportData ->
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
//                            Column(modifier = Modifier.padding(16.dp)) {
//                                Text(
//                                    text = "Seller Information",
//                                    fontSize = 20.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    color = MaterialTheme.colorScheme.primary
//                                )
//                                Spacer(modifier = Modifier.height(8.dp))
//
//                                CarDetailRow("Name", "${reportData.sellerFirstName} ${reportData.sellerLastName}")
//                                CarDetailRow("Email", reportData.sellerEmail)
//                                CarDetailRow("Phone", reportData.sellerPhone)
//                            }
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

                        // Overall Score Indicator
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Overall Score",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))

//                                CircularProgressIndicator(
//                                    progress = reportData.overallScore / 100f,
//                                    color = Color.Green,
//                                    modifier = Modifier.size(80.dp),
//                                    strokeWidth = 8.dp
//                                )
//                                Spacer(modifier = Modifier.height(8.dp))
//                                Text(
//                                    text = "${reportData.overallScore} / 100",
//                                    fontSize = 18.sp,
//                                    fontWeight = FontWeight.Bold
//                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Car Information",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))

//                                CarDetailRow("Car Name", reportData.carName)
//                                CarDetailRow("Company", reportData.carCompany)
//                                CarDetailRow("Model", reportData.model)
//                                CarDetailRow("Color", reportData.color)
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
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Component Conditions",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))

//                                Column(modifier = Modifier.padding(16.dp)) {
//                                ConditionItem("Engine Condition", reportData.engineCondition)
//                                ConditionItem("Body Condition", reportData.bodyCondition)
//                                ConditionItem("Clutch Condition", reportData.clutchCondition)
//                                ConditionItem("Steering Condition", reportData.steeringCondition)
//                                ConditionItem("Suspension Condition", reportData.suspensionCondition)
//                                ConditionItem("Brakes Condition", reportData.brakesCondition)
//                                ConditionItem("AC Condition", reportData.acCondition)
//                                ConditionItem("Electrical Condition", reportData.electricalCondition)
////                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Estimated Price
                        Card(
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
//                                    text = "$${reportData.salerCarId}",
                                    text = "$",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
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
fun ConditionItem(label: String, value: Int) {
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
fun ConditionProgressBar(label: String, value: Int) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        LinearProgressIndicator(
            progress = value / 100f,
            modifier = Modifier.fillMaxWidth(),
            color = if (value > 50) Color.Green else Color.Red
        )
        Text(text = "$value / 100", fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CarDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold,color = Color.Gray)
    }
}

fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str.substringAfter(","), Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        Log.e("Image Decode", "Error decoding base64 image: ${e.message}")
        null
    }
}
