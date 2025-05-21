package com.example.carapp.screens.Inspector



import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.assets.redcolor
import com.example.carapp.models.AssignedSlot
import com.example.carapp.models.CarI
import com.example.carapp.models.CarListViewModelI
import com.example.carapp.models.InspectionModel
import com.example.carapp.screens.extractImageUrl
import com.example.carapp.screens.getToken
import com.example.carapp.screens.performLogout
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InspectorListScree(
    navController: NavController,
    viewModel: CarListViewModelI = androidx.lifecycle.viewmodel.compose.viewModel(),
    inspectionViewModel: InspectionModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Open)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val carList by viewModel.cars.collectAsState()
    val assignedSlots by viewModel.assignedSlots.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val systemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = false

    var selectedScreen by remember { mutableStateOf("Home") }
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

    LaunchedEffect(isLoading) {
        Log.d("Loader", "isLoading: $isLoading")
    }

    if (isLoading) {
        CustomAnimatedLoade(
            modifier = Modifier.fillMaxSize(),
//            color = Color.Magenta,
//            strokeWidth = 8.dp,
            radius = 40.dp
        )
    } else {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .width(260.dp)
                        .background(Color.Transparent),
                    drawerContainerColor = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        redcolor,
                                        redcolor,
                                        redcolor
                                    )
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(90.dp))

                            Image(
                                painter = painterResource(id = R.drawable.car3),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "SABAC",
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Spacer(modifier = Modifier.height(20.dp))

                                    // Navigation Items
                                    /*DrawerIteP(
                                        icon = painterResource(id = R.drawable.dea),
                                        label = "Home",
                                        onClick = {
                                            selectedScreen = "Home"
                                            scope.launch { drawerState.close() }
                                        }
                                    )*/
                                    DrawerIte(
                                        icon = Icons.Filled.Home,
                                        label = "Home",
                                        onClick = {
                                            selectedScreen = "Home"
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                    DrawerIteP(
                                        icon = painterResource(id = R.drawable.dea),
                                        label = "Manual Entry",
                                        onClick = {
                                            selectedScreen = "ManualEntry"
                                            scope.launch { drawerState.close() }
                                        }
                                    )

                                    DrawerIteP(
                                        icon = painterResource(id = R.drawable.dea),
                                        label = "Schedule",
                                        onClick = {
                                            selectedScreen = "AddTimeSlot"
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                }

                                Column {
                                    Divider(
                                        color = Color.White,
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                    DrawerIteP(
                                        icon = painterResource(id = R.drawable.log),
                                        label = "Logout",
                                        onClick = { performLogout(navController, context) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        redcolor,
                                        redcolor,
                                        redcolor,
                                    )
                                )
                            )
                    ) {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = "INSPECTOR",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    }
                },
            ) { paddingValues ->
                when (selectedScreen) {
                    "Home" -> CarListConte(
                        carList,
                        assignedSlots = assignedSlots,
                        Modifier.padding(paddingValues),
                        navController,
                        inspectionViewModel
                    )
                    "ManualEntry" -> ManualEntryScreen(navController)
                    "AddTimeSlot" -> AddTimeSlotScreen(navController)
                }
            }
        }
    }
}




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CarListConte(carList: List<CarI>, assignedSlots: List<AssignedSlot>, modifier: Modifier = Modifier, navController: NavController, inspectionViewModel: InspectionModel) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Listed Cars",
            modifier = Modifier.padding(8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(modifier = Modifier.padding(8.dp)) {
            items(carList) { car ->
                CarIte(car, navController)
                Spacer(modifier = Modifier.height(24.dp))
            }

            items(assignedSlots) { slot ->
                AssignedSlotItem(slot, navController)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
// INSPECTOR ADDED CARS SHOW HERE
fun AssignedSlotItem(slot: AssignedSlot, navController: NavController) {
    var expanded by remember { mutableStateOf(true) }
    val imageBitmap = remember { slot.car.photos.firstOrNull()?.let { decodeBase64ToBitma(it) } }
    val context = LocalContext.current

    val targetDateTime = remember {
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            LocalDateTime.parse("${slot.date} ${slot.timeSlot}", formatter)
        } catch (e: Exception) {
            Log.e("ERROR", "Failed to parse date and time: ${e.message}")
            null
        }
    }

    val isInspected = slot.car.isInspected
    val buttonText = if (isInspected) "View Report" else "Start Inspection"
    val buttonColor = if (isInspected) Color.Blue else Color.Red

    val remainingTime = remember { mutableStateOf(calculateRemainingTime(targetDateTime)) }

    LaunchedEffect(targetDateTime) {
        while (targetDateTime != null && remainingTime.value.totalSeconds > 0) {
            delay(1000L)
            remainingTime.value = calculateRemainingTime(targetDateTime)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(14.dp, shape = RoundedCornerShape(16.dp))
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAECEE)),
    ) {
        Column {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp, 90.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(2.dp, Color.Green, RoundedCornerShape(10.dp))
                ) {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap.asImageBitmap(),
                            contentDescription = "Car Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.car2),
                            contentDescription = "Placeholder Car Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(Color.Green, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "By Inspector",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = slot.car.carName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "Inspector: ${slot.inspector}", fontSize = 14.sp, color = Color.Black)

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Phone",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        slot.car.guest?.let { Text(text = it.number, fontSize = 12.sp, color = Color.Gray) }

                        Spacer(modifier = Modifier.width(12.dp))

                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        slot.car.guest?.let { Text(text = it.email, fontSize = 12.sp, color = Color.Gray) }
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
                        Text(text = slot.date, fontSize = 12.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.width(12.dp))

                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Time Slot",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = slot.timeSlot, fontSize = 12.sp, color = Color.Gray)
                    }

                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        modifier = Modifier.align(Alignment.End),
                        tint = Color.Gray
                    )
                }
            }
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Divider(color = Color.LightGray, thickness = 1.dp)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Time remaining display
                    if (remainingTime.value.totalSeconds > 0) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFFE3F2FD),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "TIME REMAINING",
                                color = Color(0xFF0D47A1),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Days
                                TimeUnitBox(
                                    value = remainingTime.value.days,
                                    unit = "D",
                                    color = Color(0xFF1565C0)
                                )

                                // Hours
                                TimeUnitBox(
                                    value = remainingTime.value.hours,
                                    unit = "H",
                                    color = Color(0xFF1976D2)
                                )

                                // Minutes
                                TimeUnitBox(
                                    value = remainingTime.value.minutes,
                                    unit = "M",
                                    color = Color(0xFF1E88E5)
                                )

                                // Seconds
                                TimeUnitBox(
                                    value = remainingTime.value.seconds,
                                    unit = "S",
                                    color = Color(0xFF2196F3)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Action button
                    if (remainingTime.value.totalSeconds <= 0) {
                        Button(
                            onClick = {
                                if (!isInspected) {
                                    updateCarStatu(
                                        context,
                                        slot.car.salerCarId.toString(),
                                        "in_inspection",
                                        onSuccess = {
                                            Handler(Looper.getMainLooper()).post {
                                                navController.navigate("report/${slot.car.salerCarId}")
                                            }
                                        },
                                        onFailure = { errorMessage ->
                                            Log.e("API_ERROR", "Failed to update status: $errorMessage")
                                        }
                                    )
                                } else {
                                    navController.navigate("viewReport/${slot.car.salerCarId}")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Text(
                                text = buttonText,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
// SELLER CAR SHOWS HERE
fun CarIte(car: CarI, navController: NavController) {
    // State for expanded/collapsed
    var expanded by remember { mutableStateOf(true) }
//    val imageBitmap = remember { car.Photos.firstOrNull()?.let { decodeBase64ToBitma(it) } }
    var reportExists by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val carImageBitmaps = remember { mutableStateOf<List<Bitmap>>(emptyList()) }

    // Set the bitmap list when car.imageBitmaps is available
    LaunchedEffect(car.imageBitmaps) {
        Log.d("CarItem", "Setting imageBitmaps for car: ${car.carName}, bitmap count: ${car.imageBitmaps.size}")
        carImageBitmaps.value = car.imageBitmaps
    }

    LaunchedEffect(car.carId) {
        reportExists = checkIfReportExists(car.carId.toString(), context)
    }

    // Time calculation
    val targetDateTime = remember {
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            LocalDateTime.parse("${car.inspectionDate} ${car.inspectionTime}", formatter)
        } catch (e: Exception) {
            Log.e("ERROR", "Failed to parse date and time: ${e.message}")
            null
        }
    }

    val remainingTime = remember { mutableStateOf(calculateRemainingTime(targetDateTime)) }

    LaunchedEffect(targetDateTime) {
        while (targetDateTime != null && remainingTime.value.totalSeconds > 0) {
            delay(1000L)
            remainingTime.value = calculateRemainingTime(targetDateTime)
        }
    }

    val isInspected = car.isInspected
    val buttonText = if (isInspected) "View Report" else "Start Inspection"
    val buttonColor = if (isInspected) Color.Blue else Color.Red

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(14.dp, shape = RoundedCornerShape(16.dp))
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAECEE)),
    ) {
        Column {
            // Always visible content
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val firstPhoto = car.photos?.firstOrNull()
                val imageUrl = firstPhoto?.let { extractImageUrl(it) }

                Log.d("CarItem", "Raw photo: $firstPhoto")
                Log.d("CarItem", "Extracted image URL: $imageUrl")
                // Image section
                Box(
                    modifier = Modifier
                        .size(120.dp, 90.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(2.dp, Color.Red, RoundedCornerShape(10.dp))
                ) {
                   /* if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap.asImageBitmap(),
                            contentDescription = "Car Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.car2),
                            contentDescription = "Placeholder Car Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }*/
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

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(Color.Red, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "By Seller",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

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
//                        Text(text = car.Photos.size.toString(), color = Color.White, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Basic info
                    Text(text = car.carName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
//                    Text(text = "PKR ${car.} lacs", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)

                    Spacer(modifier = Modifier.height(4.dp))

                    // Contact info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Phone",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        car.primaryPhoneNumber?.let { Text(text = it, fontSize = 12.sp, color = Color.Gray) }

                        Spacer(modifier = Modifier.width(12.dp))

                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = car.carName, fontSize = 12.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Date and time
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Date",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = car.inspectionDate, fontSize = 12.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.width(12.dp))

                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Time Slot",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = car.inspectionTime, fontSize = 12.sp, color = Color.Gray)
                    }

                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        modifier = Modifier.align(Alignment.End),
                        tint = Color.Gray
                    )
                }
            }

            // Expanded content
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Divider(color = Color.LightGray, thickness = 1.dp)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Time remaining display
                    if (remainingTime.value.totalSeconds > 0) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFFE3F2FD),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "TIME REMAINING",
                                color = Color(0xFF0D47A1),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Days
                                TimeUnitBox(
                                    value = remainingTime.value.days,
                                    unit = "D",
                                    color = Color(0xFF1565C0)
                                )

                                // Hours
                                TimeUnitBox(
                                    value = remainingTime.value.hours,
                                    unit = "H",
                                    color = Color(0xFF1976D2)
                                )

                                // Minutes
                                TimeUnitBox(
                                    value = remainingTime.value.minutes,
                                    unit = "M",
                                    color = Color(0xFF1E88E5)
                                )

                                // Seconds
                                TimeUnitBox(
                                    value = remainingTime.value.seconds,
                                    unit = "S",
                                    color = Color(0xFF2196F3)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Action button

                    if (remainingTime.value.totalSeconds <= 0) {
                        Button(
                            onClick = {
                                if (!isInspected) {
                                    updateCarStatus(context, car.carId.toString(), "in_inspection",
                                        onSuccess = {
                                            Handler(Looper.getMainLooper()).post {
                                                navController.navigate("report/${car.carId}")
                                                Log.d("status", "Status Updated")
                                            }
                                        },
                                        onFailure = { errorMessage ->
                                            Log.e(
                                                "API_ERROR",
                                                "Failed to update status: $errorMessage"
                                            )
                                        }
                                    )
                                } else {
                                    navController.navigate("viewReports/${car.carId}")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Text(
                                text = buttonText,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun TimeUnitBox(value: Long, unit: String, color: Color) {
    /*Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                color = color,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Text(
            text = value.toString().padStart(2, '0'),
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = unit,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 10.sp
        )
    }*/
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .background(
                color = color.copy(alpha = 0.2f),
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = color,
                shape = CircleShape
            )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value.toString().padStart(2, '0'),
                color = color,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = unit,
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

fun updateCarStatus(context: Context, carId: String, status: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
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

fun updateCarStatu(context: Context, carId: String, status: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
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


fun checkIfReportExists(carId: String, context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("inspection_reports", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("report_$carId", false)
}

fun decodeBase64ToBitma(base64String: String): Bitmap? {
    return try {
        val pureBase64 = base64String.substringAfterLast("base64,")

        val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

data class TimeRemaining(val days: Long, val hours: Long, val minutes: Long, val seconds: Long) {
    val totalSeconds: Long get() = days * 86400 + hours * 3600 + minutes * 60 + seconds

    fun format(): String {
        return "%02d Days %02dh:%02dm:%02ds".format(days, hours, minutes, seconds)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateRemainingTime(targetDateTime: LocalDateTime?): TimeRemaining {
    val now = LocalDateTime.now()

    Log.d("DEBUG", "Current Time: $now")
    Log.d("DEBUG", "Target Time: $targetDateTime")

    return if (targetDateTime != null) {
        val duration = Duration.between(now, targetDateTime)

        Log.d("DEBUG", "Duration (Seconds): ${duration.seconds}")

        if (!duration.isNegative) {
            val days = duration.toDays()
            val hours = duration.toHours() % 24
            val minutes = duration.toMinutes() % 60
            val seconds = duration.seconds % 60

            Log.d("DEBUG", "Remaining Time -> Days: $days, Hours: $hours, Min: $minutes, Sec: $seconds")

            TimeRemaining(days, hours, minutes, seconds)
        } else {
            Log.d("DEBUG", "Time Expired!")
            TimeRemaining(0, 0, 0, 0)
        }
    } else {
        Log.d("DEBUG", "Invalid Target Time!")
        TimeRemaining(0, 0, 0, 0)
    }
}

@Composable
fun DrawerIte(
//    icon: ImageVector,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = Color.White)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = label, fontSize = 16.sp, color = Color.White)
    }
}
@Composable
fun DrawerIteP(
//    icon: ImageVector,
    icon: Painter,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, color = Color.White, fontSize = 16.sp)
    }
}

fun performLogot(navController: NavController) {
    println("User Logged Out")
    navController.navigate("login")
}

@Composable
fun CustomAnimatedLoade(
    modifier: Modifier = Modifier,
    dotColor: Color = Color.Red,
    dotCount: Int = 12,
    radius: Dp = 24.dp,
    spacingFactor: Float = 1.3f
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing)
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(radius * 2)
            .background(Color.Transparent)
    ) {
        Canvas(modifier = Modifier.size(radius * 2)) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val adjustedRadius = radius.toPx() * spacingFactor
            val circleRadius = size.minDimension / 10  // Size of the dots
            val animationOffset = 30f  // Controls the phase shift of dots

            for (i in 0 until dotCount) {
                val angleRad = Math.toRadians((i * (360f / dotCount) + angle).toDouble())
                val dotX = centerX + adjustedRadius * cos(angleRad).toFloat()
                val dotY = centerY + adjustedRadius * sin(angleRad).toFloat()

                val alpha = ((i + animationOffset) % dotCount) / dotCount.toFloat()

                drawCircle(
                    color = dotColor.copy(alpha = alpha),
                    radius = circleRadius,
                    center = Offset(dotX, dotY)
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 309, heightDp = 675)
@Composable
fun ShowInspection() {
//    InspectorScreen()
}