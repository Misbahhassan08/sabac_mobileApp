package com.example.carapp.screens.Dealer

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.carapp.R
import com.example.carapp.assets.redcolor
import com.example.carapp.models.AssignedSlot
import com.example.carapp.models.CarI
import com.example.carapp.models.CarListViewModelI
import com.example.carapp.models.DealerList
import com.example.carapp.models.InspectionModel
import com.example.carapp.models.Live
import com.example.carapp.models.Upcoming
import com.example.carapp.screens.Inspector.AddTimeSlotScreen
import com.example.carapp.screens.Inspector.ManualEntryScreen
import com.example.carapp.screens.Inspector.calculateRemainingTime
import com.example.carapp.screens.Inspector.checkIfReportExists
import com.example.carapp.screens.Inspector.decodeBase64ToBitma
import com.example.carapp.screens.extractImageUrl
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DealerListScreen(
    navController: NavController,
    viewModel: DealerList = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val upcomingCars by viewModel.cars.collectAsState()
    val liveCars by viewModel.assignedSlots.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Upcoming", "Live", "Inventory")
    var showExitDialog by remember { mutableStateOf(false) }
    // Back press handler
    BackHandler {
        showExitDialog = true
    }

    //

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text(
                    text = "Exit App?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Are you sure you want to exit the application?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        (context as? Activity)?.finish()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Yes, Exit")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showExitDialog = false },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.fetchCarList(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Dealer List", color = Color.White, fontWeight = FontWeight.SemiBold)
            },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = redcolor
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color(0xFFF84444)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                color = if (selectedTab == index) Color.Yellow else Color.White,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    when (selectedTab) {
                        0 -> CarList(upcomingCars)
                        1 -> LiveList(liveCars, navController)
                        2 -> Inventory()
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CarList(cars: List<Upcoming>) {
    LazyColumn {
        items(cars) { car ->
            CarItem(car)
        }
    }
}

/*@Composable
fun CarItem(car: Upcoming) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
//        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Car Name: ${car.carName}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Company: ${car.company}")
            Text(text = "Model: ${car.model}")
            Text(text = "Demand: ${car.demand}")
            Text(text = "City: ${car.city}")
            Text(text = "Status: ${if (car.isSold) "Sold" else "Available"}")
        }
    }
}*/

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CarItem(car: Upcoming, ) {
    val imageBitmap = remember { car.photos.firstOrNull()?.let { decodeBase64ToBitma(it) } }
    var showDialog by remember { mutableStateOf(false) }
    val carImageBitmaps = remember { mutableStateOf<List<Bitmap>>(emptyList()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(14.dp, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor =Color(0xFFEAECEE)),
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
        CarDetailsDialog(car = car, onDismiss = { showDialog = false })
    }
}

@Composable
fun CarDetailsDialog(car: Upcoming, onDismiss: () -> Unit) {
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

////////////////////// LIve Items ///////////////////////////
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LiveList(lives: List<Live>, navController: NavController) {
    LazyColumn {
        items(lives) { live ->
            LiveItem(live, navController)
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LiveItem(car: Live, navController: NavController) {
    val imageBitmap = remember { car.photos.firstOrNull()?.let { decodeBase64ToBitma(it) } }

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
//                    Text(
//                        text = "Report View",
//                        fontSize = 12.sp,
//                        color = Color.Gray
//                    )
                    Button(
                        onClick = {
                            navController.navigate("viewDealer/${car.salerCarId}")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFC107),
                            contentColor = Color.White
                        ),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "VIEW REPORT",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
*/

                Button(
                    onClick = {
                        navController.navigate("viewDealer/${car.salerCarId}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "ADD BID",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


//////////////// INVENTORY ///////////////////////////

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Inventory() {
    LazyColumn {

    }
}
