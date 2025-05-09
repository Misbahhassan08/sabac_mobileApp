package com.example.carapp.screens

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carapp.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.carapp.Apis.ApiCallback
import com.example.carapp.Apis.TestApi
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BasicScreen(navController: NavController, scheduleData: String) {
    val systemUiController = rememberSystemUiController()

    systemUiController.isStatusBarVisible = false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background( Color(0xFF1C72E8))
    ) {
        Section()
//        UserInfo(navController)
        DateAndTimeScreen(navController,scheduleData)
    }
}

@Composable
fun Section() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Brush.verticalGradient(listOf(Color(0xFF1E4DB7), Color(0xFF1C72E8))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 32.dp, end = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.height(34.dp))
            Text("Select Your Date & Time for Inspection", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DateAndTimeScreen(navController: NavController, scheduleData: String?) {
    Log.d("DateAndTimeScreen", "Received scheduleData: $scheduleData")

    val scheduleState = remember {
        derivedStateOf {
            scheduleData?.split("|")?.let { parts ->
                val free = parts.getOrNull(0)
                    ?.split(";")
                    ?.filter { it.isNotBlank() }
                    ?: emptyList()
                val reserved = parts.getOrNull(1)
                    ?.split(";")
                    ?.filter { it.isNotBlank() }
                    ?: emptyList()
                Pair(free, reserved)
            } ?: Pair(emptyList(), emptyList())
        }
    }

    val (freeSlots, reservedSlots) = scheduleState.value

    val context = LocalContext.current
    var expandedSlot by remember { mutableStateOf<String?>(null) }
    Spacer(modifier = Modifier.height(28.dp))
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 44.dp, topEnd = 44.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Available Slots", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        if (freeSlots.isNotEmpty()) {
            items(freeSlots) { slot ->
                val parts = slot.split(",")
                if (parts.size >= 2) {
                    val dateTimePart = parts[1].trim()
                    val date = dateTimePart.split(" ").getOrNull(0) ?: ""
                    val time = dateTimePart.split(" ").getOrNull(1) ?: ""

                    ExpandableSlotCard(
                        date = date,
                        time = time,
                        isExpanded = expandedSlot == slot,
                        onClick = { expandedSlot = if (expandedSlot == slot) null else slot },
                        onBookClick = {
                            val availabilityId = parts[0].toIntOrNull() ?: 0
                            bookSlot(context, availabilityId, time, object : ApiCallback {
                                override fun onSuccess(response: String) {
                                    Toast.makeText(context, "Slot booked successfully", Toast.LENGTH_SHORT).show()
                                    postStatus(context, object : ApiCallback {
                                        override fun onSuccess(response: String) {
                                            Toast.makeText(context, "Data submitted successfully", Toast.LENGTH_SHORT).show()
                                            (context as? Activity)?.runOnUiThread {
                                                navController.navigate("check")
                                            }
                                        }
                                        override fun onFailure(error: String) {
                                            Toast.makeText(context, "Failed to submit: $error", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                                }
                                override fun onFailure(error: String) {
                                    Toast.makeText(context, "Booking failed: $error", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    )
                }
            }
        } else {
            item {
                Text("No available slots", color = Color.Gray, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ExpandableSlotCard(
    date: String,
    time: String,
    isExpanded: Boolean,
    onClick: () -> Unit,
    isAvailable: Boolean = true,
    onBookClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = if (isAvailable) Color.White else Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.register_icon),
                    contentDescription = "Date Icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = date, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.tag_icon),
                    contentDescription = "Time Icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = time, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            if (isExpanded && isAvailable && onBookClick != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onBookClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Book Slot")
                }
            }
        }
    }
}

@Composable
fun SlotCard(date: String, time: String, isAvailable: Boolean, onBookClick: (() -> Unit)? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = if (isAvailable) Color.White else Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.register_icon), // Date Icon
                    contentDescription = "Date Icon",
                    tint = Color.Blue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = date, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.tag_icon), // Time Icon
                    contentDescription = "Time Icon",
                    tint = Color.Blue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = time, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            if (isAvailable && onBookClick != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onBookClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Book Slot")
                }
            }
        }
    }
}


fun bookSlot(
    context: Context,
    availabilityId: Int,
    timeSlot: String,
    callback: ApiCallback
) {
    val client = OkHttpClient()
    val url = TestApi.Post_BookSlot

    val jsonObject = JSONObject().apply {
        put("availability_id", availabilityId)
        put("time_slot", timeSlot)
        put("saler_car_id", getSalerCarId(context))
    }

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val body = jsonObject.toString().toRequestBody(mediaType)

    val token = getToken(context)
    Log.d("Token1", "TOKEN IN ACCESS$token")
    val requestBuilder = Request.Builder()
        .url(url)
        .post(body)

    token?.let {
        requestBuilder.addHeader("Authorization", "Bearer $it")
    }

    val request = requestBuilder.build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("BookSlot", "Failed to book slot: ${e.message}")
            Handler(Looper.getMainLooper()).post {
                callback.onFailure(e.message ?: "Unknown error")
            }
        }
        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string() ?: ""
            if (response.isSuccessful) {
                Log.d("BookSlot", "Response: $responseBody")
                Handler(Looper.getMainLooper()).post {
                    callback.onSuccess(responseBody)
                }
            } else {
                val errorMsg = "Error: ${response.code}"
                Log.e("BookSlot", errorMsg)
                Handler(Looper.getMainLooper()).post {
                    callback.onFailure(errorMsg)
                }
            }
        }
    })
}


fun postStatus(context: Context, callback: ApiCallback) {
    val client = OkHttpClient()

    val salerCarId = getSalerCarId(context)
    val url = "${TestApi.update_is_booked}$salerCarId/"

    val requestBuilder = Request.Builder()
        .url(url)
        .post("".toRequestBody(null))

    val token = getToken(context)
    token?.let {
        requestBuilder.addHeader("Authorization", "Bearer $it")
    }

    val request = requestBuilder.build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("PostAnotherApi", "Failed: ${e.message}")
            Handler(Looper.getMainLooper()).post {
                callback.onFailure(e.message ?: "Unknown error")
            }
        }
        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string() ?: ""
            if (response.isSuccessful) {
                Log.d("PostAnotherApi", "Response: $responseBody")
                Handler(Looper.getMainLooper()).post {
                    callback.onSuccess(responseBody)
                }
            } else {
                val errorMsg = "Error: ${response.code}"
                Log.e("PostAnotherApi", errorMsg)
                Handler(Looper.getMainLooper()).post {
                    callback.onFailure(errorMsg)
                }
            }
        }
    })
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewScree() {
    val navController = rememberNavController()
    val scheduleData = String
    BasicScreen(navController, scheduleData.toString())
}
