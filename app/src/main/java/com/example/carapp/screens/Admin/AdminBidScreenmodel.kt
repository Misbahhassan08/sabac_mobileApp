package com.example.carapp.screens.Admin

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.Apis.TestApi
import com.example.carapp.screens.getToken
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class BidViewModel : ViewModel() {

    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val cars: StateFlow<List<Car>> = _cars

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications

/*
    fun fetchNotifications(context: Context) {
        Log.d("bid","NOTIFICATION")

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("bid","NOTIFICATION_ent")
            try {
                val token = getToken(context)
                val client = OkHttpClient()
                Log.d("bid","NOTIFICATION_enter")
                val request = Request.Builder()
                    .url(TestApi.Bid_notification_for_seller)
                    .get()
                    .apply {
                        token?.let { addHeader("Authorization", "Bearer $it") }
                    }
                    .build()
                Log.d("bid","NOTIFICATION_build")
                val response = client.newCall(request).execute()
                Log.d("bid","NOTIFICATION_response")
                if (response.isSuccessful) {
                    val jsonResponse = response.body?.string()
                    Log.d("FetchNotificationsB", "Response: $jsonResponse")
                    val notificationsList = Gson().fromJson(jsonResponse, Array<NotificationItem>::class.java).toList()
                    _notifications.value = notificationsList
                    Log.d("FetchNotificationsB", "Parsed Notifications: $notificationsList")

                    if (notificationsList.isNotEmpty()) {
                        markNotificationsAsRead(context, notificationsList.map { it.id })
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("bid","NOTIFICATION_error")
            }
        }
    }
*/

    fun fetchNotifications(context: Context) {
        Log.d("bid","NOTIFICATION")

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("bid","NOTIFICATION_ent")
            try {
                val token = getToken(context)
                val client = OkHttpClient.Builder()
                    .build()
                Log.d("bid","NOTIFICATION_enter")

                val request = Request.Builder()
                    .url(TestApi.Bid_notification_for_seller)
                    .get()
                    .apply {
                        token?.let {
                            addHeader("Authorization", "Bearer $it")
                            Log.d("bid", "Token added: $it")
                        }
                    }
                    .build()

                Log.d("bid","NOTIFICATION_build")

                val response = client.newCall(request).execute()
                Log.d("bid", "HTTP Status: ${response.code}")
                val jsonResponse = response.body?.string()
                Log.d("bid_", "Raw Response: $jsonResponse")

                if (response.isSuccessful) {
                    val notificationsList = Gson().fromJson(jsonResponse, Array<NotificationItem>::class.java).toList()
//                    _notifications.value = notificationsList
                    addNotifications(notificationsList)
                    Log.d("bid", "Parsed Notifications: $notificationsList")

                    if (notificationsList.isNotEmpty()) {
                        markNotificationsAsRead(context, notificationsList.map { it.id })
                    }
                } else {
                    Log.e("bid", "Request failed: ${response.message}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("bid","NOTIFICATION_error: ${e.localizedMessage}")
            }
        }
    }

    fun addNotifications(newList: List<NotificationItem>) {
        val currentList = _notifications.value.toMutableList()
        val newOnes = newList.filterNot { n -> currentList.any { it.id == n.id } }
        if (newOnes.isNotEmpty()) {
            _notifications.value = currentList + newOnes
        }
    }

    private fun markNotificationsAsRead(context: Context, notificationIds: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = getToken(context)
                val client = OkHttpClient()

                val jsonBody = Gson().toJson(mapOf("notification_ids" to notificationIds))

                val requestBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())

                val request = Request.Builder()
                    .url(TestApi.mark_bid_notifications_as_read)
                    .post(requestBody)
                    .apply {
                        token?.let { addHeader("Authorization", "Bearer $it") }
                    }
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    Log.d("Notification", "Notifications marked as read: $notificationIds")
                } else {
                    Log.e("Notification", "Failed to mark notifications as read: ${response.code}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeNotification(notification: NotificationItem) {
        _notifications.value = _notifications.value - notification
    }

}


data class Car(
    val salerCarId: Int,
    val name: String,
    val company: String,
    val year: String,
    val engineSize: String,
    val mileage: String,
    val optionType: String,
    val paint_condition: String,
    val specs: String,
    val inspectionDate: String,
    val inspectionTime: String,
    val createdAt: String,
    val updatedAt: String,
    val status: String,
    val isInspected: Boolean,
    val isBooked: Boolean,
    val isManual: Boolean,
    val isSold: Boolean,
    val photos: List<String>?,
    val imageBitmaps: List<Bitmap> = emptyList(),
    val sellerName: String,
    val phoneNumber: String
)

data class NotificationItem(
    val category: String,
    val message: String,
    val id: String,
    val bid : Bid?
)


data class Bid(
    val id: Int,
    val bid_amount: String,
    val is_accepted: Boolean,
    val dealer_name: String,
    val car_name: String,
    val is_sold: Boolean,
    val created_at: String,
    val dealer: Int,
    val saler_car: SalerCar?
)

data class SalerCar(
    val saler_car_id: Int,
    val user: Int,
    val guest: Any?, // Adjust type based on actual use
    val car_name: String
)