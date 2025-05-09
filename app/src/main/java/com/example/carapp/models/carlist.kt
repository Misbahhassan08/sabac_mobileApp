package com.example.carapp.models

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
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/*
class CarListViewModel : ViewModel() {

    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val cars: StateFlow<List<Car>> = _cars

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchCarList(context: Context) {
        _isLoading.value = true // Start loading
        val url = TestApi.get_user_cars
        val token = getToken(context)

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .apply {
                token?.let { addHeader("Authorization", "Bearer $it") }
            }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                viewModelScope.launch(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { jsonResponse ->
                        val jsonObject = JSONObject(jsonResponse)
                        val carsArray = jsonObject.getJSONArray("cars")

                        val carList = mutableListOf<Car>()
                        for (i in 0 until carsArray.length()) {
                            val carJson = carsArray.getJSONObject(i)
                            val photosArray = carJson.getJSONArray("photos")
                            val photosList = mutableListOf<String>()

                            for (j in 0 until photosArray.length()) {
                                photosList.add(photosArray.getString(j)) // Extract Base64 strings
                            }


                            val car = Car(
                                name = carJson.getString("car_name"),
                                company = carJson.getString("company"),
                                color = carJson.getString("color"),
                                condition = carJson.getString("condition"),
                                model = carJson.getString("model"),
                                demand = carJson.getString("demand"),
                                city = carJson.getString("city"),
                                isSold = carJson.getBoolean("is_sold"),
                                mileage = carJson.getInt("milage"),
                                description = carJson.getString("description"),
                                type = carJson.getString("type"),
                                fuelType = carJson.getString("fuel_type"),
                                registeredIn = carJson.getString("registered_in"),
                                assembly = carJson.getString("assembly"),
                                engineCapacity = carJson.getString("engine_capacity"),
                                phoneNumber = carJson.getString("phone_number"),
                                status = carJson.getString("status"),
                                createdAt = carJson.getString("created_at"),
                                photos = photosList,
                                updatedAt = carJson.getString("updated_at")
                            )
                            carList.add(car)
                        }

                        viewModelScope.launch(Dispatchers.Main) {
                            _cars.value = carList
                        }
                    }
                }
            }
        })
    }
}
*/
class CarListViewModel : ViewModel() {

    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val cars: StateFlow<List<Car>> = _cars

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications

/*
    fun fetchNotifications(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = getToken(context)
                val client = OkHttpClient()

                val request = Request.Builder()
                    .url(TestApi.Bid_notification_for_seller)
                    .get()
                    .apply {
                        token?.let { addHeader("Authorization", "Bearer $it") }
                    }
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val jsonResponse = response.body?.string()

                    Log.d("API Response", "Raw JSON: $jsonResponse")

                    val notificationsList = Gson().fromJson(jsonResponse, Array<NotificationItem>::class.java).toList()

                    _notifications.value = notificationsList

                    if (notificationsList.isNotEmpty()) {
                        markNotificationsAsRead(context, notificationsList.map { it.id })
                    }

                } else {
                    Log.e("API Error", "Response failed: ${response.code}")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "Error fetching notifications", e)
            }
        }
    }
*/
fun fetchNotifications(context: Context) {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            val token = getToken(context)
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(TestApi.Bid_notification_for_seller)
                .get()
                .apply {
                    token?.let { addHeader("Authorization", "Bearer $it") }
                }
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val jsonResponse = response.body?.string()
                Log.d("FetchNotifications", "Response: $jsonResponse")
                val notificationsList = Gson().fromJson(jsonResponse, Array<NotificationItem>::class.java).toList()
                _notifications.value = notificationsList
                Log.d("FetchNotifications", "Parsed Notifications: $notificationsList")

                if (notificationsList.isNotEmpty()) {
                    markNotificationsAsRead(context, notificationsList.map { it.id })
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
/*
    fun fetchCarList(context: Context) {
        _isLoading.value = true

        val url = TestApi.get_user_cars
        val token = getToken(context)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .apply {
                token?.let { addHeader("Authorization", "Bearer $it") }
            }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()

                viewModelScope.launch(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {


                    response.body?.string()?.let { jsonResponse ->
                        val jsonObject = JSONObject(jsonResponse)
                        val carsArray = jsonObject.getJSONArray("cars")

                        val carList = mutableListOf<Car>()
                        for (i in 0 until carsArray.length()) {
                            val carJson = carsArray.getJSONObject(i)

//                            val photosArray = carJson.getJSONArray("photos")
//                            val photosList = mutableListOf<String>()
//                            for (j in 0 until photosArray.length()) {
//                                photosList.add(photosArray.getString(j))
//                            }

                            val car = Car(
                                name = carJson.getString("car_name"),
                                company = carJson.getString("company"),
                                paint_condition = carJson.getString("paint_condition"),
                                condition = carJson.getString("condition"),
                                model = carJson.getString("model"),
                                demand = carJson.getString("demand"),
                                city = carJson.getString("city"),
                                isSold = carJson.getBoolean("is_sold"),
                                mileage = carJson.getInt("milage"),
                                description = carJson.getString("description"),
                                type = carJson.getString("type"),
                                fuelType = carJson.getString("fuel_type"),
                                registeredIn = carJson.getString("registered_in"),
                                assembly = carJson.getString("assembly"),
                                engineCapacity = carJson.getString("engine_capacity"),
//                                phoneNumber = carJson.getString("phone_number"),
                                status = carJson.getString("status"),
                                createdAt = carJson.getString("created_at"),
                                updatedAt = carJson.getString("updated_at"),
//                                photos = photosList
                            )
                            carList.add(car)
                        }

                        viewModelScope.launch(Dispatchers.Main) {
                            _cars.value = carList
                            _isLoading.value = false // Turn off loading once data is updated
                        }
                    }
                } else {
                    // If not successful, ensure we turn off the loader
                    viewModelScope.launch(Dispatchers.Main) {
                        _isLoading.value = false
                    }
                }
            }
        })
    }

    */

    fun fetchCarList(context: Context) {
        _isLoading.value = true

        val url = TestApi.get_user_cars
        val token = getToken(context)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .apply {
                token?.let { addHeader("Authorization", "Bearer $it") }
            }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                viewModelScope.launch(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { jsonResponse ->
                        Log.d("FULL_API_RESPONSE", jsonResponse)

                        val jsonObject = JSONObject(jsonResponse)
                        val carsArray = jsonObject.getJSONArray("cars")

//                        val carList = mutableListOf<Car>()
//                        for (i in 0 until carsArray.length()) {
//                            val carJson = carsArray.getJSONObject(i)
                        val carList = mutableListOf<Car>()
                        for (i in 0 until carsArray.length()) {
                            val carJson = carsArray.getJSONObject(i)
                            val photosList = mutableListOf<String>()
                            val bitmapList = mutableListOf<Bitmap>()

                            /*// Extract photos from array of strings
                            val photosList = mutableListOf<String>()
                            if (!carJson.isNull("photos")) {
                                val photosArray = carJson.getJSONArray("photos")
                                for (j in 0 until photosArray.length()) {
                                    val photoJsonStr = photosArray.getString(j)
                                    try {
                                        val photoJson = JSONObject(photoJsonStr.replace("'", "\""))
                                        val imageUrl = photoJson.getString("image_url")
                                        photosList.add(imageUrl)
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                            }*/
                            // Extract photos from array of strings


                           /* if (!carJson.isNull("photos") && carJson.getJSONArray("photos").length() > 0) {
                                val photosArray = carJson.getJSONArray("photos")

                                for (j in 0 until photosArray.length()) {
                                    val photoEntry = photosArray.getString(j)

                                    val imageUrl = if (photoEntry.trim().startsWith("{")) {
                                        try {
                                            val correctedJson = photoEntry.replace("'", "\"")
                                            val photoJson = JSONObject(correctedJson)
                                            photoJson.getString("image_url")
                                        } catch (e: JSONException) {
                                            Log.e("photos", "Error parsing JSON photo string: $photoEntry", e)
                                            null
                                        }
                                    } else {
                                        photoEntry  // Direct image URL
                                    }

                                    imageUrl?.let { photosList.add(it) }
                                }
                            } else {
                                Log.e("photos", "No valid photos found in the response")
                            }*/


                                    Log.d("CarParse", "Parsing car $i")

                                    if (!carJson.isNull("photos")) {
                                        val photosArray = carJson.getJSONArray("photos")
                                        for (j in 0 until photosArray.length()) {
                                            val photoEntry = photosArray.getString(j)
                                            try {
                                                val imageUrl = when {
                                                    photoEntry.startsWith("http") -> photoEntry
                                                    photoEntry.trim().startsWith("{") -> {
                                                        val correctedJson = photoEntry.replace("'", "\"")
                                                        val photoJson = JSONObject(correctedJson)
                                                        photoJson.getString("image_url")
                                                    }
                                                    else -> null
                                                }
                                                Log.d("PhotoParse", "Photo $j parsed: $imageUrl")
                                                imageUrl?.let {
                                                    photosList.add(it)
                                                    val bitmap = fetchImageFromUrl(it)
                                                    if (bitmap != null) {
                                                        bitmapList.add(bitmap)
                                                        Log.d("BitmapFetch", "Bitmap added for photo $j")
                                                        val width = bitmap.width
                                                        val height = bitmap.height
                                                        val byteCount = bitmap.byteCount
                                                        Log.d("BitmapFetch", "Bitmap $j size: ${width}x${height}, Byte count: $byteCount")
                                                    } else {
                                                        Log.e("BitmapFetch", "Bitmap null for $it")
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                Log.e("PhotoParse", "Error parsing photo entry: $photoEntry", e)
                                            }
                                        }
                                    }

                            // Seller info
                            val seller = carJson.getJSONObject("seller")
                            val sellerName = "${seller.getString("first_name")} ${seller.getString("last_name")}"
                            val phoneNumber = seller.getString("phone_number")

                            val car = Car(
                                salerCarId = carJson.getInt("saler_car_id"),
                                name = carJson.getString("car_name"),
                                company = carJson.getString("company"),
                                year = carJson.getString("year"),
                                engineSize = carJson.getString("engine_size"),
                                mileage = carJson.getString("milage"),
                                optionType = carJson.getString("option_type"),
                                paint_condition = carJson.getString("paint_condition"),
                                specs = carJson.getString("specs"),
                                inspectionDate = carJson.getString("inspection_date"),
                                inspectionTime = carJson.getString("inspection_time"),
                                createdAt = carJson.getString("created_at"),
                                updatedAt = carJson.getString("updated_at"),
                                status = carJson.getString("status"),
                                isInspected = carJson.getBoolean("is_inspected"),
                                isBooked = carJson.getBoolean("is_booked"),
                                isManual = carJson.getBoolean("is_manual"),
                                isSold = carJson.getBoolean("is_sold"),
                                photos = photosList,
                                imageBitmaps = bitmapList,
                                sellerName = sellerName,
                                phoneNumber = phoneNumber
                            )
                            carList.add(car)
                        }

                        viewModelScope.launch(Dispatchers.Main) {
                            _cars.value = carList
                            _isLoading.value = false
                        }
                    }
                } else {
                    viewModelScope.launch(Dispatchers.Main) {
                        _isLoading.value = false
                    }
                }
            }
        })
    }

}
fun fetchImageFromUrl(url: String): Bitmap? {
    return try {
        Log.d("ImageFetch", "Fetching image from URL: $url")
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            Log.d("ImageFetch", "Image fetched successfully from: $url")
            val inputStream = response.body?.byteStream()
            BitmapFactory.decodeStream(inputStream)
        } else {
            Log.e("ImageFetch", "Failed to fetch image. Response code: ${response.code}")
            null
        }
    } catch (e: Exception) {
        Log.e("ImageFetch", "Exception while fetching image from: $url", e)
        null
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



/*

data class Car(
    val name: String,
    val company: String,
    val paint_condition: String,
    val condition: String,
    val model: String,
    val demand: String,
    val city: String,
    val isSold: Boolean,
    val mileage: Int,
    val description: String,
    val type: String,
    val fuelType: String,
    val registeredIn: String,
    val assembly: String,
    val engineCapacity: String,
//    val phoneNumber: String,
    val status: String,
    val createdAt: String,
//    val photos: List<String>,
    val updatedAt: String
)


*/


data class NotificationItem(
    val category: String,
    val message: String,
    val id: String,
    val bid_id: String
)
