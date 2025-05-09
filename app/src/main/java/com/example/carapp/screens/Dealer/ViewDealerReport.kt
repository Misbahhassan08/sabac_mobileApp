package com.example.carapp.screens.Dealer

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.models.ViewReportModel
import com.example.carapp.screens.cardColor
import com.example.carapp.screens.getToken
import com.example.carapp.screens.redcolor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/*@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewDealerReports(
//    carId: String,
    navController: NavController,
//    viewModel: ViewReportModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val context = LocalContext.current
//    val reportList by viewModel.reportList // <-- updated to reportList
//    val isLoading by viewModel.isLoading.collectAsState()
    var selectedBid by remember { mutableStateOf(5000) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf<Int?>(null) }
    val transition = updateTransition(selectedImage != null, label = "imageTransition")
    val dialogAlpha by transition.animateFloat(label = "alpha") { if (it) 1f else 0f }
    val dialogScale by transition.animateFloat(label = "scale") { if (it) 1f else 0.8f }
    val pagerState = rememberPagerState()
    var selectedImageIndex by remember { mutableStateOf<Int?>(null) }

    val carImages = listOf(
        R.drawable.car2,
        R.drawable.car3,
        R.drawable.car2,
        R.drawable.car3,
        R.drawable.car2,

    )


//    LaunchedEffect(carId) {
//        viewModel.fetchReport(carId, context) // fetching
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inspection Report", color = Color.White, fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = redcolor),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
//            if (isLoading) {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//            } else {
//                if (reportList.isNotEmpty()) { // <-- now checking if list is not empty
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
//                        reportList.forEach { reportData -> // <-- loop through list

                            BiddingSection(
                                askingPrice = "500000",
                                maxBid = 99999,
                                selectedBid = selectedBid,
                                onBidClick = { showDialog = true }
                            )

                            if (showDialog) {
                                BidNowDialog(
                                    onDismiss = { showDialog = false },
                                    onBidSubmit = { bid -> selectedBid = bid },
//                                    "carId".toInt()
                                )
                            }*/
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewDealerReports(
    carId: String,
    navController: NavController,
    viewModel: ViewBidModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val context = LocalContext.current
    var selectedImageIndex by remember { mutableStateOf<Int?>(null) }
    val pagerState = rememberPagerState()
    val transition = updateTransition(selectedImageIndex != null, label = "imageTransition")
    val dialogAlpha by transition.animateFloat(label = "alpha") { if (it) 1f else 0f }
    val dialogScale by transition.animateFloat(label = "scale") { if (it) 1f else 0.8f }
    var selectedImage by remember { mutableStateOf<Int?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    var bottomSheetContent by remember { mutableStateOf("None") }
    val report by viewModel.report.collectAsState()

        LaunchedEffect(carId) {
            viewModel.fetchReport(carId, context)
        }
    val carModel = "${report?.company ?: ""} ${report?.carName ?: ""} ${report?.carModel ?: ""}- ${report?.fuelType ?: ""} ${report?.engineCapacity ?: ""}"
    val condition = "Condition:"
    val condi = report?.condition ?: "Unknown"
    val carImages = listOf(
        R.drawable.car2,
        R.drawable.car3,
        R.drawable.car2,
        R.drawable.car3,
        R.drawable.car2,
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inspection Report", color = Color.White, fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = redcolor),
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .height(110.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .height(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF46AE3D))
                            .clickable {
                                bottomSheetContent = "asking"
                                showSheet = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Asking Price", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("PKR:500000", color = Color.White)
                        }
                    }

                    // Highest Bid Box
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .height(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0XFFFF6363))
                            .clickable {
                                bottomSheetContent = "highest"
                                showSheet = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Highest Bid", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("PKR:200000", color = Color.White)
                        }
                    }

                    // Place Your Bid Box
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .height(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEC232B))
                            .clickable {
                                bottomSheetContent = "place"
                                showSheet = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Place Your", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text("Bid", color = Color.White, fontWeight = FontWeight.Bold,fontSize = 20.sp)
                        }
                    }
                    if (showSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showSheet = false },
                            sheetState = sheetState
                        ) {
                            when (bottomSheetContent) {
                                "asking" -> {
                                    Column(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("Asking Price", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("The asking price for this vehicle is PKR:500000.")
                                    }
                                }

                                "highest" -> {
                                    Column(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("Highest Bid", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("The current highest bid is PKR:200000.")
                                    }
                                }

                                "place" -> {
                                    var bidAmount by remember { mutableStateOf("1000") }
                                    val numericBidAmount = bidAmount.toIntOrNull() ?: 1000

                                    Column(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("Place Your Bid", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Enter your bid below in PKR:")
                                        Spacer(modifier = Modifier.height(12.dp))

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .height(50.dp)
                                                .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                                        ) {
                                            // Minus Button
                                            Box(
                                                modifier = Modifier
                                                    .background(Color.Red, shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                                                    .width(50.dp)
                                                    .fillMaxHeight()
                                                    .clickable {
                                                        val current = numericBidAmount
                                                        if (current >= 1000) {
                                                            bidAmount = (current - 1000).toString()
                                                        }
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("-", fontSize = 24.sp, color = Color.White)
                                            }

                                            // TextField for manual input
                                            TextField(
                                                value = bidAmount,
                                                onValueChange = { newValue ->
                                                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                                        bidAmount = newValue
                                                    }
                                                },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .fillMaxHeight()
                                                    .background(Color.White),
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                textStyle = LocalTextStyle.current.copy(
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Center
                                                ),
                                                colors = TextFieldDefaults.colors(
                                                    focusedContainerColor = Color.White,
                                                    unfocusedContainerColor = Color.White,
                                                    disabledContainerColor = Color.White,
                                                    focusedIndicatorColor = Color.Transparent,
                                                    unfocusedIndicatorColor = Color.Transparent,
                                                    disabledIndicatorColor = Color.Transparent
                                                ),
                                                singleLine = true
                                            )

                                            // Plus Button
                                            Box(
                                                modifier = Modifier
                                                    .background(Color(0xFF46AE3D), shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                                                    .width(50.dp)
                                                    .fillMaxHeight()
                                                    .clickable {
                                                        val current = numericBidAmount
                                                        bidAmount = (current + 1000).toString()
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("+", fontSize = 24.sp, color = Color.White)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(22.dp))

                                        /*Button(
                                            onClick = {
                                                showSheet = false
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEC232B)),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(50.dp)
                                        ) {
                                            Text("Submit Bid", fontSize = 16.sp, color = Color.White)
                                        }*/
                                        Button(
                                            onClick = {
                                                val finalBid = bidAmount
                                                if (finalBid.isNotEmpty() && finalBid.toIntOrNull() != null && finalBid.toInt() > 0) {
                                                    sendBidToServer(context, carId, finalBid) { success ->
                                                        if (success) {
                                                            showSheet = false
                                                        }
                                                    }
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEC232B)),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(50.dp)
                                        ) {
                                            Text("Submit Bid", fontSize = 16.sp, color = Color.White)
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Car Model Name
                Text(
                    text = carModel,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 20.dp)
                )
                Row {
                    Text(
                        text = condition,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        //                        modifier = Modifier.padding(top = 20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = condi,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        //                        modifier = Modifier.padding(top = 20.dp),
                        color = Color.Green
                    )
                }

                // Image Slider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(top = 32.dp)
                    //                        .background(cardColor)
                ) {
                    HorizontalPager(
                        count = carImages.size,
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        Image(
                            painter = painterResource(id = carImages[page]),
                            contentDescription = "Car Photo ${page + 1}",
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { selectedImageIndex = page },
                            contentScale = ContentScale.Fit
                        )
                    }

                    // Page indicators
                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(top = 36.dp),
                        activeColor = redcolor,
                        inactiveColor = Color.LightGray
                    )
                }

                // Full-screen image viewer
                selectedImageIndex?.let { index ->
                    Dialog(
                        onDismissRequest = { selectedImageIndex = null },
                        properties = DialogProperties(usePlatformDefaultWidth = false)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = dialogAlpha * 0.95f))
                                .clickable { selectedImageIndex = null },
                            contentAlignment = Alignment.Center
                        ) {
                            ZoomableImage(
                                painter = painterResource(id = carImages[index]),
                                contentDescription = "Enlarged Car Photo",
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .aspectRatio(1f)
                                    .graphicsLayer {
                                        scaleX = dialogScale
                                        scaleY = dialogScale
                                    }
                            )

                            // Close button
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(16.dp)
                                    .align(Alignment.TopEnd)
                                    .clickable { selectedImageIndex = null },
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Milage
                    InfoCard(
                        icon = R.drawable.milage,
                        title = "Mileage",
                        subtitle = report?.kmsDriven ?: "",
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Card 2 -Color
                    InfoCard(
                        icon = R.drawable.paint,
                        title = "Color",
                        subtitle = report?.bodyColor ?: "",
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Card 3 - Body Condition
                    InfoCard(
                        icon = R.drawable.condition,
                        title = "Body",
                        subtitle = report?.condition ?: "",
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Card 4 - Paint condition
                    InfoCard(
                        icon = R.drawable.paintcondition,
                        title = "Paint",
                        subtitle = "Good",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))



                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD1E8E2))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Your Bid",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00796B)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
//                                        text = "$${reportData.id}",
                            text = "PKR:5000000",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
@Composable
fun ZoomableImage(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    minScale: Float = 1f,
    maxScale: Float = 5f
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(minScale, maxScale)
        offset += offsetChange
    }

    Box(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (scale == 1f) {
                            scale = 3f
                        } else {
                            scale = 1f
                            offset = Offset.Zero
                        }
                    }
                )
            }
            .transformable(state = state)
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun InfoCard(
    @DrawableRes icon: Int,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .heightIn(min = 100.dp) // Set minimum height
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min), // Constrain height to content
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(32.dp),
//                colorFilter = ColorFilter.tint(redcolor)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Title with fixed lines
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 1, // Ensure single line
                overflow = TextOverflow.Ellipsis
            )

            // Subtitle with fixed lines
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 1, // Ensure single line
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}



@Composable
fun BidNowDialog(
    onDismiss: () -> Unit,
    onBidSubmit: (Int) -> Unit,
    carId: Int
) {
    var selectedBid by remember { mutableStateOf("") }  // TextField value
    var selectedRadioBid by remember { mutableStateOf<Int?>(null) } // Tracks radio selection

    val bidOptions = listOf(50000, 60000, 70000, 80000, 90000)
    val context = LocalContext.current
    var showSuccessDialog by remember { mutableStateOf(false) }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Success") },
            text = { Text("Bid successfully posted!") },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    onDismiss()
                }) {
                    Text("OK")
                }
            }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Your Bid",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                bidOptions.forEach { bid ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (selectedRadioBid == bid),
                                onClick = {
                                    selectedRadioBid = bid
                                    selectedBid = bid.toString()
                                }
                            )
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedRadioBid == bid),
                            onClick = {
                                selectedRadioBid = bid
                                selectedBid = bid.toString()
                            }
                        )
                        Text(
                            text = bid.toString(),
                            modifier = Modifier.padding(start = 8.dp),
                            fontSize = 16.sp
                        )
                    }
                }

                OutlinedTextField(
                    value = selectedBid,
                    onValueChange = {
                        selectedBid = it
                        selectedRadioBid = null
                    },
                    label = { Text("Enter Custom Bid") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val finalBid = selectedBid
                            if (finalBid != null && finalBid > 0.toString()) {
                                sendBidToServer(context, carId.toString(), finalBid) { success ->
                                    if (success) {
                                        showSuccessDialog = true
                                    }
                                }
                            }
                    }) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}



/*
@Composable
fun BiddingSection(askingPrice: String, maxBid: Int, selectedBid: Int, onBidClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = redcolor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .border(BorderStroke(1.dp, Color.White), shape = RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Asking Price", fontWeight = FontWeight.Bold, color = redcolor)
                    Text(text = askingPrice.toString(), color = redcolor)
                }
            }
            Box(
                modifier = Modifier
                    .border(BorderStroke(1.dp, Color.White), shape = RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Max Bid", fontWeight = FontWeight.Bold, color = redcolor)
                    Text(text = maxBid.toString(), color = redcolor)
                }
            }

            Box(
                modifier = Modifier
                    .border(BorderStroke(1.dp, Color.White), shape = RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { onBidClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Bid Now", fontWeight = FontWeight.Bold, color = redcolor)
//                        Text(text = selectedBid.toString(), color = Color.Blue)
                    }
                }
            }
        }
    }
}
*/
@Composable
fun BiddingSection(
    askingPrice: String,
    maxBid: Int,
    selectedBid: Int,
    onBidClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val elevation = 8.dp
    val cornerRadius = 16.dp
    val innerPadding = 16.dp
    val innerCornerRadius = 12.dp
    val borderWidth = 1.5.dp

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.cardElevation(elevation),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE53935), // Slightly darker red
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bidding Status",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Asking Price Box
                InfoBox(
                    title = "ASKING PRICE",
                    value = askingPrice,
                    backgroundColor = Color.White.copy(alpha = 0.9f),
                    textColor = Color(0xFFE53935),
                    borderColor = Color.White,
                    borderWidth = borderWidth,
                    cornerRadius = innerCornerRadius
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Max Bid Box
                InfoBox(
                    title = "HIGHEST BID",
                    value = "₹${maxBid}",
                    backgroundColor = Color.White.copy(alpha = 0.9f),
                    textColor = Color(0xFFE53935),
                    borderColor = Color.White,
                    borderWidth = borderWidth,
                    cornerRadius = innerCornerRadius
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Bid Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(innerCornerRadius))
                        .border(
                            borderWidth,
                            Color.White,
                            RoundedCornerShape(innerCornerRadius)
                        )
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFF5252), Color(0xFFE53935))
                            ),
                            shape = RoundedCornerShape(innerCornerRadius)
                        )
                        .clickable { onBidClick() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Bid",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PLACE BID",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Current Bid Indicator (only shows when there's a selected bid)
            if (selectedBid > 0) {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(innerCornerRadius)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your Bid: ₹$selectedBid",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoBox(
    title: String,
    value: String,
    backgroundColor: Color,
    textColor: Color,
    borderColor: Color,
    borderWidth: Dp,
    cornerRadius: Dp
) {
    Box(
        modifier = Modifier
//            .weight(1f)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(cornerRadius))
            .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
            .background(backgroundColor, RoundedCornerShape(cornerRadius))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = textColor.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
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
fun CarDetailRow(label: String, value: String) {
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

@Composable
fun CarDetailRows(label: String, value: String) {
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

fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str.substringAfter(","), Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        Log.e("Image Decode", "Error decoding base64 image: ${e.message}")
        null
    }
}

fun sendBidToServer(context: Context, carId: String, bidAmount: String, onResult: (Boolean) -> Unit) {
    val token = getToken(context)
    val client = OkHttpClient()

    val json = """
        {
            "saler_car": $carId,
            "bid_amount": $bidAmount
        }
    """
    Log.d("Bid", "bid_json>>>$json")
    val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
        .url(TestApi.place_bid)
        .post(requestBody)
        .apply {
            token?.let { addHeader("Authorization", "Bearer $it") }
        }
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            Log.d("API_RESPONSE", "Response: $responseBody")
            if (response.isSuccessful) {
                Log.d("PostSuccess", "Bid Posted Successfully")
                (context as? Activity)?.runOnUiThread {
                    Toast.makeText(context, "Bid Posted Successfully!", Toast.LENGTH_SHORT).show()
                }
                withContext(Dispatchers.Main) {
                    onResult(true) // Show success dialog
                }

            } else {
                Log.e("PostError", "Server error: ${response.code}")
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error: ${e.message}")
        }
    }
}