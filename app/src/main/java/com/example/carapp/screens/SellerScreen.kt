package com.example.carapp.screens

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.models.Car
import com.example.carapp.models.CarListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults.color
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.key
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.example.carapp.models.NotificationItem
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListScreen(
    navController: NavController,
    viewModel: CarListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val notifications by viewModel.notifications.collectAsState()
    val carList by viewModel.cars.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val systemUiController = rememberSystemUiController()

    systemUiController.isStatusBarVisible = false
    LaunchedEffect(isLoading) {
//        if (!isLoading) {
            while (true) {
                viewModel.fetchNotifications(context)
                delay(12000L)
            }
//        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchCarList(context)
    }

    LaunchedEffect(isLoading) {
        Log.d("Loader", "isLoading: $isLoading")
    }

    if (isLoading) {
        CustomAnimatedLoader(
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
                                redcolor
//                                brush = Brush.verticalGradient(
//                                    colors = listOf(
//                                        Color(0xFF0D47A1),
//                                        Color(0xFF0D47A1),
//                                        Color(0xFF42A5F5)
//                                    )
//                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(90.dp))

                            // Profile Image
                            Image(
                                painter = painterResource(id = R.drawable.car3),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
//                                    .border(2.dp, Color.White, CircleShape)
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

                                    DrawerItem(
                                        icon = Icons.Filled.Home,
                                        label = "Home",
                                        onClick = {})
                                    DrawerItem(
                                        icon = Icons.Filled.Person,
                                        label = "List",
                                        onClick = {})
                                    DrawerItem(
                                        icon = Icons.Filled.Favorite,
                                        label = "Bidding",
                                        onClick = {})
                                    DrawerItem(
                                        icon = Icons.Filled.ShoppingCart,
                                        label = "Store",
                                        onClick = {})
                                }

                                Column {
                                    Divider(
                                        color = Color.White,
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )

                                    DrawerItem(
                                        icon = Icons.Filled.ExitToApp,
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
                    TopAppBar(
                        title = {
                            Text(
                                text = "Reports History",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        },

                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
//                            .padding(8.dp, 16.dp)
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
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(text = "Listing", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.White)
                    }
                },
                floatingActionButton = {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        redcolor,
                                        redcolor,
                                        redcolor
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        ExtendedFloatingActionButton(
                            onClick = { navController.navigate("postcar") },
                            modifier = Modifier.padding(16.dp),
                            containerColor = redcolor,
                            shape = RoundedCornerShape(50),
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 6.dp,
                                pressedElevation = 12.dp
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = Color.White
                                )
                                Text(
                                    text = "Book Inspection",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CarListContent(carList, modifier = Modifier.zIndex(0f))

                    if (notifications.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .zIndex(1f)
                                .align(Alignment.TopCenter)
                                .padding(top = 16.dp)
                        ) {
                            notifications.forEach { notification ->
                                NotificationCard(
                                    notification = notification,
                                    onDismiss = { viewModel.removeNotification(notification) },
//                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun NotificationCard(notification: NotificationItem, onDismiss: () -> Unit) {
    var timeLeft by remember { mutableStateOf(10) }
    val context = LocalContext.current

    LaunchedEffect(notification) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft -= 1
        }
        onDismiss()
    }

    val progress by animateFloatAsState(
        targetValue = timeLeft / 10f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "progress"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = notification.message,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        sendBidid(notification.bid_id, context)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    )
                ) { Text("Accept") }

                Button(
                    onClick = {
                        RejectBid(notification.bid_id, context)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336),
                        contentColor = Color.White
                    )
                ) { Text("Reject") }
            }

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(4.dp),
                color = Color.Blue,
                trackColor = Color.LightGray
            )
        }
    }
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, onClick: () -> Unit) {
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


fun performLogout(navController: NavController, context: Context) {
    val accessToken = getToken(context)
    val refreshToken = getRefreshToken(context)

    if (accessToken == null || refreshToken == null) {
        Log.e("Logout", "Tokens are missing!")
        navController.navigate("login")
        return
    }

    val client = OkHttpClient()
    val url = TestApi.Get_Logout

    val jsonObject = JSONObject().apply {
        put("refresh_token", refreshToken)
    }

    val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .addHeader("Authorization", "Bearer $accessToken")
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            Log.d("LogoutAPI", "Response Code: ${response.code}")
            Log.d("LogoutAPI", "Response Body: $responseBody")

            withContext(Dispatchers.Main) {
                clearTokenData(context)
                navController.navigate("login")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("LogoutAPI", "Logout failed: ${e.message}")
            withContext(Dispatchers.Main) {
                navController.navigate("login")
            }
        }
    }
}


@Composable
fun CustomAnimatedLoader(
    modifier: Modifier = Modifier,
    dotColor: Color = redcolor,
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


@Composable
fun CarListContent(carList: List<Car>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {

        Text(
            text = "${carList.size} Results",
            modifier = Modifier.padding(8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(modifier = Modifier.padding(8.dp)) {
            items(carList) { car ->
                CarItem(car)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

/*
@Composable
fun CarItem(car: Car) {
//    val imageBitmap = remember { car.photos.firstOrNull()?.let { decodeBase64ToBitmap(it) } }
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(14.dp, shape = RoundedCornerShape(16.dp))
            .clickable { showDialog = true },
        colors = CardDefaults.cardColors(containerColor =Color(0xFFEAECEE)),
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp, 90.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(2.dp, Color.Red, RoundedCornerShape(10.dp))
            ) {
                val context = LocalContext.current
                car.photos?.forEach { rawPhoto ->
                    val imageUrl = extractImageUrl(rawPhoto)

                    if (!imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            placeholder = painterResource(R.drawable.car2),
                            error = painterResource(R.drawable.car2),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
*/
@Composable
fun CarItem(car: Car) {
    var showDialog by remember { mutableStateOf(false) }
    val carImageBitmaps = remember { mutableStateOf<List<Bitmap>>(emptyList()) }

    // Set the bitmap list when car.imageBitmaps is available
    LaunchedEffect(car.imageBitmaps) {
        Log.d("CarItem", "Setting imageBitmaps for car: ${car.name}, bitmap count: ${car.imageBitmaps.size}")
        carImageBitmaps.value = car.imageBitmaps
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(14.dp, shape = RoundedCornerShape(16.dp))
            .clickable { showDialog = true },
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
                    .border(2.dp, Color.Red, RoundedCornerShape(10.dp))
            ) {
                /*when {
                    car.imageBitmaps.isNotEmpty() -> {
                        Log.d("CarItem", "Bitmap found. Displaying image for car:")
                        Image(
                            bitmap = car.imageBitmaps.first().asImageBitmap(),
                            contentDescription = "Car Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    imageUrl != null -> {
                        Log.d("CarItem", "Bitmap is empty but URL is available. Waiting for bitmap to load.")
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    else -> {
                        Log.d("CarItem", "No image to show — bitmap and URL are both null.")
                        Text("No Image", modifier = Modifier.align(Alignment.Center))
                    }
                }
*/
                val bitmaps = carImageBitmaps.value

                when {
                    bitmaps.isNotEmpty() -> {
                        Log.d("CarItem", "Showing bitmap for car: ${car.name}")
                        val bitmap = bitmaps.first()
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Car Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    imageUrl != null -> {
                        Log.d("CarItem", "Bitmap not available yet for car: ${car.name}")
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    else -> {
                        Log.d("CarItem", "No image URL or bitmap available for car: ${car.name}")
                        Text("No Image", modifier = Modifier.align(Alignment.Center))
                    }
                }


                // Favorite Icon (Top Left)
//                Icon(
//                    imageVector = Icons.Default.Star,
//                    contentDescription = "Favorite",
//                    tint = Color.Red,
//                    modifier = Modifier
//                        .size(20.dp)
//                        .align(Alignment.TopStart)
//                        .padding(4.dp)
//                        .background(Color.White, shape = CircleShape)
//                        .padding(2.dp)
//                )

                // Image Count Overlay (Bottom Left)
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
//                    Text(text = car.photos.size.toString(), color = Color.White, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = car.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "PKR ${car.engineSize} lacs", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Year Icon
                    Image(
                        painter = painterResource(id = R.drawable.datetime_icon),
                        contentDescription = "Year",
                        modifier = Modifier.size(14.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = car.year, fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.width(12.dp))

                    // Mileage Icon
                    Image(
                        painter = painterResource(id = R.drawable.speed_icon),
                        contentDescription = "Mileage",
                        modifier = Modifier.size(14.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${car.mileage} km", fontSize = 12.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Fuel Type Icon
                    Image(
                        painter = painterResource(id = R.drawable.tag_icon),
                        contentDescription = "Fuel Type",
                        modifier = Modifier.size(14.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = car.optionType, fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.width(8.dp))

                    // City Icon
                    Image(
                        painter = painterResource(id = R.drawable.location_icon),
                        contentDescription = "City",
                        modifier = Modifier.size(14.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = car.company, fontSize = 12.sp, color = Color.Gray)
                }

            }

           /* Box(
                modifier = Modifier
                    .offset(y = (-35).dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF00C853), RoundedCornerShape(8.dp))
                        .padding(horizontal = 11.dp, vertical = 8.dp)
                ) {
                    Text(text = car.status, color = Color.White, fontSize = 12.sp)
                }*/
            Box(
                modifier = Modifier
                    .offset(y = (-35).dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(getStatusColor(car.status), RoundedCornerShape(8.dp))
                        .padding(horizontal = 11.dp, vertical = 8.dp)
                ) {
                    Text(text = car.status, color = Color.White, fontSize = 12.sp)
                }
            }

        }
    }
    if (showDialog) {
        CarDetailsDialog(car, onDismiss = { showDialog = false })
    }
}

fun extractImageUrl(photo: String): String? {
    return if (photo.trim().startsWith("{") && photo.contains("image_url")) {
        try {
            val cleaned = photo.replace("'", "\"")
            val jsonObject = JSONObject(cleaned)
            jsonObject.getString("image_url")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else {
        photo
    }
}
fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "pending" -> Color(0xFFFFA000) // Orange
        "assigned" -> Color(0xFF1976D2) // Blue
        "bidding" -> Color(0xFF7B1FA2) // Purple
        "inspection" -> Color(0xFF00796B) // Teal
        else -> Color(0xFF00C853) // Default green (or handle other cases)
    }
}

@Composable
fun CarDetailsDialog(car: Car, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = car.name, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                if (!car.photos.isNullOrEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(car.photos!!) { photoUrl ->
                            AsyncImage(
                                model = photoUrl,
                                contentDescription = "Car Photo",
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.car2),
                        contentDescription = "Placeholder Car Image",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(2.dp, Color.Gray, RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                // Show images if available
//                if (car.photos.isNotEmpty()) {
//                    LazyRow(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        items(car.photos) { photo ->
//                            val imageBitmap = decodeBase64ToBitmap(photo)
//                            if (imageBitmap != null) {
//                                Image(
//                                    bitmap = imageBitmap.asImageBitmap(),
//                                    contentDescription = "Car Image",
//                                    modifier = Modifier
//                                        .size(200.dp)
//                                        .clip(RoundedCornerShape(12.dp)),
////                                        .border(2.dp, Color.Gray, RoundedCornerShape(12.dp)),
//                                    contentScale = ContentScale.Crop
//                                )
//                            }
//                        }
//                    }
//                } else {
//                    Image(
//                        painter = painterResource(id = R.drawable.car2), // Placeholder
//                        contentDescription = "Placeholder Car Image",
//                        modifier = Modifier
//                            .size(200.dp)
//                            .clip(RoundedCornerShape(12.dp))
//                            .border(2.dp, Color.Gray, RoundedCornerShape(12.dp)),
//                        contentScale = ContentScale.Crop
//                    )
//                }

                Spacer(modifier = Modifier.height(10.dp))

                // Car details
                Text(text = "Price: PKR ${car.company} lacs", fontSize = 14.sp)
                Text(text = "Model: ${car.year}", fontSize = 14.sp)
                Text(text = "Mileage: ${car.mileage} km", fontSize = 14.sp)
                Text(text = "Fuel Type: ${car.paint_condition}", fontSize = 14.sp)
                Text(text = "Location: ${car.specs}", fontSize = 14.sp)
                Text(text = "Status: ${car.status}", fontSize = 14.sp, color = Color.Green)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Close")
            }
        }
    )
}

fun sendBidid(bid_id: String, context: Context) {
    val url = "${TestApi.accept_bid}$bid_id/"
    val token = getToken(context)

    val client = OkHttpClient()
    val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
    val requestBody = """{ "bid_id": "$bid_id" }""".toRequestBody(mediaType)

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .addHeader("Authorization", "Bearer $token")
        .addHeader("Content-Type", "application/json")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            (context as? Activity)?.runOnUiThread {
                Toast.makeText(context, "Failed to accept bid", Toast.LENGTH_SHORT).show()
                Log.d("bid", "failed to accept bid")
            }
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                (context as? Activity)?.runOnUiThread {
                    Toast.makeText(context, "Bid accepted!", Toast.LENGTH_SHORT).show()
                    Log.d("bid", "to accept bid")

                }
            } else {
                (context as? Activity)?.runOnUiThread {
                    Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    })
}
fun RejectBid(bid_id: String, context: Context) {
    val url = "${TestApi.reject_bid}$bid_id/"
    val token = getToken(context)

    val client = OkHttpClient()
    val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
    val requestBody = """{ "bid_id": "$bid_id" }""".toRequestBody(mediaType)

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .addHeader("Authorization", "Bearer $token")
        .addHeader("Content-Type", "application/json")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            (context as? Activity)?.runOnUiThread {
                Toast.makeText(context, "Failed to reject bid", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                Log.d("resp", "REJECT  $response")
                (context as? Activity)?.runOnUiThread {
                    Toast.makeText(context, "Bid Rejected!", Toast.LENGTH_SHORT).show()
                }
            } else {
                (context as? Activity)?.runOnUiThread {
                    Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    })
}

fun decodeBase64ToBitmap(base64String: String): Bitmap? {
    return try {
        val pureBase64 = base64String.substringAfterLast("base64,")

        val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@Preview(showBackground = true, widthDp = 309, heightDp = 675)
@Composable
fun SellerPreview() {
}
