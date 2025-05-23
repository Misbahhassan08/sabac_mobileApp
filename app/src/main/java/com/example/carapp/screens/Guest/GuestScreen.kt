package com.example.carapp.screens.Guest

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carapp.Apis.ApiCallback
import com.example.carapp.Apis.TestApi
import com.example.carapp.assets.redcolor
import com.example.carapp.screens.UserInfoForm
import com.example.carapp.screens.getPhone
import com.example.carapp.screens.getToken
import com.example.carapp.screens.getUserId
import com.example.carapp.screens.getUsername
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

//**************** THIS IS NOT USING ANYMORE ////
@Composable
fun GuestScreen(navController: NavController) {
    val systemUiController = rememberSystemUiController()

    systemUiController.isStatusBarVisible = false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background( redcolor)
    ) {
        HeaderSectio()
        UserInfoFor(navController)
    }
}

@Composable
fun HeaderSectio() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                redcolor
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
//            StepProgressIndicator()
            Spacer(modifier = Modifier.height(25.dp))
            Text("Let's get you started", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun UserInfoFor(navController: NavController) {
    val context = LocalContext.current

    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 44.dp, topEnd = 44.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        UserInputCar(
            label = "Your full name",
            placeholder = "Enter your full name",
            icon = Icons.Default.Person,
            value = username,
            onValueChange = { username = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        UserInputCar(
            label = "Your Email",
            placeholder = "Enter your Email",
            icon = Icons.Default.Call,
            value = email,
            onValueChange = { email = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        UserInputCar(
            label = "Your phone number",
            placeholder = "Enter your phone number",
            icon = Icons.Default.Call,
            value = phone,
            onValueChange = { phone = it }
        )

        Spacer(modifier = Modifier.weight(1f))
        ContinueButto(navController, username, email, phone, context)
        Spacer(modifier = Modifier.height(35.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputCar(
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


fun postUserInf(context: Context, username: String, email: String,phone: String, callback: ApiCallback) {
    val client = OkHttpClient()
    val url = TestApi.post_guest_details
    val jsonObject = JSONObject().apply {
        put("name", username)
        put("email", email)
        put("number", phone)
    }

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val body = jsonObject.toString().toRequestBody(mediaType)


    val requestBuilder = Request.Builder()
        .url(url)
        .post(body)


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

/*
@Composable
fun ContinueButto(navController: NavController, username: String, email: String,phone: String, context: Context) {
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            scope.launch {
                withContext(Dispatchers.IO) {
                    postUserInf(context, username, email, phone, object : ApiCallback {
                        override fun onSuccess(response: String) {
                            try {
                                val expertsResponse = makeOkHttpReques(TestApi.Get_Inspector)
                                Log.d("API_RESPONSE", "Response: $expertsResponse")
                                val experts: List<Expert> = Gson().fromJson(
                                    expertsResponse,
                                    object : TypeToken<List<Expert>>() {}.type
                                )

                                val json = Uri.encode(Gson().toJson(experts))
                                (context as? Activity)?.runOnUiThread {
                                    navController.navigate("CallExpert/$json")
//                                    navController.navigate("post/$json")
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
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C72E8))
    ) {
        Text("Continue", color = Color.White, fontSize = 16.sp)
    }
}
*/

@Composable
fun ContinueButto(navController: NavController, username: String, email: String, phone: String, context: Context) {
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            scope.launch {
                withContext(Dispatchers.IO) {
                    postUserInf(context, username, email, phone, object : ApiCallback {
                        override fun onSuccess(response: String) {
                            try {
                                Log.d("API_Response", "POST Response: $response")
                                val jsonObject = JSONObject(response)
                                val guestId = jsonObject.optInt("guest_id", -1)

                                if (guestId != -1) {
                                    (context as? Activity)?.runOnUiThread {
                                        navController.navigate("guest/$guestId")
                                    }
                                } else {
                                    Log.e("API_ERROR", "guest_id not found in response")
                                }
                            } catch (e: Exception) {
                                Log.e("API_ERROR", "Error parsing response: ${e.message}")
                            }
                        }

                        override fun onFailure(error: String) {
                            scope.launch(Dispatchers.Main) {
                                Toast.makeText(context, "POST API call failed: $error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
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


fun makeOkHttpReques(url: String): String {
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
    com.example.carapp.screens.BasicInfoScreen(navController)
}
