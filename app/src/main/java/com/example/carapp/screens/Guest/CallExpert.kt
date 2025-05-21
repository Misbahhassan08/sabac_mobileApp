package com.example.carapp.screens.Guest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.assets.redcolor
import com.example.carapp.screens.Expert
import com.example.carapp.screens.getToken
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


@Composable
fun CallExpert(navController: NavController, carId: Int, experts: List<Expert>) {

    val systemUiController = rememberSystemUiController()
    var expertList by remember { mutableStateOf<List<Expert>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        fetchExperts(TestApi.Get_Inspector) { experts ->
            expertList = experts
        }
    }

    systemUiController.isStatusBarVisible = false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background( redcolor)
    ) {
        HeaderSectionExpertVisi()
        ExpertVisitFor(navController,experts)
    }
}

@Composable
fun HeaderSectionExpertVisi() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(redcolor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 32.dp, end = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
//                Spacer(modifier = Modifier.width(8.dp))
                Text("Expert Visit", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
//            StepProgressIndicatorExpertVisit()
            Spacer(modifier = Modifier.height(34.dp))
            Text("Schedule your car Inspection", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun ExpertVisitFor(navController: NavController, list: List<Expert>) {
    var selectedExpertId by remember { mutableStateOf<Int?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 44.dp, topEnd = 44.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        LazyColumn {
            items(list) { item ->
                ExpertIte(
                    expert = item,
                    isSelected = selectedExpertId == item.id,
                    onClick = {
                        selectedExpertId = if (selectedExpertId == item.id) null else item.id
                    },
                    navController = navController,
                    context = context
                )
            }
        }
    }
}

@Composable
fun ExpertIte(
    expert: Expert,
    isSelected: Boolean,
    onClick: () -> Unit,
    navController: NavController,
    context: Context
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAECEE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Expert Icon
                Image(
                    painter = painterResource(id = R.drawable.male_icon), // Replace with your icon
                    contentDescription = "Expert Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 8.dp)
                )

                Column {
                    // Expert Name
                    Text(
                        text = "${expert.first_name} ${expert.last_name}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Phone Number with Icon
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.call), // Replace with your phone icon
                            contentDescription = "Phone Icon",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = expert.phone_number, fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }

            // Show buttons only when selected
            if (isSelected) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    /*Button(
                        onClick = {
                            onClickWhatsApp(
                                context = context,
                                mobileNumber = expert.phone_number,
                                message = "HELLO"
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Call Expert")
                    }*/
                    Button(
                        onClick = {
                            val carId = getCarId(context)
                            if (carId != -1) {
                                postExpertSelection(context, expert.id, carId) { success ->
                                    if (success) {
                                        onClickWhatsApp(
                                            context = context,
                                            mobileNumber = expert.phone_number,
                                            message = "Hello, I need assistance!"
                                        )
                                    } else {
                                        Log.e("API_ERROR", "Failed to select expert")
                                    }
                                }
                            } else {
                                Log.e("CAR_ID_ERROR", "Car ID not found")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Call Expert")
                    }
                }
            }
        }
    }
}

data class ScheduleItem(
    val availabilityId: Int,
    val date: String,
    val timeSlot: String


)

fun onClickWhatsApp(
    context: Context,
    mobileNumber: String,
    message: String
) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(
            "https://wa.me/${mobileNumber.removePrefix("+")}?text=${
                message.replace(
                    " ",
                    "%20"
                )
            }"
        )
    )
    context.startActivity(intent)

}

fun fetchExperts(url: String, onResult: (List<Expert>) -> Unit) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .get()
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("API_ERROR", "Failed to fetch experts: ${e.message}")
            // Return an empty list on failure
            onResult(emptyList())
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            if (responseBody != null) {
                try {
                    // Assuming your API returns a JSON array of experts
                    val jsonArray = JSONArray(responseBody)
                    val experts = mutableListOf<Expert>()
                    for (i in 0 until jsonArray.length()) {
                        val expertObject = jsonArray.getJSONObject(i)

                        val expert = Expert(
                            id = expertObject.getInt("id"),
                            first_name = expertObject.getString("first_name"),
                            last_name = expertObject.getString("last_name"),
                            phone_number = expertObject.getString("phone_number")
                        )
                        experts.add(expert)
                    }

                    Handler(Looper.getMainLooper()).post {
                        onResult(experts)
                    }
                } catch (e: Exception) {
                    Log.e("API_ERROR", "Error parsing experts: ${e.message}")
                    Handler(Looper.getMainLooper()).post {
                        onResult(emptyList())
                    }
                }
            } else {
                Log.e("API_ERROR", "Response body is null")
                Handler(Looper.getMainLooper()).post {
                    onResult(emptyList())
                }
            }
        }
    })
}


fun postExpertSelection(context: Context, expertId: Int, carId: Int, onResult: (Boolean) -> Unit) {
    val client = OkHttpClient()
    val token = getToken(context)
    val jsonObject = JSONObject().apply {
        put("inspector_id", expertId)
        Log.d("exp","EXPERTTTT $expertId")
        put("car_id", carId)
        Log.d("exp","EXPERDDDD $carId")
    }

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val body = jsonObject.toString().toRequestBody(mediaType)

    val request = Request.Builder()
        .url(TestApi.assign_inspector_to_car)
        .post(body)
        .addHeader("Authorization", "Bearer $token")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("API_ERROR", "Failed to send request: ${e.message}")
            Handler(Looper.getMainLooper()).post { onResult(false) }
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                Log.d("API_RESPONSE", "Successfully posted expert selection")
                Handler(Looper.getMainLooper()).post { onResult(true) }
            } else {
                Log.e("API_ERROR", "Error response: ${response.code}")
                Handler(Looper.getMainLooper()).post { onResult(false) }
            }
        }
    })
}
