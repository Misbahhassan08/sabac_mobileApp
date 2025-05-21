package com.example.carapp.screens.Admin

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.carapp.Apis.TestApi
import com.example.carapp.assets.redcolor
import com.example.carapp.models.ViewReportModel
import com.example.carapp.screens.Inspector.CarDetailRow
import com.example.carapp.screens.Inspector.CustomAnimatedLoade
import com.example.carapp.screens.getToken
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminReport(
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
                report?.let { reportData ->
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
                                            .clip(
                                                RoundedCornerShape(
                                                    topStart = 8.dp,
                                                    topEnd = 8.dp
                                                )
                                            )
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
                                            .clip(
                                                RoundedCornerShape(
                                                    topStart = 8.dp,
                                                    topEnd = 8.dp
                                                )
                                            )
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
                                        CarDetailRow(
                                            "Condition",
                                            inspectionData.techSpecs.condition
                                        )
                                        CarDetailRow(
                                            "Engine Capacity",
                                            inspectionData.techSpecs.engineCapacity
                                        )
                                        CarDetailRow("Fuel Type", inspectionData.techSpecs.fuelType)
                                        CarDetailRow(
                                            "KMs Driven",
                                            inspectionData.techSpecs.kmsDriven
                                        )
                                        CarDetailRow("Variant", inspectionData.techSpecs.variant)
//                                    ConditionItem("AC Condition", reportData.acCondition)
//                                    ConditionItem("Electrical Condition", reportData.electricalCondition)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Body parts Inspection
                            report?.jsonObj?.bodyParts?.let { bodyParts ->
                                BodyPartsInspectionCardA(bodyParts)
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

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val coroutineScope = rememberCoroutineScope()

                                /* Button(
                                     onClick = {
                                         coroutineScope.launch {
                                             val client = OkHttpClient()
                                             val token = getToken(context)
                                             val url = "${TestApi.post_accept}${car.salerCarId}"

                                             val request = Request.Builder()
                                                 .url(url)
                                                 .post(RequestBody.create(null, ByteArray(0)))
                                                 .apply {
                                                     token.let { addHeader("Authorization", "Bearer $it") }
                                                 }
                                                 .build()

                                             client.newCall(request).enqueue(object : Callback {
                                                 override fun onFailure(call: Call, e: IOException) {
                                                     Log.e("AcceptApi", "POST Failed: ${e.message}")
                                                 }

                                                 override fun onResponse(call: Call, response: Response) {
                                                     if (response.isSuccessful) {
                                                         Log.d("AcceptApi", "Accepted Successfully")
                                                         isAccepted = true
                                                     } else {
                                                         Log.e("AcceptApi", "POST Failed: ${response.code}")
                                                     }
                                                 }
                                             })
                                         }
                                     },
                                     colors = ButtonDefaults.buttonColors(
                                         containerColor = Color(0xFF4CAF50),
                                         contentColor = Color.White
                                     ),
                                     shape = RoundedCornerShape(16.dp),
                                     modifier = Modifier.weight(1f)
                                 ) {
                                     Text(text = "ACCEPT", fontSize = 14.sp)
                                 }*/
                                if (report!!.isAccepted == true) {
                                    Text(
                                        text = "Accepted",
                                        color = Color(0xFF4CAF50),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                } else if (report!!.isRejected == true) {
                                    Text(
                                        text = "Rejected",
                                        color = Color(0xFFF44336),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                } else {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                coroutineScope.launch {
                                                    val client = OkHttpClient()
                                                    val token = getToken(context)
                                                    val url = "${TestApi.post_accept}${report!!.id}"
                                                    Log.d("AcceptApi", "POST URL: $url")

                                                    val request = Request.Builder()
                                                        .url(url)
                                                        .post(
                                                            RequestBody.create(
                                                                null,
                                                                ByteArray(0)
                                                            )
                                                        )
                                                        .apply {
                                                            token.let {
                                                                addHeader(
                                                                    "Authorization",
                                                                    "Bearer $it"
                                                                )
                                                            }
                                                        }
                                                        .build()

                                                    client.newCall(request)
                                                        .enqueue(object : Callback {
                                                            override fun onFailure(
                                                                call: Call,
                                                                e: IOException
                                                            ) {
                                                                Log.e(
                                                                    "AcceptApi",
                                                                    "POST Failed: ${e.message}"
                                                                )
                                                            }

                                                            override fun onResponse(
                                                                call: Call,
                                                                response: Response
                                                            ) {
                                                                if (response.isSuccessful) {
                                                                    Log.d(
                                                                        "AcceptApi",
                                                                        "Accepted Successfully"
                                                                    )
                                                                    Handler(Looper.getMainLooper()).post {
                                                                        navController.navigate("admin")
                                                                    }
                                                                } else {
                                                                    Log.e(
                                                                        "AcceptApi",
                                                                        "POST Failed: ${response.code}"
                                                                    )
                                                                }
                                                            }
                                                        })
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF4CAF50),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(16.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(text = "ACCEPT", fontSize = 14.sp)
                                        }

                                        Button(
                                            onClick = {
                                                coroutineScope.launch {
                                                    val client = OkHttpClient()
                                                    val token = getToken(context)
                                                    val url = "${TestApi.Post_reject}${report!!.id}"
                                                    Log.d("AcceptApi", "POST URL: $url")

                                                    val request = Request.Builder()
                                                        .url(url)
                                                        .post(
                                                            RequestBody.create(
                                                                null,
                                                                ByteArray(0)
                                                            )
                                                        )
                                                        .apply {
                                                            token.let {
                                                                addHeader(
                                                                    "Authorization",
                                                                    "Bearer $it"
                                                                )
                                                            }
                                                        }
                                                        .build()

                                                    client.newCall(request)
                                                        .enqueue(object : Callback {
                                                            override fun onFailure(
                                                                call: Call,
                                                                e: IOException
                                                            ) {
                                                                Log.e(
                                                                    "AcceptApi",
                                                                    "POST Failed: ${e.message}"
                                                                )
                                                            }

                                                            override fun onResponse(
                                                                call: Call,
                                                                response: Response
                                                            ) {
                                                                if (response.isSuccessful) {
                                                                    Log.d(
                                                                        "AcceptApi",
                                                                        "Accepted Successfully"
                                                                    )
                                                                    Handler(Looper.getMainLooper()).post {
                                                                        navController.navigate("admin")
                                                                    }
                                                                } else {
                                                                    Log.e(
                                                                        "AcceptApi",
                                                                        "POST Failed: ${response.code}"
                                                                    )
                                                                }
                                                            }
                                                        })
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFF44336),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(16.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(text = "REJECT", fontSize = 14.sp)
                                        }
                                    }
                                }


                                /*Button(
                                    onClick = {
                                        // Handle reject action
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF44336),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = "REJECT", fontSize = 14.sp)
                                }*/
                            }
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
}




@Composable
fun BodyPartsInspectionCardA(bodyParts: Map<String, Map<String, Map<String, Int>>>) {
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
