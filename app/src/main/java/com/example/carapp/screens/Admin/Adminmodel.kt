package com.example.carapp.screens.Admin

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.Apis.TestApi
import com.example.carapp.models.fetchImageFromUrl
import com.example.carapp.screens.getToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class AdminModel : ViewModel() {

    private val _cars = MutableStateFlow<List<Upcoming>>(emptyList())
    val cars: StateFlow<List<Upcoming>> = _cars

    private val _assignedSlots = MutableStateFlow<List<Live>>(emptyList())
    val assignedSlots: StateFlow<List<Live>> = _assignedSlots

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var firstApiCompleted = false
    private var secondApiCompleted = false

    fun fetchCarList(context: Context) {
        _isLoading.value = true
        firstApiCompleted = false
        secondApiCompleted = false

        val url = TestApi.get_upcoming_cars
        val token = getToken(context)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FirstApi", "FirstApi call failed: ${e.message}")
                e.printStackTrace()
                firstApiCompleted = true
                checkLoadingState()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("FirstApi", "FirstApi Code: ${response.code}")
                Log.d("FirstApi", "FirstApi Body: $responseBody")

                if (response.isSuccessful) {
                    try {
                        responseBody?.let {
                            val jsonObject = JSONObject(it)
                            val appointmentsArray = jsonObject.getJSONArray("cars")

                            val carList = mutableListOf<Upcoming>()
                            for (i in 0 until appointmentsArray.length()) {
                                val carJson = appointmentsArray.getJSONObject(i)
                                val appointmentJson = appointmentsArray.getJSONObject(i)
                                val photosList = mutableListOf<String>()
                                val bitmapList = mutableListOf<Bitmap>()

                                Log.d("CarParse", "Parsing car $i")

                                if (!carJson.isNull("photos")) {
                                    val photosArray = carJson.getJSONArray("photos")
                                    for (j in 0 until photosArray.length()) {
                                        val photoEntry = photosArray.getString(j)
                                        try {
                                            val imageUrl = when {
                                                photoEntry.startsWith("http") -> photoEntry
                                                photoEntry.trim().startsWith("{") -> {
                                                    val correctedJson =
                                                        photoEntry.replace("'", "\"")
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
                                                    Log.d(
                                                        "BitmapFetch",
                                                        "Bitmap added for photo $j"
                                                    )
                                                    val width = bitmap.width
                                                    val height = bitmap.height
                                                    val byteCount = bitmap.byteCount
                                                    Log.d(
                                                        "BitmapFetch",
                                                        "Bitmap $j size: ${width}x${height}, Byte count: $byteCount"
                                                    )
                                                } else {
                                                    Log.e("BitmapFetch", "Bitmap null for $it")
                                                }
                                            }
                                        } catch (e: Exception) {
                                            Log.e(
                                                "PhotoParse",
                                                "Error parsing photo entry: $photoEntry",
                                                e
                                            )
                                        }
                                    }
                                }


                                val car = Upcoming(
                                    carId = appointmentJson.getInt("saler_car_id"),
                                    userId = appointmentJson.optInt("user", -1).takeIf { it != -1 },
                                    guest = appointmentJson.optString("guest", null),
                                    carName = appointmentJson.getString("car_name"),
                                    company = appointmentJson.getString("company"),
                                    year = appointmentJson.getString("year"),
                                    engineSize = appointmentJson.getString("engine_size"),
                                    milage = appointmentJson.getString("milage"),
                                    optionType = appointmentJson.getString("option_type"),
                                    paintCondition = appointmentJson.getString("paint_condition"),
                                    specs = appointmentJson.getString("specs"),
                                    inspectionDate = appointmentJson.getString("inspection_date"),
                                    inspectionTime = appointmentJson.getString("inspection_time"),
                                    createdAt = appointmentJson.getString("created_at"),
                                    updatedAt = appointmentJson.getString("updated_at"),
                                    status = appointmentJson.getString("status"),
                                    isInspected = appointmentJson.getBoolean("is_inspected"),
                                    primaryPhoneNumber = appointmentJson.optString(
                                        "primary_phone_number",
                                        null
                                    ),
                                    secondaryPhoneNumber = appointmentJson.optString(
                                        "secondary_phone_number",
                                        null
                                    ),
                                    addedBy = appointmentJson.getString("added_by"),
                                    isBooked = appointmentJson.getBoolean("is_booked"),
                                    isManual = appointmentJson.getBoolean("is_manual"),
                                    isSold = appointmentJson.getBoolean("is_sold"),
                                    photos = photosList,
                                    imageBitmaps = bitmapList
                                )
                                carList.add(car)
                            }

                            viewModelScope.launch {
                                _cars.value = carList
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("FirstApi", "FirstApi Error parsing JSON: ${e.message}")
                    }
                } else {
                    Log.e("FirstApi", "FirstApi API request failed with code: ${response.code}")
                }

                firstApiCompleted = true
                checkLoadingState()
            }
        })

        // Fetch second API
        fetchSecondApi(client, token)
    }


    private fun fetchSecondApi(client: OkHttpClient, token: String?) {
        val url2 = TestApi.get_approval_list

        val request2 = Request.Builder()
            .url(url2)
            .get()
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        client.newCall(request2).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("SecondApi", "Second API call failed: ${e.message}")
                e.printStackTrace()
                secondApiCompleted = true
                checkLoadingState()
            }

            /*override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("SecondApi2", "Second API Response Code: ${response.code}")
                Log.d("SecondApi2", "Second API Response Body: $responseBody")

                if (response.isSuccessful) {
                    try {
                        responseBody?.let {
                            val jsonObject = JSONObject(it)
                            val carsArray = jsonObject.getJSONArray("cars")

                            val assignedSlotsList = mutableListOf<Live>()

                            for (i in 0 until carsArray.length()) {
                                val carJson = carsArray.getJSONObject(i)
                                val photosArray = carJson.getJSONArray("photos")
                                val carPhotosList = mutableListOf<String>()

                                for (j in 0 until photosArray.length()) {
                                    carPhotosList.add(photosArray.getString(j))
                                }

                                val sellerJson = carJson.getJSONObject("seller")

                                val seller = Seller(
                                    id = sellerJson.getInt("id"),
                                    username = sellerJson.getString("username"),
                                    firstName = sellerJson.getString("first_name"),
                                    lastName = sellerJson.getString("last_name"),
                                    email = sellerJson.getString("email"),
                                    role = sellerJson.getString("role"),
                                    phoneNumber = sellerJson.getString("phone_number"),
                                    adress = sellerJson.getString("adress"),
                                    image = if (sellerJson.isNull("image")) null else sellerJson.getString("image")
                                )

                                val slot = Live(
                                    salerCarId = carJson.getInt("saler_car_id"),
                                    user = carJson.getInt("user"),
                                    guest = null, // handle this if structure is known
                                    carName = carJson.getString("car_name"),
                                    company = carJson.getString("company"),
                                    year = carJson.getString("year"),
                                    engineSize = carJson.getString("engine_size"),
                                    milage = carJson.getString("milage"),
                                    optionType = carJson.getString("option_type"),
                                    paintCondition = carJson.getString("paint_condition"),
                                    specs = carJson.getString("specs"),
                                    inspectionDate = carJson.getString("inspection_date"),
                                    inspectionTime = carJson.getString("inspection_time"),
                                    createdAt = carJson.getString("created_at"),
                                    updatedAt = carJson.getString("updated_at"),
                                    status = carJson.getString("status"),
                                    isInspected = carJson.getBoolean("is_inspected"),
                                    primaryPhoneNumber = if (carJson.isNull("primary_phone_number")) null else carJson.getString("primary_phone_number"),
                                    secondaryPhoneNumber = if (carJson.isNull("secondary_phone_number")) null else carJson.getString("secondary_phone_number"),
                                    addedBy = carJson.getString("added_by"),
                                    isBooked = carJson.getBoolean("is_booked"),
                                    isManual = carJson.getBoolean("is_manual"),
                                    isSold = carJson.getBoolean("is_sold"),
                                    photos = carPhotosList,
                                    inspector = carJson.getInt("inspector"),
                                    seller = seller
                                )

                                assignedSlotsList.add(slot)
                            }

                            viewModelScope.launch {
                                _assignedSlots.value = assignedSlotsList
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("SecondApi", "Error parsing JSON: ${e.message}")
                    }
                }

                secondApiCompleted = true
                checkLoadingState()
            }*/
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("SecondApi2", "Second API Response Code: ${response.code}")
                Log.d("SecondApi2", "Second API Response Body: $responseBody")

                if (response.isSuccessful) {
                    try {
                        responseBody?.let {
                            val carsArray = JSONArray(it)

                            val assignedSlotsList = mutableListOf<Live>()

                            for (i in 0 until carsArray.length()) {
                                val carJson = carsArray.getJSONObject(i)
                                val photosArray = carJson.getJSONArray("photos")
                                val carPhotosList = mutableListOf<String>()

                                for (j in 0 until photosArray.length()) {
                                    carPhotosList.add(photosArray.getString(j))
                                }

                                val sellerJson = carJson.getJSONObject("seller")

                                val seller = Seller(
                                    id = sellerJson.getInt("id"),
                                    username = sellerJson.getString("username"),
                                    firstName = sellerJson.getString("first_name"),
                                    lastName = sellerJson.getString("last_name"),
                                    email = sellerJson.getString("email"),
                                    role = sellerJson.getString("role"),
                                    phoneNumber = sellerJson.getString("phone_number"),
                                    adress = sellerJson.getString("adress"),
                                    image = if (sellerJson.isNull("image")) null else sellerJson.getString("image")
                                )

                                val slot = Live(
                                    salerCarId = carJson.getInt("saler_car_id"),
                                    user = carJson.getInt("user"),
                                    guest = null, // handle this if needed
                                    carName = carJson.getString("car_name"),
                                    company = carJson.getString("company"),
                                    year = carJson.getString("year"),
                                    engineSize = carJson.getString("engine_size"),
                                    milage = carJson.getString("milage"),
                                    optionType = carJson.getString("option_type"),
                                    paintCondition = carJson.getString("paint_condition"),
                                    specs = carJson.getString("specs"),
                                    inspectionDate = carJson.getString("inspection_date"),
                                    inspectionTime = carJson.getString("inspection_time"),
                                    createdAt = carJson.getString("created_at"),
                                    updatedAt = carJson.getString("updated_at"),
                                    status = carJson.getString("status"),
                                    isInspected = carJson.getBoolean("is_inspected"),
                                    primaryPhoneNumber = if (carJson.isNull("primary_phone_number")) null else carJson.getString("primary_phone_number"),
                                    secondaryPhoneNumber = if (carJson.isNull("secondary_phone_number")) null else carJson.getString("secondary_phone_number"),
                                    addedBy = carJson.getString("added_by"),
                                    isBooked = carJson.getBoolean("is_booked"),
                                    isManual = carJson.getBoolean("is_manual"),
                                    isSold = carJson.getBoolean("is_sold"),
                                    photos = carPhotosList,
                                    inspector = carJson.getInt("inspector"),
                                    seller = seller
                                )

                                assignedSlotsList.add(slot)
                            }

                            viewModelScope.launch {
                                _assignedSlots.value = assignedSlotsList
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("SecondApi", "Error parsing JSON: ${e.message}")
                    }
                }

                secondApiCompleted = true
                checkLoadingState()
            }

        })
    }


    private fun checkLoadingState() {
        if (firstApiCompleted && secondApiCompleted) {
            viewModelScope.launch {
                _isLoading.value = false
            }
        }
    }
}


/*data class Upcoming(
    val salerCarId: Int,
    val userId: Int,
    val carName: String,
    val company: String,
    val color: String,
    val condition: String,
    val model: String,
    val demand: String,
    val city: String,
    val isSold: Boolean,
    val mileage: String,
    val description: String,
    val type: String,
    val fuelType: String,
    val registeredIn: String,
    val assembly: String,
    val engineCapacity: String,
    val photos: List<String>,
    val phoneNumber: String,
    val secondaryNumber: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val isInspected: Boolean
)*/
data class Upcoming(
    val carId: Int,
    val userId: Int?,
    val guest: String?,
    val carName: String,
    val company: String,
    val year: String,
    val engineSize: String,
    val milage: String,
    val optionType: String,
    val paintCondition: String,
    val specs: String,
    val inspectionDate: String,
    val inspectionTime: String,
    val createdAt: String,
    val updatedAt: String,
    val status: String,
    val isInspected: Boolean,
    val primaryPhoneNumber: String?,
    val secondaryPhoneNumber: String?,
    val addedBy: String,
    val isBooked: Boolean,
    val isManual: Boolean,
    val isSold: Boolean,
    val photos: List<String>,
    val imageBitmaps: List<Bitmap> = emptyList(),
)

/*
data class Live(
    val salerCarId: Int,
    val carName: String,
    val company: String,
    val model: String,
    val demand: String,
    val photos: List<String>,
    val overallRating: String
)*/

data class Live(
    val salerCarId: Int,
    val user: Int,
    val guest: Any?, // could be null or a specific type if needed
    val carName: String,
    val company: String,
    val year: String,
    val engineSize: String,
    val milage: String,
    val optionType: String,
    val paintCondition: String,
    val specs: String,
    val inspectionDate: String,
    val inspectionTime: String,
    val createdAt: String,
    val updatedAt: String,
    val status: String,
    val isInspected: Boolean,
    val primaryPhoneNumber: String?,
    val secondaryPhoneNumber: String?,
    val addedBy: String,
    val isBooked: Boolean,
    val isManual: Boolean,
    val isSold: Boolean,
    val photos: List<String>,
    val inspector: Int,
    val seller: Seller
)

data class Seller(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String,
    val phoneNumber: String,
    val adress: String,
    val image: String?
)

