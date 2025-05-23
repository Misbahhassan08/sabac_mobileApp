package com.example.carapp.screens.Guest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.assets.seller_Color
import com.example.carapp.screens.CustomAlertDialog
import com.example.carapp.screens.Expert
import com.example.carapp.screens.getSalerCarId
import com.example.carapp.screens.getToken
import com.example.carapp.screens.saveAndLogFormData
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestBookExpertVisit(navController: NavController, list: List<Expert>) {
    val systemUiController = rememberSystemUiController()
    var expertList by remember { mutableStateOf<List<Expert>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        fetchExpertsG(TestApi.Get_Inspector) { experts ->
            expertList = experts
        }
    }

    systemUiController.isStatusBarVisible = false
    var showDialog by rememberSaveable { mutableStateOf(false) }

    // Handle back press
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog = true
            }
        }
    }

    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    LaunchedEffect(Unit) {
        backDispatcher?.addCallback(backCallback)
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(seller_Color)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "Book Inspection",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(seller_Color)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Step progress under TopAppBar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                StepProgressIndicatorsssG(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 32.dp, bottom = 4.dp),
                    stepCount = 3,
                    currentStep = 1,
                    titles = listOf("Car Detail", "User Detail", "Book Inspection"),
                    onStepClicked = { /* optional */ }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = Color.Gray,
                    thickness = 3.dp
                )
            }

            ExpertVisitFormG(navController, list)
        }
    }

    if (showDialog) {
        CustomAlertDialog(
            onDismiss = { showDialog = false },
            onConfirm = {
                showDialog = false
                navController.navigate("dash") {
                    popUpTo("basicInfoScreen") { inclusive = true }
                }
            }
        )
    }
}


@Composable
fun StepProgressIndicatorsssG(
    modifier: Modifier = Modifier,
    stepCount: Int,
    currentStep: Int,
    titles: List<String>,
    onStepClicked: (Int) -> Unit
/*) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(stepCount) { index ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable { onStepClicked(index) }
            ) {
                // Step circle
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(
                            color = if (index <= 2) seller_Color
                            else Color.Gray,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (index + 1).toString(),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = titles.getOrElse(index) { "Step ${index + 1}" },
                    color = Color.DarkGray,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 4.dp,start = 8.dp, end = 8.dp)
                )
                // Connector line
                if (index < stepCount - 1) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .offset(y = (-13).dp)
                            .width(90.dp)
                            .background(
                                color = if (index < 1) seller_Color// Line from Step 1 to Step 2 is blue
                                else seller_Color // Other lines remain gray
                            )
                    )
                }
            }

            if (index < stepCount - 1) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .offset(y = (-13).dp)
                        .width(90.dp)
                        .background(
                            color = if (index < 2) Color.White
                            else Color.Gray
                        )
                )
            }
        }
    }
}*/
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(stepCount) { index ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable { onStepClicked(index) }
            ) {
                // Step circle
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(
                            color = if (index <= 1) seller_Color
                            else seller_Color, // Other steps are gray
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (index + 1).toString(),
                        color = Color.White, // Text stays white for all steps
                        fontSize = 14.sp
                    )
                }

                // Step title
                Text(
                    text = titles.getOrElse(index) { "Step ${index + 1}" },
                    color = Color.DarkGray,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 4.dp,start = 8.dp, end = 8.dp)
                )
            }

            // Connector line
            if (index < stepCount - 1) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .offset(y = (-13).dp)
                        .width(90.dp)
                        .background(
                            color = if (index < 1) seller_Color// Line from Step 1 to Step 2 is blue
                            else seller_Color // Other lines remain gray
                        )
                )
            }
        }
    }
}


@Composable
fun ExpertVisitFormG(navController: NavController, list: List<Expert>) {
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
                ExpertItemG(
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
fun ExpertItemG(
    expert: Expert,
    isSelected: Boolean,
    onClick: () -> Unit,
    navController: NavController,
    context: Context
) {
    var showSchedule by remember { mutableStateOf(false) }
    var schedules by remember {
        mutableStateOf<Pair<List<ScheduleItem>, List<ScheduleItem>>>(
            emptyList<ScheduleItem>() to emptyList<ScheduleItem>()
        )
    }
    var isLoading by remember { mutableStateOf(false) }

    var freeSchedule by remember { mutableStateOf<List<ScheduleItem>>(emptyList()) }
    var reservedSchedule by remember { mutableStateOf<List<ReservedScheduleItemG>>(emptyList()) }
    var isSlotSelected by remember { mutableStateOf(false) }
    var isBookingInProgress by remember { mutableStateOf(false) }
    var selectedSlot by remember { mutableStateOf<ScheduleItem?>(null) }

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
                Image(
                    painter = painterResource(id = R.drawable.male_icon),
                    contentDescription = "Expert Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 8.dp)
                )

                Column {
                    Text(
                        text = "Inspector ${expert.id}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            if (isSelected) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            val carId = getSalerCarId(context)
                            if (carId != -1) {
                                postExpertSelection(context, expert.id, carId) { success ->
                                    if (success) {
                                        onClickWhatsAppG(
                                            context = context,
                                            mobileNumber = expert.phone_number,
                                            message = "Hello, I need assistance!"
                                        )
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Call Expert")
                    }

                    Button(
                        onClick = {
                            isLoading = true
                            fetchScheduleG(expert.id) { freeSlotsG, reservedSlotsG ->
                                freeSchedule = freeSlotsG
                                reservedSchedule = reservedSlotsG
                                showSchedule = true
                                isLoading = false
                            }
                        }
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Select Date & Time")
                        }
                    }
                }

// Modify your ScheduleList to update this state
                if (showSchedule) {

                    var selectedSlot by remember { mutableStateOf<ScheduleItem?>(null) }

                    ScheduleListG(
                        freeSlots = freeSchedule,
                        reservedSlots = reservedSchedule,
                        onSlotSelected = { selectedSlotItemG ->
                            // Save data using the enhanced saveAndLogFormData function
                            saveAndLogFormDataG(
                                selectedOption = "slot_selected",
                                inputFields = emptyMap(),
                                context = context,
                                date = selectedSlotItemG.date,
                                timeSlot = selectedSlotItemG.timeSlot
                            )

                            // Update the selected slot
                            selectedSlot = selectedSlotItemG
                            isSlotSelected = true

                            Toast.makeText(context, "Slot saved locally", Toast.LENGTH_SHORT).show()
                        },
                        selectedSlot = selectedSlot // Pass the selected slot to highlight it
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val savedJson = prefs.getString("saved_form_data", "{}") ?: "{}"
            val token = getToken(context)

            try {
                val jsonElement = Json.parseToJsonElement(savedJson).jsonObject

                val apiJson = buildJsonObject {
                    put("car_name", jsonElement["car_name"]?.jsonPrimitive?.contentOrNull ?: "")
                    put("company", jsonElement["company"]?.jsonPrimitive?.contentOrNull ?: "")
                    put("year", jsonElement["year"]?.jsonPrimitive?.contentOrNull ?: "")
                    put("engine_size", jsonElement["engine_size"]?.jsonPrimitive?.contentOrNull ?: "")
                    put("milage", jsonElement["milage"]?.jsonPrimitive?.contentOrNull ?: "")

                    put("option_type", mapOptionTypeG(jsonElement["option_type"]?.jsonPrimitive?.contentOrNull))
                    put("paint_condition", mapPaintConditionG(jsonElement["paint_condition"]?.jsonPrimitive?.contentOrNull))
                    put("specs", mapSpecsG(jsonElement["specs"]?.jsonPrimitive?.contentOrNull))

                    put("primary_phone_number", jsonElement["primaryPhone"]?.jsonPrimitive?.contentOrNull ?: "")
                    put("secondary_phone_number", jsonElement["secondaryPhone"]?.jsonPrimitive?.contentOrNull ?: "")
                    put("inspection_date", jsonElement["availabilityId"]?.jsonPrimitive?.contentOrNull ?: "")
                    put("inspection_time", jsonElement["timeSlot"]?.jsonPrimitive?.contentOrNull ?: "")
                    put("inspector", expert.id)


                    // Handle images
                    if (jsonElement["imageUrls"] != null && jsonElement["imageUrls"] is JsonArray) {
                        val imageArray = jsonElement["imageUrls"]!!.jsonArray.map {
                            JsonPrimitive(it.jsonPrimitive.content)
                        }
                        put("photos", JsonArray(imageArray))
                    } else {
                        put("photos", JsonArray(emptyList()))
                    }

                }
                Log.d("API_PAYLOAD", "Sending JSON:\n${apiJson.toString()}")

                val client = OkHttpClient()
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val body = apiJson.toString().toRequestBody(mediaType)
                val request = Request.Builder()
                    .url(TestApi.Post_Add)
                    .post(body)
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("Content-Type", "application/json")
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        (context as? Activity)?.runOnUiThread {
                            Toast.makeText(context, "Failed to submit: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val isSuccess = response.isSuccessful
                        val message = if (isSuccess) "Inspection booked successfully!" else "Submission failed: ${response.message}"
                        (context as? Activity)?.runOnUiThread {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            if (isSuccess) {
                                navController.navigate("dash")
                            }
                        }
                        Log.d("API_RESPONSE", "Status: ${response.code}\nBody: ${response.body?.string()}")
                    }
                })

            } catch (e: Exception) {
                Toast.makeText(context, "Error parsing form data", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        },
        modifier = Modifier.fillMaxWidth(1.0f),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSlotSelected) seller_Color else Color.LightGray,
            disabledContainerColor = Color.LightGray
        ),
        shape = RoundedCornerShape(8.dp),
        enabled = isSlotSelected && !isBookingInProgress // Disable during booking process
    ) {
        if (isBookingInProgress) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.Red,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Booking...", fontSize = 16.sp)
            }
        } else {
            Text("Book Inspection", fontSize = 16.sp)
        }
    }

}

fun mapOptionTypeG(value: String?): String {
    return when (value?.trim()?.lowercase()) {
        "full option" -> "full_option"
        "mid option" -> "mid_option"
        "basic" -> "basic"
        "i don't know" -> "i_dont_know"
        else -> "i_dont_know" // fallback to valid value
    }
}

fun mapSpecsG(value: String?): String {
    return when (value?.trim()?.lowercase()) {
        "gcc specs" -> "gcc_specs"
        "non specs" -> "non_specs"
        "i don't know" -> "i_dont_know"
        else -> "i_dont_know"
    }
}

fun mapPaintConditionG(value: String?): String {
    return when (value?.trim()?.lowercase()) {
        "original paint" -> "original_paint"
        "partial repaint" -> "partial_repaint"
        "full repaint" -> "full_repaint"
        "i don't know" -> "i_dont_know"
        else -> "i_dont_know"
    }
}



@Composable
fun ScheduleListG(
    freeSlots: List<ScheduleItem>,
    reservedSlots: List<ReservedScheduleItemG>,
    onSlotSelected: (ScheduleItem) -> Unit,
    selectedSlot: ScheduleItem? = null // Add selected slot parameter
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Available Slots:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (freeSlots.isEmpty()) {
            Text("No available slots")
        } else {
            freeSlots.groupBy { it.date }.forEach { (date, slots) ->
                Text(
                    text = date,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
                LazyRow {
                    items(slots) { slot ->
                        SlotChipG(
                            time = slot.timeSlot,
                            isAvailable = true,
                            isSelected = selectedSlot?.timeSlot == slot.timeSlot && selectedSlot.date == slot.date,
                            onClick = { onSlotSelected(slot) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SlotChipG(
    time: String,
    isAvailable: Boolean,
    isSelected: Boolean, // Add selected state
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> Color.Red // Selected color
        isAvailable -> Color(0xFF4CAF50) // Available color
        else -> Color.LightGray // Unavailable color
    }
    val contentColor = if (isAvailable || isSelected) Color.White else Color.DarkGray

    Surface(
        modifier = Modifier
            .padding(end = 8.dp)
            .clickable(enabled = isAvailable, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor.copy(alpha = if (isAvailable || isSelected) 1f else 0.5f),
        border = if (isSelected) BorderStroke(2.dp, Color.Red) else null
    ) {
        Text(
            text = time,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}


fun fetchScheduleG(
    expertId: Int,
    onResult: (List<ScheduleItem>, List<ReservedScheduleItemG>) -> Unit
) {
    val client = OkHttpClient()
    val url = "${TestApi.Get_Inspector_slots}?inspector=$expertId"

    Log.d("API_CALL", "Requesting URL: $url")

    val request = Request.Builder()
        .url(url)
        .get()
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("API_ERROR", "Failed to fetch schedule", e)
            Handler(Looper.getMainLooper()).post {
                onResult(emptyList(), emptyList())
            }
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            Log.d("API_RESPONSE", "Full response: $responseBody")
            if (responseBody != null) {
                try {
                    val jsonObject = JSONObject(responseBody)
                    val freeSlotsArray = jsonObject.getJSONArray("free_slots")
                    val reservedSlotsArray = jsonObject.getJSONArray("reserved_slots")

                    val freeScheduleList = mutableListOf<ScheduleItem>()
                    val reservedScheduleList = mutableListOf<ReservedScheduleItemG>()

                    // Parse free slots
                    for (i in 0 until freeSlotsArray.length()) {
                        val slot = freeSlotsArray.getJSONObject(i)
                        val availabilityId = slot.getInt("availability_id")
                        val date = slot.getString("date")
                        val timeSlot = slot.getString("time_slot")
                        freeScheduleList.add(ScheduleItem(availabilityId, date, timeSlot))
                    }

                    // Parse reserved slots
                    for (i in 0 until reservedSlotsArray.length()) {
                        val slot = reservedSlotsArray.getJSONObject(i)
                        val slotId = slot.getString("slot_id")
                        val date = slot.getString("date")
                        val timeSlot = slot.getString("time_slot")
                        reservedScheduleList.add(ReservedScheduleItemG(slotId, date, timeSlot))
                    }

                    Log.d("ScheduleData", "Fetched ${freeScheduleList.size} free slots, ${reservedScheduleList.size} reserved slots")

                    Handler(Looper.getMainLooper()).post {
                        onResult(freeScheduleList, reservedScheduleList)
                    }
                } catch (e: Exception) {
                    Log.e("API_ERROR", "Error parsing schedule: ${e.message}")
                    Handler(Looper.getMainLooper()).post {
                        onResult(emptyList(), emptyList())
                    }
                }
            } else {
                Log.e("API_ERROR", "Response body is null")
                Handler(Looper.getMainLooper()).post {
                    onResult(emptyList(), emptyList())
                }
            }
        }
    })
}

data class ScheduleItemG(
    val availabilityId: Int,
    val date: String,
    val timeSlot: String
)
data class ReservedScheduleItemG(
    val slotId: String,
    val date: String,
    val timeSlot: String
)

fun onClickWhatsAppG(
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

fun fetchExpertsG(url: String, onResult: (List<Expert>) -> Unit) {
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

