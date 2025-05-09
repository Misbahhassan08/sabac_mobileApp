package com.example.carapp.screens

import android.content.Intent
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


//@Composable
//fun RegisterScreen(navController: NavController) {
//    var firstname by remember { mutableStateOf("") }
//    var lastname by remember { mutableStateOf("") }
//    var username by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var phonenumber by remember { mutableStateOf("") }
//    var isLoading by remember { mutableStateOf(false) }
//
//    val coroutineScope = rememberCoroutineScope()
//
//    val context = LocalContext.current
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF2C2B34)) // Dark background
//            .padding(26.dp)
//            .verticalScroll(rememberScrollState())
//    ) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.car1),
//                contentDescription = "Cars",
//                modifier = Modifier.size(150.dp)
//            )
//            Text(
//                text = "Join Us",
//                color = Color.White,
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(bottom = 8.dp).align(Alignment.Start)
//            )
//            TextInput(name = "First Name", value = firstname, onChange = { firstname = it })
//            TextInput(name = "Last Name", value = lastname, onChange = { lastname = it })
//            TextInput(name = "Username", value = username, onChange = { username = it })
//            TextInput(name = "Email", value = email, onChange = { email = it })
//            TextInput(name = "Password", value = password, onChange = { password = it })
//            TextInput(name = "Phone Number", value = phonenumber, onChange = { phonenumber = it })
//            Spacer(modifier = Modifier.height(8.dp))
//            Button(
//                onClick = {
//
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF9ED90D),
//                    contentColor = Color.Black
//                ),
//                shape = RoundedCornerShape(8.dp)
//            ) {
//                Text("Sign Up", fontSize = 16.sp)
//            }
//            Spacer(modifier = Modifier.height(10.dp))
//            Row(
//                modifier = Modifier.align(Alignment.Start)
//            ) {
//                Text(text = "Already a user?",
//                    fontSize = 12.sp, fontWeight = FontWeight.Normal, color = Color.White)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(text = "Log In",
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = Color.White,
//                    textDecoration = TextDecoration.Underline,
//                    modifier = Modifier.clickable { navController.navigate("login") }
//                )
//            }
////            Spacer(modifier = Modifier.height(3.dp))
//            Row(
//                modifier = Modifier.padding(4.dp),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                HorizontalDivider( modifier = Modifier.weight(1f).height(1.dp), color = Color(0x80FFFFFF))
//                Spacer(modifier = Modifier.width(2.dp))
//                Text(
//                    text = "Or",
//                    color = Color.Gray
//                )
//                Spacer(modifier = Modifier.width(2.dp))
//                HorizontalDivider( modifier = Modifier.weight(1f).height(1.dp), color = Color(0x80FFFFFF))
//            }
//            Spacer(modifier = Modifier.height(10.dp))
//            Button(
//                onClick = {
//                    coroutineScope.launch {
//                        isLoading = true
//                        val response = registerUser(
//                            context,firstname, lastname, username, email, password, phonenumber
//                        )
//                        isLoading = false
//                        Toast.makeText(context, response, Toast.LENGTH_LONG).show()
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.White,
//                    contentColor = Color.Black
//                ),
//                shape = RoundedCornerShape(8.dp)
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Image(
//                        painter = painterResource(id = R.drawable.google),
//                        contentDescription = "Google Logo",
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Continue with Google", fontSize = 16.sp)
//                }
//            }
//            Spacer(modifier = Modifier.height(12.dp))
//            Text(
//                text = "Joining our app means you agree with our",
//                color = Color.Gray,
//                fontSize = 12.sp
//            )
//            Row {
//                Text(
//                    text = "Terms of use",
//                    color = Color(0xFF9ED90D),
//                    fontSize = 12.sp,
//                    modifier = Modifier.clickable { /* click */ }
//                )
//                Spacer(modifier = Modifier.width(4.dp))
//                Text(
//                    text = "and",
//                    color = Color.Gray,
//                    fontSize = 12.sp
//                )
//                Spacer(modifier = Modifier.width(4.dp))
//                Text(
//                    text = "Privacy Policy",
//                    color = Color(0xFF9ED90D),
//                    fontSize = 12.sp,
//                    modifier = Modifier.clickable { /* Handle privacy click */ }
//                )
//            }
//            Spacer(modifier = Modifier.height(36.dp))
//        }
//    }
//}

@Composable
fun RegisterScreen(navController: NavController) {
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phonenumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(redcolor)
            .padding(26.dp)
            .verticalScroll(rememberScrollState()) // Enable scrolling
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.car1),
                contentDescription = "Cars",
                modifier = Modifier.size(150.dp)
            )
            Text(
                text = "Join Us",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp).align(Alignment.Start)
            )
            TextInput(name = "First Name", value = firstname, onChange = { firstname = it })
            TextInput(name = "Last Name", value = lastname, onChange = { lastname = it })
            TextInput(name = "Username", value = username, onChange = { username = it })
            TextInput(name = "Email", value = email, onChange = { email = it })
            TextInput(name = "Password", value = password, onChange = { password = it })
            TextInput(name = "Phone Number", value = phonenumber, onChange = { phonenumber = it })

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        val response = registerUser(
                            context,navController,firstname, lastname, username, email, password, phonenumber
                        )
                        isLoading = false
//                        Toast.makeText(context, response, Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp))
                } else {
                    Text("Sign Up", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.align(Alignment.Start)) {
                Text(text = "Already a user?", fontSize = 12.sp, fontWeight = FontWeight.Normal, color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Log In",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { navController.navigate("login") }
                )
            }
        }
    }
}

suspend fun registerUser(
    context: android.content.Context, // Pass Context to show Toast
    navController: NavController,    // Added NavController for navigation
    firstname: String,
    lastname: String,
    username: String,
    email: String,
    password: String,
    phonenumber: String
): String {
    return withContext(Dispatchers.IO) {
        try {
            Log.d("RegisterAPI", "Starting API call...")

            val client = OkHttpClient.Builder()
                .followRedirects(false) // Disable automatic redirects
                .followSslRedirects(false)
                .build()

            val json = JSONObject().apply {
                put("first_name", firstname)
                put("last_name", lastname)
                put("username", username)
                put("email", email)
                put("password", password)
                put("phone_number", phonenumber)
            }

            val jsonString = json.toString()
            Log.d("RegisterAPI", "JSON Payload: $jsonString")

            val requestBody = jsonString.toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(TestApi.Get_Register)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()

            Log.d("RegisterAPI", "Sending request to API...")

            val response = client.newCall(request).execute()

            Log.d("RegisterAPI", "Received response with code: ${response.code}")

            val responseBody = response.body?.string() ?: "No Response"
            Log.d("RegisterAPI", "Response Body: $responseBody")

            if (response.isSuccessful) {
                Log.d("RegisterAPI", "Registration Successful")

                // Ensure response is JSON before parsing
                val jsonResponse = try {
                    JSONObject(responseBody)
                } catch (e: Exception) {
                    Log.e("RegisterAPI", "Invalid JSON response", e)
                    null
                }

                // Show success Toast on Main Thread
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                    navController.navigate("login")
                }

                jsonResponse?.toString() ?: "Success"
            } else {
                Log.e("RegisterAPI", "Error Response: $responseBody")

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Registration Failed!", Toast.LENGTH_SHORT).show()
                }

                "Error: ${response.code} - $responseBody"
            }
        } catch (e: Exception) {
            Log.e("RegisterAPI", "Exception: ${e.localizedMessage}", e)

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }

            "Error: ${e.localizedMessage}"
        }
    }
}


@Composable
fun TextInput(name: String, value: String, onChange: (String) -> Unit) {

    OutlinedTextField(
        value = value,
        onValueChange =  onChange ,
        label = { Text(text = name, color = Color.White) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color(0xFF434A53),
            focusedIndicatorColor = Color(0xFF9ED90D)
        )
    )
}

@Preview(showBackground = true, widthDp = 309, heightDp = 675)
@Composable
fun RegisterPreview() {
    val navController = rememberNavController()
    RegisterScreen(navController)
}
