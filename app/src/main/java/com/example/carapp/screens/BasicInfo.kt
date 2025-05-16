package com.example.carapp.screens

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carapp.Apis.ApiCallback
import com.example.carapp.Apis.TestApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


@Composable
fun BasicInfoScreen(navController: NavController) {
    val systemUiController = rememberSystemUiController()

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
    val context = LocalContext.current
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    LaunchedEffect(Unit) {
        backDispatcher?.addCallback(backCallback)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(redcolor)
    ) {
        HeaderSection {
            showDialog = true
        }
        UserInfoForm(navController)
    }

    if (showDialog) {
        CustomAlertDialog(
            onDismiss = { showDialog = false },
            onConfirm = {
                showDialog = false
                navController.navigate("seller") { popUpTo("basicInfoScreen") { inclusive = true } }
            }
        )
    }
}

@Composable
fun HeaderSection(function: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                redcolor
//                Brush.verticalGradient(listOf(Color(0xFF1E4DB7), Color(0xFF1C72E8)))
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 32.dp, end = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
//                Spacer(modifier = Modifier.width(8.dp))
                Text("Basic info", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            StepProgressIndicators(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                stepCount = 3,
                currentStep = 1, // Change this based on your actual step
                titles = listOf("Step 1", "Step 2", "Step 3"), // Replace with your titles
                onStepClicked = { /* Handle step click if needed */ }
            )

            Spacer(modifier = Modifier.height(25.dp))
            Text("Let's get you started", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
        }
    }
}
/*@Composable
fun StepProgressIndicator(
    modifier: Modifier = Modifier,
    currentStep: Int = 1, // Default to step 1 (Basic Info)
    onStepClicked: (Int) -> Unit = {}
) {
    val titles = listOf("Basic Info", "Book Expert", "Step 3")
    val stepCount = titles.size

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(stepCount) { index ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                // Circle and line container
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // Connector line before the circle (except first step)
                    if (index > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(
                                    color = if (index <= currentStep) Color.White
                                    else Color.White//.copy(alpha = 0.3f)
                                )
                                .align(Alignment.CenterStart)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(
                                color = if (index <= currentStep) Color.White
                                else Color.White.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                            .zIndex(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            color = if (index <= currentStep) Color(0xFF1C72E8)
                            else Color.White,
                            fontSize = 14.sp
                        )
                    }

                    // Connector line after the circle (except last step)
                    if (index < stepCount - 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(
                                    color = if (index < currentStep) Color.White
                                    else Color.White.copy(alpha = 0.3f)
                                )
                                .align(Alignment.CenterEnd)
                        )
                    }
                }

                // Step title
                Text(
                    text = titles[index],
                    color = Color.White,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}*/
@Composable
fun StepProgressIndicators(
    modifier: Modifier = Modifier,
    stepCount: Int,
    currentStep: Int,
    titles: List<String>,
    onStepClicked: (Int) -> Unit
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
                            color = if (index <= 1) Color.Red
                            else Color.Gray, // Other steps are gray
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
                    modifier = Modifier.padding(top = 4.dp)
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
                            color = if (index < 1) Color.White // Line from Step 1 to Step 2 is blue
                            else Color.Gray // Other lines remain gray
                        )
                )
            }
        }
    }
}



/*

@Composable
fun StepProgressIndicator(
    modifier: Modifier = Modifier,
    currentStep: Int = 1, // Default to step 1 (Basic Info)
    onStepClicked: (Int) -> Unit = {}
) {
    val titles = listOf("Basic Info", "Book Expert", "Step 3")
    val stepCount = titles.size

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(stepCount) { index ->
            // Step container
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                // Circle and line container
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // Only show connector line if not first step and not last step
                    if (index > 0 && index < stepCount) {
                        // Left connector line
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(
                                    color = if (index <= currentStep) Color.White
                                    else Color.White//.copy(alpha = 0.3f)
                                )
                                .align(Alignment.CenterStart)
                        )
                    }

                    // Step circle
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(
                                color = if (index <= currentStep) Color.White
                                else Color.White,//.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                            .zIndex(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            color = if (index <= currentStep) Color(0xFF1C72E8)
                            else Color.White,
                            fontSize = 14.sp
                        )
                    }

                    if (index < stepCount - 1 && index >= 0) {
                        // Right connector line
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(2.dp)
                                .background(
                                    color = if (index < currentStep) Color.White
                                    else Color.White.copy(alpha = 0.3f)
                                )
//                                .align(Alignment.CenterEnd)
                        )
                    }
                }

                // Step title
                Text(
                    text = titles[index],
                    color = Color.White,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
*/

/*

@Composable
fun StepProgressIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.White)
                .align(Alignment.Center)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf("Basic Info", "Expert Visit").forEachIndexed { index, step ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (index == 0) Color.Yellow else Color.Gray)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(step, color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

*/


/*@Composable
fun UserInfoForm(navController: NavController) {
    val context = LocalContext.current
    val storedUsername = getUsername(context) ?: ""
    val storedPhone = getPhone(context) ?: ""

    var username by rememberSaveable { mutableStateOf(storedUsername) }
    var phone by rememberSaveable { mutableStateOf(storedPhone) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 44.dp, topEnd = 44.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(28.dp))
        UserInputField(
            label = "Your full name",
            placeholder = "Enter your full name",
            icon = Icons.Default.Person,
            value = username,
            onValueChange = { username = it }
        )
        Spacer(modifier = Modifier.height(20.dp))
        UserInputField(
            label = "Your phone number",
            placeholder = "Enter your phone number",
            icon = Icons.Default.Call,
            value = phone,
            onValueChange = { phone = it }
        )

        Spacer(modifier = Modifier.weight(1f))
        ContinueButton(navController, username, phone, context)
        Spacer(modifier = Modifier.height(24.dp))
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputField(
    label: String,
    placeholder: String,
    icon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.LightGray
            )
        )
    }
}*/
@Composable
fun UserInfoForm(navController: NavController) {
    val context = LocalContext.current
    val storedUsername = getUsername(context) ?: ""
    val storedFirstname = getFirstName(context) ?: ""
    val storedLastname = getLastName(context) ?: ""
    val storedPhone = getPhone(context) ?: ""
    val storedId = getUserId(context) ?: -1

    var username by rememberSaveable { mutableStateOf(storedUsername) }
    var firstname by rememberSaveable { mutableStateOf(storedFirstname) }
    var lastname by rememberSaveable { mutableStateOf(storedLastname) }
    var phone by rememberSaveable { mutableStateOf("") }
    var userId by rememberSaveable { mutableStateOf(storedId) }
    var secondaryPhone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 44.dp, topEnd = 44.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        UserInputCard(
            label = "Your phone number",
            placeholder = "Enter your phone number",
            icon = Icons.Default.Call,
            value = phone,
            onValueChange = { phone = it }
        )
        Spacer(modifier = Modifier.height(28.dp))

        UserInputCard(
            label = "Your secondary phone",
            placeholder = "Enter your secondary number",
            icon = Icons.Default.Call,
            value = secondaryPhone,
            onValueChange = {
                secondaryPhone = it
            }
        )

        Spacer(modifier = Modifier.weight(1f))
        ContinueButton(navController, username, phone, context, userId, firstname, lastname, secondaryPhone)
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputCard(
    label: String,
    placeholder: String,
    icon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(label, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                placeholder = { Text(placeholder) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = redcolor,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = redcolor
                )
            )
        }
    }
}


fun postUserInfo(context: Context, username: String, phone: String,  callback: ApiCallback) {
    val client = OkHttpClient()
    val url = TestApi.Post_UserInfo
    val jsonObject = JSONObject().apply {
        put("name", username)
        put("number", phone)
    }

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val body = jsonObject.toString().toRequestBody(mediaType)

    val token = getToken(context)
    Log.d("Token","Token in slot $token")
    val requestBuilder = Request.Builder()
        .url(url)
        .post(body)

    token?.let {
        requestBuilder.addHeader("Authorization", "Bearer $it")
    }

    val request = requestBuilder.build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("API_Call", "Failed to send request: ${e.message}")
            callback.onFailure(e.message ?: "Unknown error")
        }
        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string() ?: ""
            if (response.isSuccessful) {
                Log.d("API_Call", "Response: $responseBody")
                callback.onSuccess(responseBody)
            } else {
                val errorMsg = "Error: ${response.code}"
                Log.e("API_Call", errorMsg)
                callback.onFailure(errorMsg)
            }
        }
    })
}
@Composable
fun ContinueButton(navController: NavController,
                   username: String,
                   phone: String,
                   context: Context,
                   userId: Int,
                   firstName: String,
                   lastName: String,
                   secondaryPhone: String
) {
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            /*scope.launch {
                withContext(Dispatchers.IO) {
                    postUserInfo(context, username, phone, object : ApiCallback {
                        override fun onSuccess(response: String) {

                            saveToken(context, getToken(context) ?: "", username, phone, userId, firstName, lastName)

                            try {
                                val expertsResponse = makeOkHttpRequest(TestApi.Get_Inspector)
                                Log.d("API_RESPONSE", "Response: $expertsResponse")
                                val experts: List<Expert> = Gson().fromJson(
                                    expertsResponse,
                                    object : TypeToken<List<Expert>>() {}.type
                                )

                                val json = Uri.encode(Gson().toJson(experts))
                                (context as? Activity)?.runOnUiThread {
                                    navController.navigate("bookexpertvisit/$json")
                                }
                            } catch (e: Exception) {
                                Log.e("API_ERROR", "GET request failed: ${e.message}")
                                (context as? Activity)?.runOnUiThread {
                                    Toast.makeText(context, "GET request failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onFailure(error: String) {
                            scope.launch(Dispatchers.Main) {
                                Toast.makeText(context, "POST API call failed: $error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }*/
            scope.launch {
                withContext(Dispatchers.IO) {
                    // Save data locally
                    saveAndLogFormData(
                        selectedOption = "Self",
                        inputFields = emptyMap(),
                        context = context,
                        imageUrls = emptyList(),
                        primaryPhone = phone,
                        secondaryPhone = secondaryPhone
                    )

                    try {
                        // Now make the GET request and navigate
                        val expertsResponse = makeOkHttpRequest(TestApi.Get_Inspector)
                        Log.d("API_RESPONSE", "Response: $expertsResponse")

                        val experts: List<Expert> = Gson().fromJson(
                            expertsResponse,
                            object : TypeToken<List<Expert>>() {}.type
                        )

                        val json = Uri.encode(Gson().toJson(experts))
                        (context as? Activity)?.runOnUiThread {
                            navController.navigate("bookexpertvisit/$json")
                        }

                    } catch (e: Exception) {
                        Log.e("API_ERROR", "GET request failed: ${e.message}")
                        (context as? Activity)?.runOnUiThread {
                            Toast.makeText(
                                context,
                                "GET request failed: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(containerColor = redcolor)
    ) {
        Text("Continue", color = Color.White, fontSize = 16.sp)
    }
}

fun makeOkHttpRequest(url: String): String {
    return try {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        response.body?.string() ?: "No Response"
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}

data class Expert(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val phone_number: String
)


@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
    val navController = rememberNavController()
    StepProgressIndicators(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        stepCount = 3,
        currentStep = 1, // Change this based on your actual step
        titles = listOf("Car Detail", "User Detail", "Inspector Detail"),
        onStepClicked = {  }
    )
}
