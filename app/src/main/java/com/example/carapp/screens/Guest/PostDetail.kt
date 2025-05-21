package com.example.carapp.screens.Guest

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.assets.redcolor
import com.example.carapp.screens.getToken
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetail(navController: NavController, guestId: String) {
    val selectedImages = remember { mutableStateListOf<String>() }
    var description by remember { mutableStateOf("") }

    val systemUiController = rememberSystemUiController()

    systemUiController.isStatusBarVisible = false

    Scaffold (
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(
                       redcolor
                    ),
                title = {
                    Text(
                        "Sell Your Car",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
//                navigationIcon = {
//                    IconButton(onClick = {
//                        navController.navigate("seller")
//                    }) {
//                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
//                    }
//                }
            )
        },
        bottomBar = {
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
//            .padding(16.dp)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            if (selectedImages.size == 0) {
                AddImag(selectedImages)
            }
            else {
                PageSlide(selectedImages, selectedImages)
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                val dropdownOptions = mapOf(
                    "Car Name" to listOf("Toyota",
                        "Honda",
                        "Suzuki",
                        "BMW",
                        "Mercedes"),
                    "Car Model" to listOf("2015",
                        "2016",
                        "2017",
                        "2018",
                        "2019"),
                    "Body color" to listOf("White",
                        "Black",
                        "Blue",
                        "Red",
                        "Silver"),
                    "Company" to listOf("Toyota",
                        "Honda",
                        "Suzuki",
                        "BMW",
                        "Mercedes"),
                    "KMs Driven" to listOf(""),
                    "Variant (auto/manual)" to listOf("Automatic",
                        "Manual"),
                    "Engine Capacity" to listOf("1000",
                        "1300",
                        "1500",
                        "1800",
                        "2000"),
                    "Condition" to listOf("New",
                        "Used",
                        "Excellent",
                        "Good",
                        "Fair"),
                    "Fuel type" to listOf("Petrol",
                        "Diesel",
                        "Hybrid",
                        "Electric"),
                    "Assembly" to listOf("Local",
                        "Imported"),
                    "Price (PKR)" to listOf(""),
                    "Location" to listOf("Karachi",
                        "Lahore",
                        "Islamabad",
                        "Rawalpindi",
                        "Faisalabad"),
                    "Registered in" to listOf("Punjab",
                        "Sindh",
                        "KPK",
                        "Balochistan"),
                    "Phone Number" to listOf("")
                )

                val iconMapping = mapOf(
                    "Location" to R.drawable.location_icon,
                    "Car Model" to R.drawable.car_icon,
                    "Registered in" to R.drawable.register_icon,
                    "Body color" to R.drawable.color_bucket_icon,
                    "KMs Driven" to R.drawable.speed_icon,
                    "Car Name" to R.drawable.car_icon,
                    "Company" to R.drawable.company,
                    "Condition" to R.drawable.termsconditions,
                    "Variant (auto/manual)" to R.drawable.varient,
                    "Fuel type" to R.drawable.fueltype,
                    "Assembly" to R.drawable.assembly,
                    "Engine Capacity" to R.drawable.enginecapacity,
                    "Price (PKR)" to R.drawable.description_icon,
                    "Phone Number" to R.drawable.call
                )

                val inputFields = remember {
                    dropdownOptions.keys.associateWith { mutableStateOf("") }
                }
                ProfessionalInputField(inputFields, iconMapping, dropdownOptions)

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = { Text("For example: Alloy Rims, First Owner, etc.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    maxLines = 3
                )
                val context = LocalContext.current
                Button(
                    onClick = {
                        val requestBody = prepareRequestBodySeparat(context, selectedImages, inputFields, description, guestId)
                        sendPostReques(context, requestBody, navController)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Book Inspection Date", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun ProfessionalInputField(
    inputFields: Map<String, MutableState<String>>,
    iconMapping: Map<String, Int>,
    dropdownOptions: Map<String, List<String>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        inputFields.keys.forEach { fieldName ->
            val textFieldValue = inputFields[fieldName]?.value ?: ""
            var expanded by remember { mutableStateOf(false) }
            val suggestions = dropdownOptions[fieldName] ?: emptyList()

            Box(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = fieldName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = { newValue ->
                            inputFields[fieldName]?.value = newValue

                        },
                        placeholder = { Text("Enter or select $fieldName") },
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .clickable { expanded = true }
                            .padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = redcolor,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = redcolor
                        ),
                        leadingIcon = {
                            iconMapping[fieldName]?.let { iconResId ->
                                Image(
                                    painter = painterResource(id = iconResId),
                                    contentDescription = "$fieldName Icon",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                                    contentDescription = if (expanded) "Collapse" else "Expand"
                                )
                            }
                        }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        suggestions.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    inputFields[fieldName]?.value = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun InputFieldWithDropdow(
    label: String,
    icon: Int,
    dropdownItems: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(selectedItem) }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newText ->
                textFieldValue = newText
                onItemSelected(newText)
            },
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = label
                )
            },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            dropdownItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        textFieldValue = item
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PageSlide(pages: MutableList<String>, sliderList: MutableList<String>) {

    val pagerState = rememberPagerState { pages.size + 1 }
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
        ) { page ->
            Box(
                modifier = Modifier
                    .matchParentSize(),
                contentAlignment = Alignment.Center
            ) {
                if (page < pages.size) {
                    val painter = rememberAsyncImagePainter(model = pages[page])
                    Image(
                        painter = painter, contentDescription = "Page $page",
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    AddImag(sliderList)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                scope.launch {
                    pagerState.scrollToPage(pagerState.currentPage - 1)
                }
            },
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x26000000)).fillMaxHeight()
//                        .alpha(if (page == 0) 0f else 1f)
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "",  tint = Color.White)
            }
            IconButton(onClick = {
                scope.launch {
                    pagerState.scrollToPage(pagerState.currentPage + 1)
                }
            },
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x26000000)).fillMaxHeight()
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "", tint = Color.White)
            }
        }
    }
}

fun prepareRequestBodySeparat(
    context: Context,
    selectedImages: List<String>,
    inputFields: Map<String, MutableState<String>>,
    description: String,
    guestId: String
): JSONObject {
    val jsonObject = JSONObject()

    // Define a mapping from UI label to API key
    val keyMapping = mapOf(
        "Car Name" to "car_name",
        "Company" to "company",
        "Body color" to "color",
        "Condition" to "condition",
        "Car Model" to "model",
        "Price (PKR)" to "demand",
        "Variant (auto/manual)" to "type",
        "Fuel type" to "fuel_type",
        "Registered in" to "registered_in",
        "Assembly" to "assembly",
        "Location" to "city",
        "KMs Driven" to "milage",
        "Engine Capacity" to "engine_capacity",
        "Phone Number" to "phone_number"
    )

    inputFields.forEach { (label, valueState) ->
        val apiKey = keyMapping[label] ?: label.replace(" ", "_").toLowerCase()
        val value = valueState.value

        when (apiKey) {
            "milage" -> {

                val numericValue = value.filter { it.isDigit() }
                jsonObject.put(apiKey, numericValue.toIntOrNull() ?: 0)
            }
            "engine_capacity" -> {
                val numericValue = value.filter { it.isDigit() }
                jsonObject.put(apiKey, numericValue.toIntOrNull() ?: 0)
            }
            "demand" -> {
                val numericValue = value.filter { it.isDigit() }
                jsonObject.put(apiKey, numericValue)
            }
            else -> {
                jsonObject.put(apiKey, value)
            }
        }
    }

    jsonObject.put("description", description)
    jsonObject.put("guest_id", guestId)

    val base64Images = JSONArray()
    for (uriString in selectedImages) {
        val base64String = convertImageToBase6(context, Uri.parse(uriString))
        base64Images.put(base64String)
    }
    jsonObject.put("photos", base64Images)

    return jsonObject
}

@Composable
fun AddImag(sliderList: MutableList<String>){

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            uri.let {
//            selectedMediaUri = it
                sliderList.add(it.toString())
            }
        }
    }

    val stroke = Stroke(width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable {
                launcher.launch("image/*")
            }
            .drawBehind { drawRoundRect(color = Color.Black, style = stroke) }
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.camera_icon),
                contentDescription = "Add photo",
                tint = Color.Gray,
                modifier = Modifier.size(48.dp)
            )
            Text("Add photo", color = Color.Gray)
            Text(
                "Tap on the icon and select the image to add to the post",
                color = Color.Gray,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

fun convertImageToBase6(context: Context, imageUri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(imageUri)
    val byteArray = inputStream?.readBytes()
    inputStream?.close()
    return if (byteArray != null) {
        Base64.encodeToString(byteArray, Base64.DEFAULT)
    } else {
        ""
    }
}

/*
fun sendPostReques(context: Context, requestBody: JSONObject, navController: NavController)     {
    val client = OkHttpClient()

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val body = requestBody.toString().toRequestBody(mediaType)
    Log.d("REQUEST_PAYLOAD", requestBody.toString())

//    val token = getToken(context)

    val requestBuilder = Request.Builder()
        .url(TestApi.guest_add_car_details)
        .post(body)

//    token?.let {
//        requestBuilder.addHeader("Authorization", "Bearer $it")
//    }

    val request = requestBuilder.build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("API_Call", "Failed to send request: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseString = response.body?.string()
                Log.d("API_Call", "Response: $responseString")
                try {
                    val jsonResponse = JSONObject(responseString)
                    val carId = jsonResponse.getInt("car_id")
                    saveCarId(context, carId)

                    (context as? Activity)?.runOnUiThread {
//                        navController.navigate("post")
                    }
                } catch (e: JSONException) {
                    Log.e("API_Call", "JSON parsing error: ${e.message}")
                }
            } else {
                Log.e("API_Call", "Error: ${response.code}")
            }
        }
    })
}

*/
fun sendPostReques(context: Context, requestBody: JSONObject, navController: NavController) {
    val client = OkHttpClient()

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val body = requestBody.toString().toRequestBody(mediaType)
    Log.d("REQUEST_PAYLOAD", requestBody.toString())

    val requestBuilder = Request.Builder()
        .url(TestApi.guest_add_car_details)
        .post(body)

    val request = requestBuilder.build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("API_Call", "Failed to send request: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val responseString = response.body?.string()
            Log.d("API_Call", "Response: $responseString")

            if (response.isSuccessful) {
                try {
                    val jsonResponse = JSONObject(responseString)
                    val carId = jsonResponse.getInt("car_id")
                    saveCarId(context, carId)


                    try {
                        val expertsResponse = makeOkHttpReques(TestApi.Get_Inspector)
                        Log.d("API_RESPONSE", "Response: $expertsResponse")
                        val experts: List<Expert> = Gson().fromJson(
                            expertsResponse,
                            object : TypeToken<List<Expert>>() {}.type
                        )

                        val jsonExperts = Uri.encode(Gson().toJson(experts))

                        (context as? Activity)?.runOnUiThread {
                            navController.navigate("CallExpert/$carId/$jsonExperts")
                        }
                    } catch (e: Exception) {
                        Log.e("API_ERROR", "GET request failed: ${e.message}")
                        (context as? Activity)?.runOnUiThread {
                            Toast.makeText(context, "GET request failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: JSONException) {
                    Log.e("API_Call", "JSON parsing error: ${e.message}")
                }
            } else {
                Log.e("API_Call", "Error: ${response.code}")
            }
        }
    })
}

fun saveCarId(context: Context, carId: Int) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putInt("CAR_ID", carId).apply()
    Log.d("ID", "CAR_ID$carId")
}

fun getCarId(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("CAR_ID", -1) // Default to -1 if not found
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, widthDp = 309, heightDp = 675)
//@Composable
//fun SliderPreview() {
//    val navController = rememberNavController()
//    CarSellScreen(navController = navController)
//}