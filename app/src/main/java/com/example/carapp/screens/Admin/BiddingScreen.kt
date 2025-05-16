package com.example.carapp.screens.Admin

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.carapp.Apis.TestApi
import com.example.carapp.screens.Inspector.updateCarStatu
import com.example.carapp.screens.RejectBid
import com.example.carapp.screens.getToken
import com.example.carapp.screens.sendBidid
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
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
fun BiddingScreen(
    navController: NavController,
    viewModel: BidViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
    val notifications by viewModel.notifications.collectAsState()
    val systemUiController = rememberSystemUiController()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    systemUiController.isStatusBarVisible = false
    LaunchedEffect(isLoading) {
//        if (!isLoading) {
        while (true) {
            viewModel.fetchNotifications(context)
            delay(120L)
        }
//        }
    }
    if (notifications.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
//                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        ) {
            notifications.forEach { notification ->
                NotificationCardB(
                    notification = notification,
                    onDismiss = { viewModel.removeNotification(notification) },
//                                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun NotificationCardB(notification: com.example.carapp.screens.Admin.NotificationItem, onDismiss: () -> Unit) {
    var timeLeft by remember { mutableStateOf(20) }
    val context = LocalContext.current

    LaunchedEffect(notification) {
        while (timeLeft > 0) {
            delay(2000L)
            timeLeft -= 1
        }
        onDismiss()
    }

    val progress by animateFloatAsState(
        targetValue = timeLeft / 10f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "progress"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = notification.message,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        sendBidid(notification.bid_id, context)
                        updateCarStatusB(
                            context,
                            notification.id,
//                            slot.car.salerCarId.toString(),
                            "sold",
                            onSuccess = {
                                Handler(Looper.getMainLooper()).post {
//                                    navController.navigate("report/${slot.car.salerCarId}")
                                }
                            },
                            onFailure = { errorMessage ->
                                Log.e("API_ERROR", "Failed to update status: $errorMessage")
                            }
                        )
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    )
                ) { Text("Accept") }

                Button(
                    onClick = {
                        RejectBid(notification.bid_id, context)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336),
                        contentColor = Color.White
                    )
                ) { Text("Reject") }
            }

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(4.dp),
                color = Color.Blue,
                trackColor = Color.LightGray
            )
        }
    }
}



fun updateCarStatusB(context: Context, carId: String, status: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    val url = "${TestApi.update_status}$carId/"
    Log.d("API_DEBUG", "Request URL: $url")

    val token = getToken(context)
    Log.d("API_DEBUG", "Token: $token")
    val client = OkHttpClient()
    val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    val jsonBody = JSONObject().apply {
        put("status", status)
    }.toString()

    val requestBody = jsonBody.toRequestBody(jsonMediaType)

    val request = Request.Builder()
        .url(url)
        .patch(requestBody)
        .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onFailure(e.message ?: "Unknown error")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onFailure(response.message)
            }
        }
    })
}



/*

@Composable
fun BiddingScreen(
    navController: NavController,
    viewModel: BidViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val notifications by viewModel.notifications.collectAsState()
    val systemUiController = rememberSystemUiController()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    systemUiController.isStatusBarVisible = false

    LaunchedEffect(isLoading) {
        while (true) {
            viewModel.fetchNotifications(context)
            delay(12000L)
        }
    }

    AnimatedVisibility(
        visible = notifications.isNotEmpty(),
        enter = fadeIn() + slideInVertically { -40 },
        exit = fadeOut() + slideOutVertically { -40 },
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            notifications.forEach { notification ->
                key(notification.bid_id) {
                    NotificationCardB(
                        notification = notification,
                        onDismiss = { viewModel.removeNotification(notification) }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationCardB(
    notification: NotificationItem,
    onDismiss: () -> Unit
) {
    var timeLeft by remember { mutableIntStateOf(20) }
    val context = LocalContext.current

    LaunchedEffect(notification) {
        while (timeLeft > 0) {
            delay(2000L)
            timeLeft -= 1
        }
        onDismiss()
    }

    val progress by animateFloatAsState(
        targetValue = timeLeft / 10f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "progress"
    )

    // Animate card appearance/disappearance
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    spotColor = Color.Blue.copy(alpha = 0.1f)
                ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header with icon and title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notification",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "New Bid Request",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Notification message
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )

                // Progress indicator with time remaining
                Column {
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surface
                    )
                    Text(
                        text = "$timeLeft seconds remaining",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.align(Alignment.End),
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
                ) {
                    OutlinedButton(
                        onClick = {
                            RejectBid(notification.bid_id, context)
                            onDismiss()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                    ) {
                        Text("Reject")
                    }

                    Button(
                        onClick = {
                            sendBidid(notification.bid_id, context)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Accept")
                    }
                }
            }
        }
    }
}*/
