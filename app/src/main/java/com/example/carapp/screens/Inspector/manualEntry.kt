package com.example.carapp.screens.Inspector

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.models.AssignedSlot
import com.example.carapp.models.CarI
import com.example.carapp.models.CarListViewModelI
import com.example.carapp.models.GuestListViewModel
import com.example.carapp.models.InspectionModel
import com.example.carapp.models.LinkedCar
import com.example.carapp.models.guest
import com.example.carapp.screens.fetchSchedule
import com.example.carapp.screens.getToken
import com.example.carapp.screens.getUserId
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
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ManualEntryScreen(
    navController: NavController,
    viewModel: GuestListViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val context = LocalContext.current
    val carList by viewModel.cars.collectAsState()
    val linkedCarList by viewModel.Linkedcars.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCarListguest(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("INSPECTOR", color = Color.White) },
//                backgroundColor = Color(0xFF1565C0) // Adjust color as needed
            )
        }
    ) { paddingValues ->
        CarListCon(
            carList = carList,
            linkedCarList = linkedCarList,
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CarListCon(
    carList: List<guest>,
    linkedCarList: List<LinkedCar>,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            items(carList) { car ->
                CarIt(car, navController)
                Spacer(modifier = Modifier.height(24.dp))
            }

            items(linkedCarList) { linkedCar ->
                LinkedCarItem(linkedCar, navController)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


//fun decodeBase64ToBit(base64String: String): Bitmap? {
//    return try {
//        // Extract Base64 part
//        val pureBase64 = base64String.substringAfter("base64,", base64String)
//
//        // Log the extracted Base64 string
//        Log.d("PureBase64", pureBase64.take(100)) // Only first 100 chars for debugging
//
//        // Decode Base64
//        val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
//        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
//    } catch (e: Exception) {
//        Log.e("Base64DecodeError", "Error decoding Base64: ${e.message}")
//        null
//    }
//}

fun decodeBase64ToBit(base64String: String): Bitmap? {
    return try {
        // Extract only the Base64 data (removing the prefix)
        val base64Strin = base64String.substringAfter("base64,")

        // Debugging: Log the first 100 characters of the Base64 string
        Log.d("Base64Debug", "Base64 Data (first 100 chars): ${base64Strin.take(100)}")

        // Check if Base64 is empty
        if (base64Strin.isEmpty()) {
            Log.e("Base64Error", "Base64 string is empty!")
            return null
        }

        // Decode Base64
        val decodedBytes = try {
            Base64.decode(base64Strin, Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e("Base64Error", "Base64 decoding failed: ${e.message}")
            return null
        }

        // Check if decoding was successful
        if (decodedBytes.isEmpty()) {
            Log.e("Base64Error", "Decoded byte array is empty!")
            return null
        }

        // Convert bytes to Bitmap
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

        if (bitmap != null) {
            Log.d("Base64Debug", "Decoded Bitmap successfully!")
        } else {
            Log.e("Base64Error", "BitmapFactory.decodeByteArray returned NULL!")
        }

        return bitmap
    } catch (e: Exception) {
        Log.e("Base64Error", "Error decoding Base64: ${e.message}")
        return null
    }
}
fun decodeBase64ToBi(base64String: String): Bitmap? {
    return try {
        val base64Data = base64String.substringAfter("base64,", base64String) // Removes prefix if present
        val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        Log.e("ImageDecodeError", "Failed to decode Base64 image", e)
        null
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CarIt(car: guest, navController: NavController) {
    val imageBitmap = remember { car.Photos.firstOrNull()?.let { decodeBase64ToBi(it) } }
//    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var reportExists by remember { mutableStateOf(false) }
    val context = LocalContext.current

//    LaunchedEffect(car.Photos) {
//        car.Photos.firstOrNull()?.let {
//            imageBitmap = decodeBase64ToBit(it)
//        }
//    }

    LaunchedEffect(car.saler_car_id) {
        reportExists = checkIfReportExists(car.saler_car_id.toString(), context)
    }

    val isInspected = car.isInspected
//    val buttonText = if (isInspected) "View Report" else "Start Inspection"
//    val buttonColor = if (isInspected) Color.Blue else Color.Red

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(14.dp, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAECEE)),
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
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap.asImageBitmap(),
                        contentDescription = "Car Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Log.e("ImageStatus", "Decoded image is NULL, showing placeholder")
                    Image(
                        painter = painterResource(id = R.drawable.car2), // Placeholder
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
                        text = "By Guest",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = car.carName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "PKR ${car.demand} lacs", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = car.sellerPhoneNumber, fontSize = 12.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        fetchSchedule(getUserId(context)) { freeSchedule, reservedSchedule ->
                            if (freeSchedule.isNotEmpty() || reservedSchedule.isNotEmpty()) {
                                val freeData = freeSchedule.joinToString(separator = ";") {
                                    "${it.availabilityId},${it.date} ${it.timeSlot}"
                                }
                                val reservedData = reservedSchedule.joinToString(separator = ";") {
                                    "${it.slotId},${it.date} ${it.timeSlot}"
                                }
                                val scheduleData = "$freeData|$reservedData"

                                Log.d("ScheduleData", "Navigating with: $scheduleData")

                                Handler(Looper.getMainLooper()).post {
                                    val encodedScheduleData = Uri.encode(scheduleData)
                                    navController.navigate("assignSlot/${car.saler_car_id}?scheduleData=$encodedScheduleData")
                                }
                            } else {
                                Log.e("NavigationError", "No schedule data received")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9ED90D)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(text = "Assign Slot", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LinkedCarItem(linkedCar: LinkedCar, navController: NavController) {
    val imageBitmap = remember { linkedCar.photos.firstOrNull()?.let { decodeBase64ToBit(it) } }
//    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var reportExists by remember { mutableStateOf(false) }
    val context = LocalContext.current

//    LaunchedEffect(car.Photos) {
//        car.Photos.firstOrNull()?.let {
//            imageBitmap = decodeBase64ToBit(it)
//        }
//    }

    LaunchedEffect(linkedCar.salerCarId) {
        reportExists = checkIfReportExists(linkedCar.salerCarId.toString(), context)
    }

    val isInspected = linkedCar.isInspected
//    val buttonText = if (isInspected) "View Report" else "Start Inspection"
//    val buttonColor = if (isInspected) Color.Blue else Color.Red

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(14.dp, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAECEE)),
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
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap.asImageBitmap(),
                        contentDescription = "Car Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Log.e("ImageStatus", "Decoded image is NULL, showing placeholder")
                    Image(
                        painter = painterResource(id = R.drawable.car2), // Placeholder
                        contentDescription = "Placeholder Car Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
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
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = linkedCar.carName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "PKR ${linkedCar.demand} lacs", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = linkedCar.sellerPhoneNumber, fontSize = 12.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        fetchSchedule(getUserId(context)) { freeSchedule, reservedSchedule ->
                            if (freeSchedule.isNotEmpty() || reservedSchedule.isNotEmpty()) {
                                val freeData = freeSchedule.joinToString(separator = ";") {
                                    "${it.availabilityId},${it.date} ${it.timeSlot}"
                                }
                                val reservedData = reservedSchedule.joinToString(separator = ";") {
                                    "${it.slotId},${it.date} ${it.timeSlot}"
                                }
                                val scheduleData = "$freeData|$reservedData"

                                Log.d("ScheduleData", "Navigating with: $scheduleData")

                                Handler(Looper.getMainLooper()).post {
                                    val encodedScheduleData = Uri.encode(scheduleData)
                                    navController.navigate("assignSlot/${linkedCar.salerCarId}?scheduleData=$encodedScheduleData")
                                }
                                updateStatu(linkedCar.salerCarId, context) { success ->
                                    if (success) {
                                        Log.d("StatusUpdate", "Status updated successfully")
                                    } else {
                                        Log.e("StatusUpdate", "Failed to update status")
                                    }
                                }
                            } else {
                                Log.e("NavigationError", "No schedule data received")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9ED90D)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(text = "Assign Slot", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

fun updateStatu(carId: Int, context: Context, onResult: (Boolean) -> Unit) {
    val client = OkHttpClient()

    val jsonObject = JSONObject().apply {
        put("status", "assigned")
    }

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val body = jsonObject.toString().toRequestBody(mediaType)

    val token = getToken(context)
    if (token.isNullOrEmpty()) {
        Log.e("StatusUpdate", "Token is missing!")
        onResult(false)
        return
    }

    val request = Request.Builder()
        .url("${TestApi.update_status}$carId/")
        .patch(body)
        .addHeader("Authorization", "Bearer $token")
        .addHeader("Content-Type", "application/json")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("StatusUpdate", "Failed to update status", e)
            Handler(Looper.getMainLooper()).post { onResult(false) }
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                Log.d("StatusUpdate", "Status updated successfully")
                Handler(Looper.getMainLooper()).post { onResult(true) }
            } else {
                Log.e("StatusUpdate", "Failed to update status: ${response.code}")
                Handler(Looper.getMainLooper()).post { onResult(false) }
            }
        }
    })
}
