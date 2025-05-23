package com.example.carapp.screens.Guest

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carapp.Apis.ApiCallback
import com.example.carapp.Apis.TestApi
import com.example.carapp.assets.seller_Color
import com.example.carapp.screens.CustomAlertDialog
import com.example.carapp.screens.getFirstName
import com.example.carapp.screens.getLastName
import com.example.carapp.screens.getPhone
import com.example.carapp.screens.getToken
import com.example.carapp.screens.getUserId
import com.example.carapp.screens.getUsername
import com.example.carapp.screens.saveAndLogFormData
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestBasicInfoScreen(navController: NavController) {
    val systemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = false

    var showDialog by rememberSaveable { mutableStateOf(false) }

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
                modifier = Modifier
                    .background(seller_Color)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "User Details",
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
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
            // Step Progress Bar under TopBar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                StepProgressIndicatorsG(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 7.dp, end = 7.dp, top = 32.dp, bottom = 4.dp),
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
            UserInfoFormG(navController)
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
fun StepProgressIndicatorsG(
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
                            color = if (index <= 1) seller_Color
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
                            else Color.Gray // Other lines remain gray
                        )
                )
            }
        }
    }
}


@Composable
fun UserInfoFormG(navController: NavController) {
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
    var email by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var userId by rememberSaveable { mutableStateOf(storedId) }
    var secondaryPhone by remember { mutableStateOf("") }

    var nameError by rememberSaveable { mutableStateOf(false) }
    var phoneError by rememberSaveable { mutableStateOf(false) }
    var emailError by rememberSaveable { mutableStateOf(false) }

    val emailRegex = remember { Regex( "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,3}\$") }
    val phoneRegex = remember { Regex( "^[0-9]{10,15}\$" ) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 44.dp, topEnd = 44.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        UserInputCardG(
            label = "Name",
            placeholder = "Enter your name",
            icon = Icons.Default.Person,
            value = name,
            onValueChange = {
                name = it
                nameError = false // Reset error when user types
            },
            isError = nameError,
            errorMessage = "Name cannot be empty"
        )
        Spacer(modifier = Modifier.height(28.dp))

        UserInputCardG(
            label = "Phone number",
            placeholder = "Enter your Phone number",
            icon = Icons.Default.Call,
            value = phone,
            onValueChange = {
                phone = it
                phoneError = false
            },
            isError = phoneError,
            errorMessage = if (phone.isEmpty()) "Phone cannot be empty" else "Invalid phone number",
            keyboardType = KeyboardType.Phone
        )

        Spacer(modifier = Modifier.height(28.dp))

        UserInputCardG(
            label = "Email",
            placeholder = "Enter your email",
            icon = Icons.Default.Email,
            value = email,
            onValueChange = {
                email = it
                emailError = false
            },
            isError = emailError,
            errorMessage = if (email.isEmpty()) "Email cannot be empty" else "Invalid email format"
        )

        Spacer(modifier = Modifier.weight(1f))


        val isValidForm = !nameError && !phoneError && !emailError

        ContinueButtonG(
            navController = navController,
            username = username,
            phone = phone,
            context = LocalContext.current,
            userId = userId,
            firstName = firstname,
            lastName = lastname,
            secondaryPhone = secondaryPhone,
            onValidate = {
                nameError = name.isEmpty()
                phoneError = phone.isEmpty() || !phone.matches(phoneRegex)
                emailError = email.isEmpty() || !email.matches(emailRegex)
                !nameError && !phoneError && !emailError
            },
            isValidForm = isValidForm
        )
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputCardG(
    label: String,
    placeholder: String,
    icon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String = "",
    keyboardType: KeyboardType = KeyboardType.Text
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
                    focusedBorderColor = if (isError) Color.Red else seller_Color,
                    unfocusedBorderColor = if (isError) Color.Red else Color.Gray,
                    cursorColor = seller_Color
                ),
                isError = isError,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
            )
            if (isError) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }
        }
    }
}

fun postUserInfoG(context: Context, username: String, phone: String, callback: ApiCallback) {
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
fun ContinueButtonG(
    navController: NavController,
    username: String,
    phone: String,
    context: Context,
    userId: Int,
    firstName: String,
    lastName: String,
    secondaryPhone: String,
    onValidate: () -> Boolean,
    isValidForm: Boolean
) {
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            if (onValidate()) {
                scope.launch {
                    withContext(Dispatchers.IO) {
                        // Save data locally
                        saveAndLogFormDataG(
                            selectedOption = "Self",
                            inputFields = emptyMap(),
                            context = context,
                            imageUrls = emptyList(),
                            primaryPhone = phone,
                            secondaryPhone = secondaryPhone
                        )

                        try {
                            val expertsResponse = makeOkHttpRequestG(TestApi.Get_Inspector)
                            Log.d("API_RESPONSE", "Response: $expertsResponse")

                            val experts: List<ExpertG> = Gson().fromJson(
                                expertsResponse,
                                object : TypeToken<List<ExpertG>>() {}.type
                            )

                            val json = Uri.encode(Gson().toJson(experts))
                            (context as? Activity)?.runOnUiThread {
                                navController.navigate("Guestbookexpertvisit/$json")
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
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isValidForm) seller_Color else seller_Color.copy(alpha = 0.5f)
        ),
        enabled = isValidForm
    ) {
        Text("Continue", color = Color.White, fontSize = 16.sp)
    }
}
fun makeOkHttpRequestG(url: String): String {
    return try {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        response.body?.string() ?: "No Response"
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}

data class ExpertG(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val phone_number: String
)

