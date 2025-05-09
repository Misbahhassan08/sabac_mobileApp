package com.example.carapp.models

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.Apis.TestApi
import com.example.carapp.screens.getToken
import kotlinx.coroutines.Dispatchers
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

/*

class CarListViewModelI : ViewModel() {

    private val _cars = MutableStateFlow<List<CarI>>(emptyList())
    val cars: StateFlow<List<CarI>> = _cars

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchCarList(context: Context) {
        _isLoading.value = true

        val url = TestApi.Get_Inspector_cars
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
                Log.e("CarListViewModelI", "API call failed: ${e.message}")
                e.printStackTrace()

                viewModelScope.launch(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                Log.d("CarListViewModelI", "Response Code: ${response.code}")
                Log.d("CarListViewModelI", "Response Body: $responseBody")

                if (response.isSuccessful) {
                    try {
                        responseBody?.let {
                            val jsonObject = JSONObject(it)
                            val appointmentsArray = jsonObject.getJSONArray("appointments")

                            val carList = mutableListOf<CarI>()
                            for (i in 0 until appointmentsArray.length()) {
                                val appointmentJson = appointmentsArray.getJSONObject(i)

                                val photosArray = appointmentJson.getJSONArray("car_phots")
                                val carPhotosList = mutableListOf<String>()
                                for (j in 0 until photosArray.length()) {
                                    carPhotosList.add(photosArray.getString(j))
                                }

                                val car = CarI(
                                    appointmentId = appointmentJson.getInt("appointment_id"),
                                    carId = appointmentJson.getInt("car_id"),
                                    sellerFirstName = appointmentJson.getString("seller_first_name"),
                                    sellerLastName = appointmentJson.getString("seller_last_name"),
                                    sellerPhoneNumber = appointmentJson.getString("seller_phone_number"),
                                    sellerEmail = appointmentJson.getString("seller_email"),
                                    carName = appointmentJson.getString("car_name"),
                                    carCompany = appointmentJson.getString("car_company"),
                                    carModel = appointmentJson.getString("car_model"),
                                    date = appointmentJson.getString("date"),
                                    timeSlot = appointmentJson.getString("time_slot"),
                                    remainingDays = appointmentJson.getInt("remaining_days"),
                                    remainingHours = appointmentJson.getInt("remaining_hours"),
                                    remainingMinutes = appointmentJson.getInt("remaining_minutes"),
                                    remainingSeconds = appointmentJson.getInt("remaining_seconds"),
                                    selectedBy = appointmentJson.getString("selected_by"),
                                    Photos = carPhotosList
                                )
                                carList.add(car)
                            }

                            viewModelScope.launch(Dispatchers.Main) {
                                _cars.value = carList
                                _isLoading.value = false
                            }
                            // Now call the second API
                            fetchSecondApi(client, token)
                        }
                    } catch (e: Exception) {
                        Log.e("CarListViewModelI", "Error parsing JSON: ${e.message}")
                    }
                } else {
                    Log.e("CarListViewModelI", "API request failed with code: ${response.code}")
                    viewModelScope.launch(Dispatchers.Main) {
                        _isLoading.value = false
                    }
                }
            }
        })
    }
}

*/
/*

class CarListViewModelI : ViewModel() {

    private val _cars = MutableStateFlow<List<CarI>>(emptyList())
    val cars: StateFlow<List<CarI>> = _cars

    private val _assignedSlots = MutableStateFlow<List<AssignedSlot>>(emptyList())
    val assignedSlots: StateFlow<List<AssignedSlot>> = _assignedSlots

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchCarList(context: Context) {
        _isLoading.value = true

        val url = TestApi.Get_Inspector_cars
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
                Log.e("CarListViewModelI", "API call failed: ${e.message}")
                e.printStackTrace()
                viewModelScope.launch { _isLoading.value = false }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("CarListViewModelI", "Response Code: ${response.code}")
                Log.d("CarListViewModelI", "Response Body: $responseBody")

                if (response.isSuccessful) {
                    try {
                        responseBody?.let {
                            val jsonObject = JSONObject(it)
                            val appointmentsArray = jsonObject.getJSONArray("appointments")

                            val carList = mutableListOf<CarI>()
                            for (i in 0 until appointmentsArray.length()) {
                                val appointmentJson = appointmentsArray.getJSONObject(i)
                                val photosArray = appointmentJson.getJSONArray("car_phots")
                                val carPhotosList = mutableListOf<String>()

                                for (j in 0 until photosArray.length()) {
                                    carPhotosList.add(photosArray.getString(j))
                                }

                                val car = CarI(
                                    appointmentId = appointmentJson.getInt("appointment_id"),
                                    carId = appointmentJson.getInt("car_id"),
                                    sellerFirstName = appointmentJson.getString("seller_first_name"),
                                    sellerLastName = appointmentJson.getString("seller_last_name"),
                                    sellerPhoneNumber = appointmentJson.getString("seller_phone_number"),
                                    sellerEmail = appointmentJson.getString("seller_email"),
                                    carName = appointmentJson.getString("car_name"),
                                    carCompany = appointmentJson.getString("car_company"),
                                    carModel = appointmentJson.getString("car_model"),
                                    date = appointmentJson.getString("date"),
                                    timeSlot = appointmentJson.getString("time_slot"),
                                    remainingDays = appointmentJson.getInt("remaining_days"),
                                    remainingHours = appointmentJson.getInt("remaining_hours"),
                                    remainingMinutes = appointmentJson.getInt("remaining_minutes"),
                                    remainingSeconds = appointmentJson.getInt("remaining_seconds"),
                                    selectedBy = appointmentJson.getString("selected_by"),
                                    Photos = carPhotosList
                                )
                                carList.add(car)
                            }

                            viewModelScope.launch {
                                _cars.value = carList
                                _isLoading.value = false
                            }
                            // Fetch second API data
                            fetchSecondApi(client, token)
                        }
                    } catch (e: Exception) {
                        Log.e("CarListViewModelI", "Error parsing JSON: ${e.message}")
                    }
                } else {
                    Log.e("CarListViewModelI", "API request failed with code: ${response.code}")
                    viewModelScope.launch { _isLoading.value = false }
                }
            }
        })
    }

    private fun fetchSecondApi(client: OkHttpClient, token: String?) {
        val url2 = TestApi.Get_assigned_slots

        val request2 = Request.Builder()
            .url(url2)
            .get()
            .apply {
                token?.let { addHeader("Authorization", "Bearer $it") }
            }
            .build()

        client.newCall(request2).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CarListViewModelI", "Second API call failed: ${e.message}")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("CarListViewModelI", "Second API Response Code: ${response.code}")
                Log.d("CarListViewModelI", "Second API Response Body: $responseBody")

                if (response.isSuccessful) {
                    try {
                        responseBody?.let {
                            val jsonArray = JSONArray(it)
                            val assignedSlotsList = mutableListOf<AssignedSlot>()

                            for (i in 0 until jsonArray.length()) {
                                val slotJson = jsonArray.getJSONObject(i)
                                val photosArray = slotJson.getJSONArray("car_photos")
                                val carPhotosList = mutableListOf<String>()

                                for (j in 0 until photosArray.length()) {
                                    carPhotosList.add(photosArray.getString(j))
                                }

                                val slot = AssignedSlot(
                                    id = slotJson.getInt("id"),
                                    inspector = slotJson.getString("inspector"),
                                    sellerName = slotJson.getString("seller_name"),
                                    sellerPhone = slotJson.getString("seller_phone"),
                                    sellerEmail = slotJson.getString("seller_email"),
                                    carCompany = slotJson.getString("car_company"),
                                    carModel = slotJson.getString("car_model"),
                                    carName = slotJson.getString("car_name"),
                                    Photos = carPhotosList,
                                    date = slotJson.getString("date"),
                                    timeSlot = slotJson.getString("time_slot")
                                )
                                assignedSlotsList.add(slot)
                            }

                            viewModelScope.launch {
                                _assignedSlots.value = assignedSlotsList
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("CarListViewModelI", "Error parsing JSON: ${e.message}")
                    }
                }
            }
        })
    }
}

*/

class CarListViewModelI : ViewModel() {

    private val _cars = MutableStateFlow<List<CarI>>(emptyList())
    val cars: StateFlow<List<CarI>> = _cars

    private val _assignedSlots = MutableStateFlow<List<AssignedSlot>>(emptyList())
    val assignedSlots: StateFlow<List<AssignedSlot>> = _assignedSlots

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var firstApiCompleted = false
    private var secondApiCompleted = false

    fun fetchCarList(context: Context) {
        _isLoading.value = true
        firstApiCompleted = false
        secondApiCompleted = false

        val url = TestApi.Get_Inspector_cars
        val token = getToken(context)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CarListViewModelIi", "API-1 call failed: ${e.message}")
                e.printStackTrace()
                firstApiCompleted = true
                checkLoadingState()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("CarListViewModelIi", "Response Code-1: ${response.code}")
                Log.d("CarListViewModelIi", "Response Body-1: $responseBody")

                if (response.isSuccessful) {
                    try {
                        responseBody?.let {
                            val jsonObject = JSONObject(it)
                            val appointmentsArray = jsonObject.getJSONArray("appointments")

                            /*val carList = mutableListOf<CarI>()
                            for (i in 0 until appointmentsArray.length()) {
                                val appointmentJson = appointmentsArray.getJSONObject(i)
                                val photosArray = appointmentJson.getJSONArray("photos")
                                val carPhotosList = mutableListOf<String>()

                                for (j in 0 until photosArray.length()) {
                                    carPhotosList.add(photosArray.getString(j))
                                }*/
                            val carList = mutableListOf<CarI>()
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

                                val car = CarI(
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
                        Log.e("CarListViewModelIi", "Error parsing JSON-1: ${e.message}")
                    }
                } else {
                    Log.e("CarListViewModelIi", "API request failed with code-1: ${response.code}")
                }

                firstApiCompleted = true
                checkLoadingState()
            }
        })

        // Fetch second API
        fetchSecondApi(client, token)
    }

    private fun fetchSecondApi(client: OkHttpClient, token: String?) {
        val url2 = TestApi.Get_assigned_slots

        val request2 = Request.Builder()
            .url(url2)
            .get()
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        client.newCall(request2).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CarListViewModelI2", "Second API call failed-2: ${e.message}")
                e.printStackTrace()
                secondApiCompleted = true
                checkLoadingState()
            }
           override fun onResponse(call: Call, response: Response) {
               val responseBody = response.body?.string()
               Log.d("CarListViewModelI2", "Second API Response Code-2: ${response.code}")
               Log.d("CarListViewModelI2", "Second API Response Body-2: $responseBody")

               if (response.isSuccessful) {
                   try {
                       responseBody?.let {
                           val jsonObject = JSONObject(it)
                           val jsonArray = jsonObject.getJSONArray("slots")
                           val assignedSlotsList = mutableListOf<AssignedSlot>()

                           for (i in 0 until jsonArray.length()) {
                               val slotJson = jsonArray.getJSONObject(i)

                               // Extracting "car" object
                               val carJson = slotJson.optJSONObject("car")

                               // Extracting "guest" object inside "car"
                               val guestJson = carJson?.optJSONObject("guest")
                               val guest = guestJson?.let {
                                   Cars.Guest(  // Use Cars.Guest instead of Guest
                                       name = it.optString("name", ""),
                                       number = it.optString("number", ""),
                                       email = it.optString("email", "")
                                   )
                               }

                               // Extracting photos
                               val carPhotosList = mutableListOf<String>()
                               if (carJson != null && carJson.has("photos")) {
                                   val photosArray = carJson.getJSONArray("photos")
                                   for (j in 0 until photosArray.length()) {
                                       carPhotosList.add(photosArray.getString(j))
                                   }
                               }

                               // Creating Cars object
                               val cars = carJson?.let {
                                   Cars(
                                       salerCarId = it.optInt("saler_car_id", 0),
                                       guest = guest,  // Use the nested guest object
                                       carName = it.optString("car_name", "Unknown"),
                                       company = it.optString("company", "Unknown"),
                                       color = it.optString("color", "Unknown"),
                                       condition = it.optString("condition", "Unknown"),
                                       model = it.optString("model", "Unknown"),
                                       demand = it.optString("demand", "0.0"),
                                       city = it.optString("city", "Unknown"),
                                       isSold = it.optBoolean("is_sold", false),
                                       milage = it.optDouble("milage", 0.0),
                                       description = it.optString("description", "No description"),
                                       type = it.optString("type", "Unknown"),
                                       fuelType = it.optString("fuel_type", "Unknown"),
                                       registeredIn = it.optString("registered_in", "Unknown"),
                                       assembly = it.optString("assembly", "Unknown"),
                                       engineCapacity = it.optString("engine_capacity", "Unknown"),
                                       photos = carPhotosList,
                                       phoneNumber = it.optString("phone_number", ""),
                                       secondaryNumber = it.optString("secondary_number", ""),
                                       status = it.optString("status", ""),
                                       createdAt = it.optString("created_at", ""),
                                       updatedAt = it.optString("updated_at", ""),
                                       isInspected = it.optBoolean("is_inspected", ),
                                       addedBy = it.optString("added_by", ""),
                                       inspector = it.optInt("inspector", 0)
                                   )
                               } ?: Cars(0, null, "", "", "", "", "", "", "", false, 0.0, "", "", "", "", "", "", emptyList(), "", "", "", "", "", false, "", 0)
                               Log.d("CarListViewModelI2", "Parsed saler_car_id: ${cars.salerCarId}")
                               // Creating AssignedSlot object
                               val slot = AssignedSlot(
                                   id = slotJson.getInt("id"),
                                   inspector = slotJson.optString("inspector", "Unknown"),
                                   car = cars,  // Use the correct Cars object
                                   date = slotJson.optString("date", ""),
                                   timeSlot = slotJson.optString("time_slot", ""),
                                   assignedBy = slotJson.optString("assigned_by", "Unknown")
                               )

                               assignedSlotsList.add(slot)
                           }

                           viewModelScope.launch {
                               _assignedSlots.value = assignedSlotsList
                           }
                       }
                   } catch (e: Exception) {
                       Log.e("CarListViewModelI2", "Error parsing JSON-2: ${e.message}")
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
data class CarI(
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

data class CarI(
    val appointmentId: Int,
    val carId: Int,
    val sellerFirstName: String,
    val sellerLastName: String,
    val sellerPhoneNumber: String,
    val sellerEmail: String,
    val carName: String,
    val carCompany: String,
    val carModel: String,
    val date: String,
    val timeSlot: String,
    val remainingDays: Int,
    val remainingHours: Int,
    val remainingMinutes: Int,
    val remainingSeconds: Int,
    val selectedBy: String,
    val Photos: List<String>,
    val isInspected: Boolean
)

*/

//data class AssignedSlot(
//    val id: Int,
//    val inspector: String,
//    val sellerName: String,
//    val sellerPhone: String,
//    val sellerEmail: String,
//    val carCompany: String,
//    val carModel: String,
//    val carName: String,
//    val photos: List<String>,
//    val date: String,
//    val timeSlot: String,
//
//)


data class AssignedSlot(
    val id: Int,
    val inspector: String,
    val car: Cars,
    val date: String,
    val timeSlot: String,
    val assignedBy: String
)

data class Cars(
    val salerCarId: Int,
    val guest: Guest?,
    val carName: String,
    val company: String,
    val color: String,
    val condition: String,
    val model: String,
    val demand: String,
    val city: String,
    val isSold: Boolean,
    val milage: Double,
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
    val isInspected: Boolean,
    val addedBy: String,
    val inspector: Int
) {
    data class Guest(
        val name: String,
        val number: String,
        val email: String
    )
}

/*

data class Guest(
    val name: String,
    val number: String,
    val email: String
)
*/
