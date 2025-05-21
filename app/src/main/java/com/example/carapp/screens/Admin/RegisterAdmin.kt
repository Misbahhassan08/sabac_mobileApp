package com.example.carapp.screens.Admin

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.screens.getToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


/*@Composable
fun RegisterAdminScreen(
    navController: NavController,
    onRegistrationSuccess: () -> Unit
) {
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phonenumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Error States
    var firstNameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var phoneNumberError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    val roles = listOf("dealer", "inspector", "admin")
    var selectedRole by remember { mutableStateOf(roles.first()) }
    val coroutineScope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
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
                painter = painterResource(id = R.drawable.car1),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Create Admin Account",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF2C3E50),
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Name Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EnhancedTextField(
                    label = "First Name",
                    value = firstname,
                    onValueChange = { firstname = it },
                    modifier = Modifier.weight(1f),
                    leadingIcon = Icons.Default.Person
                )

                EnhancedTextField(
                    label = "Last Name",
                    value = lastname,
                    onValueChange = { lastname = it },
                    modifier = Modifier.weight(1f),
                    leadingIcon = Icons.Default.Person
                )
            }

            EnhancedTextField(
                label = "Username",
                value = username,
                onValueChange = { username = it },
                leadingIcon = Icons.Default.AccountCircle
            )

            EnhancedTextField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = Icons.Default.Email
            )

            EnhancedTextField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
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
                leadingIcon = Icons.Default.Lock
            )

            EnhancedTextField(
                label = "Phone Number",
                value = phonenumber,
                onValueChange = { phonenumber = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = Icons.Default.Phone
            )

            EnhancedTextField(
                label = "Address",
                value = address,
                onValueChange = { address = it },
                singleLine = false,
                maxLines = 3,
                leadingIcon = Icons.Default.LocationOn
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        val result = registerUserA(
                            context,navController,firstname, lastname, username, email, password, phonenumber, address
                        )
                        isLoading = false
//                        Toast.makeText(context, response, Toast.LENGTH_LONG).show()
                        Toast.makeText(context, "Registered Successfully", Toast.LENGTH_LONG).show()
                        onRegistrationSuccess()
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
                    Text("Register", fontSize = 16.sp)
                }
            }
        }
    }
}*/
@Composable
fun RegisterAdminScreen(
    navController: NavController,
    onRegistrationSuccess: () -> Unit
) {
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
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            apiErrorMessage = null // Clear after showing
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
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
                painter = painterResource(id = R.drawable.car1),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Create Admin Account",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF2C3E50),
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
                EnhancedTextField(
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

                EnhancedTextField(
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

            EnhancedTextField(
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

            EnhancedTextField(
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

            EnhancedTextField(
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

            /*EnhancedTextField(
                label = "Phone Number *",
                value = phonenumber,
                onValueChange = {
                    phonenumber = it
                    phoneNumberError = it.isBlank()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = Icons.Default.Phone,
                isError = phoneNumberError,
                errorMessage = if (phoneNumberError) "Phone number is required" else null
            )*/
            EnhancedTextField(
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

            EnhancedTextField(
                label = "Address *",
                value = address,
                onValueChange = {
                    address = it
                    addressError = it.isBlank()
                },
                singleLine = false,
                maxLines = 3,
                leadingIcon = Icons.Default.LocationOn,
                isError = addressError,
                errorMessage = if (addressError) "Address is required" else null
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
               /* onClick = {
                    // Validate all fields before submission
                    firstNameError = firstname.isBlank()
                    lastNameError = lastname.isBlank()
                    usernameError = username.isBlank()
                    emailError = email.isBlank()
                    passwordError = password.isBlank()
                    phoneNumberError = phonenumber.isBlank()
                    addressError = address.isBlank()

                    val isValid = !firstNameError && !lastNameError && !usernameError &&
                            !emailError && !passwordError && !phoneNumberError &&
                            !addressError

                    if (isValid) {
                        coroutineScope.launch {
                            isLoading = true
                            val result = registerUserA(
                                context = context,
                                navController = navController,
                                firstname = firstname,
                                lastname = lastname,
                                username = username,
                                email = email,
                                password = password,
                                phonenumber = phonenumber,
                                address = address
                            ).also { response ->
                                Log.d("RegisterScreen", "API Response: $response")

                                // Check if response contains error
                                if (response.startsWith("Error:")) {
                                    apiErrorMessage = response.removePrefix("Error: ")
                                } else {
                                    // Success case - navigate only if API was successful
                                    onRegistrationSuccess()
                                }
                            }
                            isLoading = false*/
                onClick = {
                    // Validate all fields before submission
                    firstNameError = firstname.isBlank()
                    lastNameError = lastname.isBlank()
                    usernameError = username.isBlank()
                    emailError = email.isBlank()
                    emailFormatError = email.isNotBlank() && !isValidEmail(email)
                    passwordError = password.isBlank()
                    phoneNumberError = phonenumber.isBlank()
                    phoneFormatError = phonenumber.isNotBlank() && !isValidPhoneNumber(phonenumber)
                    addressError = address.isBlank()

                    val isValid = !firstNameError && !lastNameError && !usernameError &&
                            !emailError && !emailFormatError && !passwordError &&
                            !phoneNumberError && !phoneFormatError && !addressError

                    if (isValid) {
                        coroutineScope.launch {
                            isLoading = true
                            val result = registerUserA(
                                context = context,
                                navController = navController,
                                firstname = firstname,
                                lastname = lastname,
                                username = username,
                                email = email,
                                password = password,
                                phonenumber = phonenumber,
                                address = address
                            ).also { response ->
                                Log.d("RegisterScreen", "API Response: $response")
                                if (response.startsWith("Error:")) {
                                    apiErrorMessage = response.removePrefix("Error: ")
                                } else {
                                    onRegistrationSuccess()
                                }
                            }
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
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Text("Register", fontSize = 16.sp)
                }
            }
        }
    }
}

fun isValidEmail(email: String): Boolean {
//    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+[a-z]\$"
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,3}\$"
    return email.matches(emailRegex.toRegex())
}

fun isValidPhoneNumber(phone: String): Boolean {
    val phoneRegex = "^[0-9]{10,15}\$" // Allows 10-15 digits
    return phone.matches(phoneRegex.toRegex())
}
suspend fun registerUserA(
    context: android.content.Context,
    navController: NavController,
    firstname: String,
    lastname: String,
    username: String,
    email: String,
    password: String,
    phonenumber: String,
    address: String,
): String {
    return withContext(Dispatchers.IO) {
        try {
            Log.d("RegisterAPI", "Starting API call...")

            val client = OkHttpClient.Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build()

            val json = JSONObject().apply {
                put("first_name", firstname)
                put("last_name", lastname)
                put("username", username)
                put("email", email)
                put("password", password)
                put("phone_number", phonenumber)
                put("adress", address)
            }

            val jsonString = json.toString()
            Log.d("RegisterAPI", "JSON Payload: $jsonString")

            val requestBody = jsonString.toRequestBody("application/json".toMediaTypeOrNull())
            val token = getToken(context)

            val request = Request.Builder()
                .url(TestApi.Get_Register_admin)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer $token")
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
                Handler(Looper.getMainLooper()).post {
//                    Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                    // If need to navigate after this
                    // navController.navigate("login")
                }
//                // Show success Toast on Main Thread
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
//                    navController.navigate("login") // Navigate to login screen
//                }

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

/*@Composable
fun EnhancedTextField(
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
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF7F8C8D)
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
                    tint = Color(0xFF7F8C8D)
                )
            }
        },
        isError = isError,
        trailingIcon = trailingIcon,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = if (singleLine) 56.dp else 80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = Color(0xFF2C3E50),
            unfocusedTextColor = Color(0xFF2C3E50),
            focusedLabelColor = Color(0xFF2C3E50),
            unfocusedLabelColor = Color(0xFF7F8C8D),
            focusedIndicatorColor = Color(0xFF2C3E50),
            unfocusedIndicatorColor = Color(0xFFBDC3C7),
            cursorColor = Color(0xFF2C3E50)
        )
    )
    if (isError && errorMessage != null) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}*/
/*@Composable
fun EnhancedTextField(
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
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color(0xFF2C3E50),
                unfocusedTextColor = Color(0xFF2C3E50),
                focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color(0xFF2C3E50),
                unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color(0xFF7F8C8D),
                focusedIndicatorColor = if (isError) MaterialTheme.colorScheme.error else Color(0xFF2C3E50),
                unfocusedIndicatorColor = if (isError) MaterialTheme.colorScheme.error else Color(0xFFBDC3C7),
                cursorColor = Color(0xFF2C3E50)
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
}*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTextField(
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
@Composable
fun TextInputA(name: String, value: String, onChange: (String) -> Unit) {

    OutlinedTextField(
        value = value,
        onValueChange =  onChange ,
        label = { Text(text = name, color = Color(0xFF9ED90D)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF9ED90D),
            unfocusedContainerColor = Color(0xFF434A53),
            focusedIndicatorColor = Color(0xFF9ED90D)
        )
    )
}


/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownInput(label: String, options: List<String>, selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, color = Color.White) },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = Color.White,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9ED90D),
                unfocusedBorderColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
//                textColor = Color.White,
                containerColor = Color(0xFF434A53)
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
//                .matchParentSize()
                .background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
*/

@Composable
fun DropdownInputA(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Calculate the longest text width to match dropdown width
    val density = LocalDensity.current
    val textStyle = MaterialTheme.typography.bodyLarge
    val longestTextWidth = remember(options) {
        options.maxOfOrNull {
            with(density) {
                textStyle.fontSize.toPx() * it.length * 0.6f // Approximate calculation
            }
        } ?: 0f
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text(text = label, color = Color.White) },
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFF434A53),
                focusedIndicatorColor = Color(0xFF9ED90D),
                unfocusedIndicatorColor = Color.LightGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.White
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { longestTextWidth.toDp() + 48.dp }) // Add padding
                .background(Color(0xFF434A53))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    },
                    modifier = Modifier.background(Color(0xFF434A53))
                )
            }
        }
    }
}


//////////// ************ Admin List View ****************** /////////////////////////////


@Composable
fun AdminListScreen(
    viewModel: AdminListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val admins by remember { derivedStateOf { viewModel.admins } }
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(true) }

    val adminToEdit = remember { mutableStateOf<Admin?>(null) }
    val adminToDelete = remember { mutableStateOf<Admin?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchAdmins(context)
        isLoading.value = false
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7FA))) {
        if (isLoading.value) {
            LoadingAnimationI()
        } else if (admins.isEmpty()) {
            EmptyStateScreenI()
        } else {
            AnimatedAdminListI(admins, adminToEdit, adminToDelete)
        }
    }
    adminToEdit.value?.let { selectedAdmin ->
        EditAdminDialog(
            admin = selectedAdmin,
            onDismiss = { adminToEdit.value = null },
            onSubmit = { updateAdmin ->
                viewModel.updateAdmin(context, updateAdmin)
                adminToEdit.value = null
            }
        )
    }
    adminToDelete.value?.let { dealer ->
        AlertDialog(
            onDismissRequest = { adminToDelete.value = null },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete ${dealer.username}?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteAdminById(dealer.id, context)
                    adminToDelete.value = null
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { adminToDelete.value = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun LoadingAnimationI() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            strokeWidth = 4.dp,
            color = Color(0xFF4CAF50)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Loading Inspectors...",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF333333)
        )
    }
}

@Composable
private fun EmptyStateScreenI() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.car2),
            contentDescription = "No inspectors",
            modifier = Modifier.size(120.dp),
            tint = Color(0xFFBDBDBD)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Inspectors Found",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF333333)
        )
        Text(
            text = "Please check back later",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF757575),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun AnimatedAdminListI(admins: List<Admin>, adminToEdit: MutableState<Admin?>, adminToDelete: MutableState<Admin?>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        itemsIndexed(admins) { index, admin ->
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .clickable { /* Handle click */ },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 8.dp
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .animateContentSize()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = Color(0xFFE3F2FD),
                                        shape = CircleShape
                                    )
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = admin.username.take(1).uppercase(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF1976D2)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = admin.username,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF333333)
                                )

                                Text(
                                    text = admin.email,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF757575),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // Add and delete icon here
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(onClick = {
                                    adminToEdit.value = admin
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit dealer",
                                        tint = Color(0xFF4CAF50)
                                    )
                                }

                                IconButton(onClick = {
                                    adminToDelete.value = admin
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete dealer",
                                        tint = Color(0xFFF44336)
                                    )
                                }
                            }
                        }

                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color(0xFFEEEEEE),
                            thickness = 1.dp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            InfoChipA(
                                icon = Icons.Default.Phone,
                                text = admin.phone_number
                            )

                            InfoChipA(
                                icon = Icons.Default.LocationOn,
                                text = admin.adress ?: "N/A"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoChipA(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color(0xFF757575)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = Color(0xFF616161)
        )
    }
}
@Composable
fun EditAdminDialog(
    admin: Admin,
    onDismiss: () -> Unit,
    onSubmit: (Admin) -> Unit
) {
    var name by remember { mutableStateOf(admin.username) }
    var email by remember { mutableStateOf(admin.email) }
    var phone_number by remember { mutableStateOf(admin.phone_number) }
    var first_name by remember { mutableStateOf(admin.first_name) }
    var last_name by remember { mutableStateOf(admin.last_name) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit $name") },
        text = {
            Column {
                OutlinedTextField(
                    value = first_name,
                    onValueChange = { first_name = it },
                    label = { Text("First name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = last_name,
                    onValueChange = { last_name = it },
                    label = { Text("Last name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Username") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone_number,
                    onValueChange = { phone_number = it },
                    label = { Text("Phone Number") }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        confirmButton = {
            Button(onClick = {
                val updateInspector = admin.copy(
                    username = name,
                    email = email,
                    phone_number = phone_number,
                    first_name = first_name,
                    last_name = last_name
                )
                onSubmit(updateInspector)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}