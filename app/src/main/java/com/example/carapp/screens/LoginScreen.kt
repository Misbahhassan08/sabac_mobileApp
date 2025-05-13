package com.example.carapp.screens

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.models.AuthState
import com.example.carapp.viewmodels.GAuthViewModel
import com.example.carapp.viewmodels.GoogleViewModel
import com.example.carapp.viewmodels.LoginViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


/*@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("seller@sabac.com") }
    var password by remember { mutableStateOf("123456789") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

//    val gAuthViewModel: GAuthViewModel = hiltViewModel()
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        val account = gAuthViewModel.handleGoogleSignIn(result.data)
//        Log.d("LoginScreen", "Result: $account")
//    }

    val googleAuth: GoogleViewModel = viewModel()

    val loginViewModel: LoginViewModel = hiltViewModel()

    val authState by loginViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
    when (authState) {
        is AuthState.Loading -> {}
        is AuthState.Success -> {
            val role = (authState as AuthState.Success).role
            if (role == "seller") {
                navController.navigate("seller")
            }
        }
        is AuthState.Error -> {
            Log.e(TAG, "Login Error: ${(authState as AuthState.Error).message}")
        }

        is AuthState.Idle -> {}
    }
}
    Box(
        modifier = Modifier.background(Color(0xFF2C2B34))
    ) {

        Image(
            painter = painterResource(id = R.drawable.skew),
            contentDescription = null,
//            modifier = Modifier.fillMaxWidth()
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.car3),
                contentDescription = "car",
                modifier = Modifier.height(150.dp).fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Selling Point", fontWeight = FontWeight.Bold, fontSize = 32.sp, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Travel love a car", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(26.dp))
            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                placeholder = {
                    Text(text = "Email")
                },
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(28.dp)).height(58.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF9ED90D)
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                placeholder = {
                    Text(text = "Password")
                },
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(28.dp)).height(58.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF9ED90D)
                ),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    loginViewModel.loginUser(email, password)
                    if(email == "seller@sabac.com"){
                        navController.navigate("seller")
                    }
                    if(email == "inspection@sabac.com" ) {
                        navController.navigate("inspection")
                    }
                    if(email == "admin@sabac.com") {
                        navController.navigate("admin")
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9ED90D),
                    contentColor = Color.Black
                )
            ) {
                Text(text = "LOG IN", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider( modifier = Modifier.weight(1f).height(1.dp), color = Color(0x80FFFFFF))
                IconButton( modifier = Modifier.background(color = Color.White, shape = CircleShape),
                    onClick = {
//                        launcher.launch(googleViewModel.googleSignInClient.signInIntent)
//                        launcher.launch(gAuthViewModel.getGoogleSignInIntent(context))
                        googleAuth.initiateGoogleSignIn(context, coroutineScope)
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Sign in using Google",
                        modifier = Modifier.size(30.dp),
                        tint = Color.Unspecified
                    )
                }
                HorizontalDivider( modifier = Modifier.weight(1f).height(1.dp), color = Color(0x80FFFFFF))
            }
            Row {
                Text(text = "Don't have an account?",
                    fontSize = 12.sp, fontWeight = FontWeight.Normal, color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Sign Up",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { navController.navigate("registration") }
                )
            }
            Spacer(modifier = Modifier.height(54.dp))
        }
    }
}*/

@Composable
fun LoginScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.background(redcolor)
    ) {
        Image(
            painter = painterResource(id = R.drawable.skew),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.car3),
                contentDescription = "car",
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Selling Point",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Travel love a car",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(26.dp))
            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(text = "Email")
                },
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(28.dp))
                    .height(58.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = redcolor
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = {
                    Text(text = "Password")
                },
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(28.dp))
                    .height(58.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = redcolor
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            /*Button(
                onClick = {
                    coroutineScope.launch {
                        val success = loginUser(context, email, password, navController)
                        if (!success) {
                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor =Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = "LOG IN", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }*/
            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        val success = loginUser(context, email, password, navController)
                        isLoading = false
                        if (!success) {
                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(text = "LOG IN", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }
          Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Continue as Guest",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable {
                        navController.navigate("post")
                    }
                    .padding(8.dp)
            )

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f).height(1.dp),
                    color = Color(0x80FFFFFF)
                )
                IconButton(
                    modifier = Modifier.background(color = Color.White, shape = CircleShape),
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.car2),
                        contentDescription = "Sign in using Google",
                        modifier = Modifier.size(30.dp),
                        tint = Color.Unspecified
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.weight(1f).height(1.dp),
                    color = Color(0x80FFFFFF)
                )
            }
            Row {
                Text(
                    text = "Don't have an account?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign Up",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { navController.navigate("registration") }
                )
            }
            Spacer(modifier = Modifier.height(54.dp))
        }
    }
}


suspend fun loginUser(context: Context, email: String, password: String, navController: NavController): Boolean {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val url = TestApi.Get_Login
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        Log.d("device_id","****** $deviceId")


        val jsonObject = JSONObject().apply {
            put("username_or_email", email)
            put("password", password)
            put("device_id", "device-$deviceId")
        }

        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: "No response body"

            Log.d("LoginAPI", "Response Code: ${response.code}")
            Log.d("LoginAPI", "Response Body222: $responseBody")

            if (response.isSuccessful) {
                val jsonResponse = JSONObject(responseBody)
                val userObject = jsonResponse.optJSONObject("user")
                val accessToken = jsonResponse.optString("access_token", "")
                Log.d("TOKENN", "ACCC $accessToken ")
                val refreshToken = jsonResponse.optString("refresh_token", "")
                Log.d("TOKENN", "REFRESH $refreshToken ")
                val username = userObject?.optString("username", "") ?: ""
                val phone = userObject?.optString("phone_number", "") ?: ""
                Log.d("use", "Extracted Username: $username")
                val userId = jsonResponse.optJSONObject("user")?.optInt("id", -1) ?: -1
                val role = jsonResponse.optJSONObject("user")?.optString("role", "") ?: ""
                val firstName = userObject?.optString("first_name", "") ?: ""
                val lastName = userObject?.optString("last_name", "") ?: ""

                Log.d("USER_ROLE", "User Role: $role")

                Log.d("USER_ID", "User ID: $userId")
                if (accessToken.isNotEmpty()) {
                    saveToken(context, accessToken, username, phone, userId, firstName, lastName, refreshToken)

                    withContext(Dispatchers.Main) {
                        when (role) {
                            "inspector" -> navController.navigate("inspector")
                            "saler" -> navController.navigate("seller")
                            "dealer" -> navController.navigate("dealer")
                            "admin" -> navController.navigate("admin")
                        }
                    }
                    return@withContext true
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return@withContext false
    }
}

fun saveToken(
    context: Context,
    token: String,
    username: String,
    phone: String,
    userId: Int,
    firstName: String,
    lastName: String,
    refreshToken: String,
) {
    Log.d("SaveToken", "Saving -> Token: $token, Username: $username, Phone: $phone, UserId: $userId")

    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("access_token", token)
        putString("username", username)
        putString("phone", phone)
        putInt("user_id", userId)
        putString("first_name", firstName)
        putString("last_name", lastName)
        putString("refresh_token", refreshToken)
        apply()
    }
}

fun getToken(context: Context): String? {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("access_token", null)
}
fun getRefreshToken(context: Context): String? {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("refresh_token", null)
}
fun clearTokenData(context: Context) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        clear()
        apply()
    }
    Log.d("Logout", "Token data cleared from SharedPreferences")
}

fun getUsername(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    val username = sharedPreferences.getString("username", null)
    Log.d("GetUsername", "Retrieved username: $username")
    return username
}
fun getFirstName(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    val firstName = sharedPreferences.getString("first_name", "NOT FOUND")
    Log.d("GetFirstName", "Retrieved first name: $firstName")
    return firstName
}

fun getLastName(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    val lastName = sharedPreferences.getString("last_name", "NOT FOUND")
    Log.d("GetLastName", "Retrieved last name: $lastName")
    return lastName
}

fun getPhone(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("phone", null)
}

fun getUserId(context: Context): Int {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("user_id", -1)
}

@Preview(showBackground = true, widthDp = 309, heightDp = 675)
@Composable
fun LoginPreview() {
    val navController = rememberNavController()
    LoginScreen(navController)
}
