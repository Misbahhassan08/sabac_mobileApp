package com.example.carapp.screens.Admin

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.models.Live
import com.example.carapp.screens.Inspector.decodeBase64ToBitma
import com.example.carapp.screens.extractImageUrl
import com.example.carapp.screens.getToken
import com.example.carapp.screens.redcolor
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.IOException

/*
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpcomingList(modifier: Modifier = Modifier, navController: NavController) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Upcoming Cars",
            modifier = Modifier.padding(8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
*/
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpcomingList(
    cars: List<com.example.carapp.screens.Admin.Upcoming>,
    modifier: Modifier = Modifier, navController: NavController) {
    LazyColumn {
        items(cars) { car ->
            CarItemA(car)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CarItemA(car: com.example.carapp.screens.Admin.Upcoming, ) {
    val imageBitmap = remember { car.photos.firstOrNull()?.let { decodeBase64ToBitma(it) } }
    var showDialog by remember { mutableStateOf(false) }
    val carImageBitmaps = remember { mutableStateOf<List<Bitmap>>(emptyList()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(14.dp, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAECEE)),
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val firstPhoto = car.photos?.firstOrNull()
            val imageUrl = firstPhoto?.let { extractImageUrl(it) }

            Log.d("CarItem", "Raw photo: $firstPhoto")
            Log.d("CarItem", "Extracted image URL: $imageUrl")

            Box(
                modifier = Modifier
                    .size(120.dp, 90.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(2.dp, Color.Transparent, RoundedCornerShape(10.dp))
            ) {
                val bitmaps = carImageBitmaps.value

                when {
                    bitmaps.isNotEmpty() -> {
                        Log.d("CarItem", "Showing bitmap for car: ${car.carName}")
                        val bitmap = bitmaps.first()
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Car Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    imageUrl != null -> {
                        Log.d("CarItem", "Bitmap not available yet for car: ${car.carName}")
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    else -> {
                        Log.d("CarItem", "No image URL or bitmap available for car: ${car.carName}")
                        Text("No Image", modifier = Modifier.align(Alignment.Center))
                    }
                }
//                if (imageBitmap != null) {
//                    Image(
//                        bitmap = imageBitmap.asImageBitmap(),
//                        contentDescription = "Car Image",
//                        modifier = Modifier.fillMaxSize(),
//                        contentScale = ContentScale.Crop
//                    )
//                } else {
//                    Image(
//                        painter = painterResource(id = R.drawable.car2),
//                        contentDescription = "Placeholder Car Image",
//                        modifier = Modifier.fillMaxSize(),
//                        contentScale = ContentScale.Crop
//                    )
//                }
//
//                Box(
//                    modifier = Modifier
//                        .align(Alignment.TopEnd)
//                        .background(Color.Red, RoundedCornerShape(6.dp))
//                        .padding(horizontal = 6.dp, vertical = 2.dp)
//                ) {
//                    Text(
//                        text = "By Seller",
//                        color = Color.White,
//                        fontSize = 10.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Images",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = car.photos.size.toString(), color = Color.White, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = car.carName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
//                Text(text = "PKR ${car.carName} lacs", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                Text(text = "PKR 1000 lacs", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    car.secondaryPhoneNumber?.let { Text(text = it, fontSize = 12.sp, color = Color.Gray) }
//                    Text(text = "car.phoneNumber", fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(text = car.secondaryNumber, fontSize = 12.sp, color = Color.Gray)
                    Text(text = "car.secondaryNumber", fontSize = 12.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = car.carName, fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Time Slot",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )

                }

//                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        showDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = redcolor),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = "Details", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

            }
        }
    }
    if (showDialog) {
        CarDetailsDialogA(car = car, onDismiss = { showDialog = false })
    }
}

@Composable
fun CarDetailsDialogA(car: com.example.carapp.screens.Admin.Upcoming, onDismiss: () -> Unit) {
    val imageBitmap = remember { car.photos.firstOrNull()?.let { decodeBase64ToBitma(it) } }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${car.carName} (${car.year})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Divider()

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Condition: ${car.paintCondition}", fontWeight = FontWeight.Bold)
                Text(text = "Color: ${car.paintCondition}", fontWeight = FontWeight.Bold)
                Text(text = "Fuel Type: ${car.specs}", fontWeight = FontWeight.Bold)
//                Text(text = "Demand: PKR ${car.demand}", fontWeight = FontWeight.Bold)
//                Text(text = "Description: ${car.description}", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(12.dp))
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap.asImageBitmap(),
                        contentDescription = "Car Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                else {
                    Text(text = "No images available", fontStyle = FontStyle.Italic, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = "Close", color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}

///////// ************************ Await Approval cars ///////////////////////////////

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Awaiting(
    lives: List<com.example.carapp.screens.Admin.Live>, navController: NavController,
    modifier: Modifier = Modifier) {
    LazyColumn {
        Log.d("test","here")
        items(lives) { live ->
            LiveItemA(live, navController)
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LiveItemA(car: com.example.carapp.screens.Admin.Live, navController: NavController) {
    val imageBitmap = remember { car.photos.firstOrNull()?.let { decodeBase64ToBitma(it) } }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isAccepted by remember { mutableStateOf(false) }

    Log.d("test","here2")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
//                    .background(Color.LightGray)
            ) {
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap.asImageBitmap(),
                        contentDescription = "Car Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.car2),
                        contentDescription = "Placeholder Car Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "${car.carName} ${car.company}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = car.year,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Demand",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "${car.salerCarId} PKR",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1B5E20)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                /*Column {
                    Text(
                        text = "Overall Score",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFC107), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${car.salerCarId}/100",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }*/
                Column {
                    Button(
                        onClick = {
//                            navController.navigate("viewDealer/${car.salerCarId}")
                            navController.navigate("viewReportAdmin/${car.salerCarId}")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFC107),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "VIEW REPORT",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


//                Button(
//                    onClick = {
//                        navController.navigate("viewDealer/${car.salerCarId}")
//                    },
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
//                    shape = RoundedCornerShape(16.dp)
//                ) {
//                    Text(
//                        text = "ADD BID",
//                        color = Color.White,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
            }
        }
    }
}



///////// ************************ LIVE CARS ///////////////////////////////
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LiveList(
//    lives: List<Live>,
    navController: NavController,
    modifier: Modifier = Modifier) {
//    LazyColumn {
        Log.d("test","here")
//        items() { live ->
            LiveItemL(navController)
//        }
//    }
}



@SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LiveItemL(navController: NavController) {
    val context = LocalContext.current
    val viewModel: LiveViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val token = remember { getToken(context) }
    val client = remember { OkHttpClient() }

    LaunchedEffect(Unit) {
        viewModel.fetchLiveCars(token)
    }
    val carList by viewModel.assignedSlots.collectAsState()
    LazyColumn {
        items(carList) { car ->
            CarCard(car = car)
        }
    }

}

@Composable
fun CarCard(car: Live){

    val imageBitmap = remember { car.photos.firstOrNull()?.let { decodeBase64ToBitma(it) } }
    Log.d("test","here8")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
//                    .background(Color.LightGray)
            ) {
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap.asImageBitmap(),
                        contentDescription = "Car Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.car2),
                        contentDescription = "Placeholder Car Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "${car.carName} ${car.company}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = car.year,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Demand",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "${car.salerCarId} PKR",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1B5E20)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                /*Column {
                    Text(
                        text = "Overall Score",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFC107), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${car.salerCarId}/100",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }*/

                /*
                     Column {
                         Button(
                             onClick = {
     //                            navController.navigate("viewDealer/${car.salerCarId}")
                                 navController.navigate("viewReports/${car.salerCarId}")
                             },
                             colors = ButtonDefaults.buttonColors(
                                 containerColor = Color(0xFFFFC107),
                                 contentColor = Color.White
                             ),
                             shape = RoundedCornerShape(16.dp)
                         ) {
                             Text(
                                 text = "VIEW REPORT",
                                 fontSize = 14.sp,
                                 fontWeight = FontWeight.Bold
                             )
                         }

                         Spacer(modifier = Modifier.height(8.dp))

                         Row(
                             horizontalArrangement = Arrangement.spacedBy(8.dp)
                         ) {
                             Button(
                                 onClick = {
                                     // Handle accept action
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
                             }
                         }
                     }

     */
//                Button(
//                    onClick = {
//                        navController.navigate("viewDealer/${car.salerCarId}")
//                    },
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
//                    shape = RoundedCornerShape(16.dp)
//                ) {
//                    Text(
//                        text = "ADD BID",
//                        color = Color.White,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
            }
        }
    }
}

/////////////////// ************************** ////////////////////////////////