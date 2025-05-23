package com.example.carapp.screens

import android.content.Intent
import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.assets.redcolor
import com.example.carapp.models.CarListViewModel
import com.example.carapp.models.SignUpViewModel
import com.example.carapp.screens.Admin.EnhancedTextField
import com.example.carapp.screens.Admin.isValidEmail
import com.example.carapp.screens.Admin.isValidPhoneNumber
import com.example.carapp.screens.Admin.registerUserA
import com.example.carapp.ui.theme.lexendFont
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
fun RegisterScreen(
    navController: NavController,
//    viewModel: SignUpViewModel = hiltViewModel()
) {
   /* var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phonenumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
//    val state = viewModel.signUpState.collectAsState(initial = null)

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

            Spacer(modifier = Modifier.height(8.dp))*/
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phonenumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Add error states for each field
    var firstNameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var phoneNumberError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }

    var apiErrorMessage by remember { mutableStateOf<String?>(null) }
    var emailFormatError by remember { mutableStateOf(false) }
    var phoneFormatError by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    val roles = listOf("dealer", "inspector", "admin")
    var selectedRole by remember { mutableStateOf(roles.first()) }
    val coroutineScope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Show API error message if exists
    apiErrorMessage?.let { message ->
        LaunchedEffect(message) {
//            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            apiErrorMessage = null
        }
    }

    Box(
        modifier = Modifier.background(redcolor)
    ) {
        Image(
            painter = painterResource(id = R.drawable.skew),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "Logo",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Create Your Account",
                    style = MaterialTheme.typography.headlineMedium,
//                color = Color(0xFF2C3E50),
                    color = Color.White,
                    fontFamily = lexendFont,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Form Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp) // Fixed spacing
            ) {
                // Name Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    EnhancedTextFieldS(
                        label = "First Name *",
                        value = firstname,
                        onValueChange = {
                            firstname = it
                            firstNameError = it.isBlank()
                        },
                        modifier = Modifier.weight(1f),
                        leadingIcon = Icons.Default.Person,
                        isError = firstNameError,
                        errorMessage = if (firstNameError) "First name is required" else null
                    )

                    EnhancedTextFieldS(
                        label = "Last Name *",
                        value = lastname,
                        onValueChange = {
                            lastname = it
                            lastNameError = it.isBlank()
                        },
                        modifier = Modifier.weight(1f),
                        leadingIcon = Icons.Default.Person,
                        isError = lastNameError,
                        errorMessage = if (lastNameError) "Last name is required" else null
                    )
                }

                EnhancedTextFieldS(
                    label = "Username *",
                    value = username,
                    onValueChange = {
                        username = it
                        usernameError = it.isBlank()
                    },
                    leadingIcon = Icons.Default.AccountCircle,
                    isError = usernameError,
                    errorMessage = if (usernameError) "Username is required" else null
                )

                EnhancedTextFieldS(
                    label = "Email *",
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = it.isBlank()
                        emailFormatError = it.isNotBlank() && !isValidEmail(it)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    leadingIcon = Icons.Default.Email,
                    isError = emailError || emailFormatError,
                    errorMessage = when {
                        emailError -> "Email is required"
                        emailFormatError -> "Please enter a valid email"
                        else -> null
                    }
                )

                EnhancedTextFieldS(
                    label = "Password *",
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = it.isBlank()
                    },
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        val image = if (passwordVisible) {
                            Icons.Default.Lock
                        } else {
                            Icons.Default.Lock
                        }
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription = description,
                                tint = Color(0xFF7F8C8D)
                            )
                        }
                    },
                    leadingIcon = Icons.Default.Lock,
                    isError = passwordError,
                    errorMessage = if (passwordError) "Password is required" else null
                )

                EnhancedTextFieldS(
                    label = "Phone Number *",
                    value = phonenumber,
                    onValueChange = {
                        phonenumber = it
                        phoneNumberError = it.isBlank()
                        phoneFormatError = it.isNotBlank() && !isValidPhoneNumber(it)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = Icons.Default.Phone,
                    isError = phoneNumberError || phoneFormatError,
                    errorMessage = when {
                        phoneNumberError -> "Phone number is required"
                        phoneFormatError -> "Please enter 10-15 digits"
                        else -> null
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
//                    coroutineScope.launch {
//                        isLoading = true
//                        val response = registerUser(
//                            context,navController,firstname, lastname, username, email, password, phonenumber
//                        )
//                        isLoading = false
////                        Toast.makeText(context, response, Toast.LENGTH_LONG).show()
//                    }
//                },
                        Log.d("btn", "button Clicked.........")
                        firstNameError = firstname.isBlank()
                        lastNameError = lastname.isBlank()
                        usernameError = username.isBlank()
                        emailError = email.isBlank()
                        emailFormatError = email.isNotBlank() && !isValidEmail(email)
                        passwordError = password.isBlank()
                        phoneNumberError = phonenumber.isBlank()
                        phoneFormatError =
                            phonenumber.isNotBlank() && !isValidPhoneNumber(phonenumber)
                        addressError = address.isBlank()
                        Log.d("btn", "button Clicked..2.......")
                        val isValid = !firstNameError && !lastNameError && !usernameError &&
                                !emailError && !emailFormatError && !passwordError &&
                                !phoneNumberError && !phoneFormatError
                        Log.d("btn", "button Clicked....3....")
                        if (isValid) {
                            Log.d("btn", "button Clicked....4.....")
                            coroutineScope.launch {
                                Log.d("btn", "button Clicked.....6....")
                                isLoading = true
                                val result = registerUser(
                                    context,
                                    navController,
                                    firstname,
                                    lastname,
                                    username,
                                    email,
                                    password,
                                    phonenumber
                                )
//                                .also { response ->
//                                Log.d("RegisterScreen", "API Response: $response")
//                                if (response.startsWith("Error:")) {
//                                    apiErrorMessage = response.removePrefix("Error: ")
//                                } else {
//                                    onRegistrationSuccess()
//                                }
//                            }
                                isLoading = false
                            }
                        } else {
                            apiErrorMessage = "Please fill all required fields"
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
                        CircularProgressIndicator(
                            color = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text("Sign Up", fontSize = 16.sp)
                    }
                }
//            //ERROR MESSAGE TEXT
//            Spacer(modifier = Modifier.height(8.dp))
////            apiErrorMessage?.let {
////                Log.d("err","ERROR***** $it")
//                Text(
//                    text = "g", color = Color.Red
//                )
////            }

                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.align(Alignment.Start)) {
                    Text(
                        text = "Already a user?",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
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
}

suspend fun registerUser(
    context: android.content.Context,
    navController: NavController,
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
//                Log.e("RegisterAPI", "Error Response: $responseBody")
//
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(context, "Registration Failed!", Toast.LENGTH_SHORT).show()
//                }
//
//                "Error: ${response.code} - $responseBody"
                val errorMessageR = try {
                    val errorJson = JSONObject(responseBody)
                    errorJson.optString("message", "Unknown error occurred")
                } catch (e: Exception) {
                    Log.e("RegisterAPI", "Error parsing error response", e)
                    "Unknown error occurred"
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Registration Failed: $errorMessageR", Toast.LENGTH_LONG).show()
                }
                // Return the extracted message for reuse elsewhere
                "Error: $errorMessageR"
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTextFieldS(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isError) MaterialTheme.colorScheme.error else Color(0xFF7F8C8D)
                )
            },
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon?.let {
                @Composable {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = if (isError) MaterialTheme.colorScheme.error else Color(0xFF7F8C8D)
                    )
                }
            },
            isError = isError,
            trailingIcon = trailingIcon,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = if (singleLine) 56.dp else 80.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                // Container colors
                containerColor = Color.White,
//                unfocusedContainerColor = Color.White,
                errorContainerColor = Color.White,

                // Text colors
//                textColor = Color(0xFF2C3E50),
                unfocusedTextColor = Color(0xFF2C3E50),
                errorTextColor = Color(0xFF2C3E50),

                // Label colors
                focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color(0xFF2C3E50),
                unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color(0xFF7F8C8D),

                // Border colors
                focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color(0xFF2C3E50),
                unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color(0xFFBDC3C7),
                errorBorderColor = MaterialTheme.colorScheme.error,

                // Cursor color
                cursorColor = Color(0xFF2C3E50),

                // Placeholder color
//                placeholderColor = Color(0xFF7F8C8D),
                unfocusedPlaceholderColor = Color(0xFF7F8C8D)
            )
        )

        // Error message with fixed height to prevent layout shifts
        Box(modifier = Modifier.height(20.dp)) {
            if (isError && errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
    }
}
@Preview(showBackground = true, widthDp = 309, heightDp = 675)
@Composable
fun RegisterPreview() {
    val navController = rememberNavController()
    RegisterScreen(navController)
}
