package com.example.carapp.models

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.Apis.TestApi
import com.example.carapp.screens.getToken
import com.example.carapp.screens.getUserId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/*class GuestListViewModel : ViewModel() {

    private val _cars = MutableStateFlow<List<guest>>(emptyList())
    val cars: StateFlow<List<guest>> = _cars



    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var firstApiCompleted = false
    private var secondApiCompleted = false

    fun fetchCarListguest(context: Context) {
        _isLoading.value = true
        firstApiCompleted = false
        secondApiCompleted = false
        val userId = getUserId(context)
        Log.d("User", "USERIDDDDDD $userId")

        val url = "${TestApi.get_guest_car_details}?inspector_id=$userId"
        val token = getToken(context)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CarListViewModelI", "API call failed: ${e.message}")
                e.printStackTrace()
                firstApiCompleted = true
                checkLoadingState()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("CarListViewModelI", "Response Code: ${response.code}")
                Log.d("CarListViewModelI", "Response Body: $responseBody")

                if (response.isSuccessful) {
                    try {
                        if (responseBody.isNullOrEmpty() || responseBody == "[]") {
                            Log.e("CarListViewModelI", "Empty response received.")
                            viewModelScope.launch {
                                _cars.value = emptyList()
                            }
                            firstApiCompleted = true
                            checkLoadingState()
                            return
                        }

                        val appointmentsArray = JSONArray(responseBody)

                        val carList = mutableListOf<guest>()
                        for (i in 0 until appointmentsArray.length()) {
                            val appointmentJson = appointmentsArray.getJSONObject(i)
                            val photosArray = appointmentJson.optJSONArray("car_phots") ?: JSONArray()
                            val carPhotosList = mutableListOf<String>()

                            for (j in 0 until photosArray.length()) {
                                carPhotosList.add(photosArray.getString(j))
                            }

                            val car = guest(
                                saler_car_id = appointmentJson.getInt("saler_car_id"),
                                user = appointmentJson.optInt("user", -1), // Handle null values safely
                                carName = appointmentJson.getString("car_name"),
                                carCompany = appointmentJson.getString("company"),
                                color = appointmentJson.getString("color"),
                                condition = appointmentJson.getString("condition"),
                                carModel = appointmentJson.getString("model"),
                                demand = appointmentJson.getString("demand"),
                                city = appointmentJson.getString("city"),
                                is_sold = appointmentJson.getString("is_sold"),
                                millage = appointmentJson.getString("milage"),
                                description = appointmentJson.getString("description"),
                                type = appointmentJson.getString("type"),
                                fuelType = appointmentJson.getString("fuel_type"),
                                registered_in = appointmentJson.getString("registered_in"),
                                assembly = appointmentJson.getString("assembly"),
                                enginecapacity = appointmentJson.getString("engine_capacity"),
                                Photos = carPhotosList,
                                sellerPhoneNumber = appointmentJson.getString("phone_number"),
                                secondoryNumber = appointmentJson.getString("secondary_number"),
                                status = appointmentJson.getString("status"),
                                created_at = appointmentJson.getString("created_at"),
                                updated_at = appointmentJson.getString("updated_at"),
                                isInspected = appointmentJson.getBoolean("is_inspected"),
                                addedby = appointmentJson.getString("added_by"),
                            )
                            carList.add(car)
                        }

                        viewModelScope.launch {
                            _cars.value = carList
                        }

                    } catch (e: JSONException) {
                        Log.e("CarListViewModelI", "Error parsing JSON: ${e.message}")
                    }
                } else {
                    Log.e("CarListViewModelI", "API request failed with code: ${response.code}")
                }

                firstApiCompleted = true
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
}*/


/*

class GuestListViewModel : ViewModel() {

    private val _cars = MutableStateFlow<List<guest>>(emptyList())
    val cars: StateFlow<List<guest>> = _cars

    private val _linkedCars = MutableStateFlow<List<LinkedCar>>(emptyList())
    val linkedCars: StateFlow<List<LinkedCar>> = _linkedCars

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var firstApiCompleted = false
    private var secondApiCompleted = false

    fun fetchCarListguest(context: Context) {
        _isLoading.value = true
        firstApiCompleted = false
        secondApiCompleted = false

        val userId = getUserId(context)
        val token = getToken(context)
        val client = OkHttpClient()

        // First API Call
        val firstUrl = "${TestApi.get_guest_car_details}?inspector_id=$userId"
        fetchApiData(client, firstUrl, token, isFirstApi = true)

        // Second API Call
        val secondUrl = "${TestApi.seller_manual_entries}?inspector_id=$userId"
        fetchApiData(client, secondUrl, token, isFirstApi = false)
    }

    private fun fetchApiData(client: OkHttpClient, url: String, token: String?, isFirstApi: Boolean) {
        val request = Request.Builder()
            .url(url)
            .get()
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GuestListViewModel", "API call failed: ${e.message}")
                e.printStackTrace()
                updateApiCompletionState(isFirstApi)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("GuestListViewModel", "Response Code: ${response.code}")
                Log.d("GuestListViewModel", "Response Body: $responseBody")

                if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                    try {
                        when (isFirstApi) {
                            true -> parseGuestCarResponse(responseBody)
                            false -> parseLinkedCarResponse(responseBody)
                        }
                    } catch (e: JSONException) {
                        Log.e("GuestListViewModel", "Error parsing JSON: ${e.message}")
                    }
                } else {
                    Log.e("GuestListViewModel", "API request failed with code: ${response.code}")
                }

                updateApiCompletionState(isFirstApi)
            }
        })
    }

    private fun parseGuestCarResponse(responseBody: String) {
        val carList = mutableListOf<guest>()
        val jsonArray = JSONArray(responseBody)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val photosArray = jsonObject.optJSONArray("car_phots") ?: JSONArray()
            val carPhotosList = mutableListOf<String>()

            for (j in 0 until photosArray.length()) {
                carPhotosList.add(photosArray.getString(j))
            }

            val car = guest(
                saler_car_id = jsonObject.getInt("saler_car_id"),
                user = jsonObject.optInt("user", -1),
                carName = jsonObject.getString("car_name"),
                carCompany = jsonObject.getString("company"),
                color = jsonObject.getString("color"),
                condition = jsonObject.getString("condition"),
                carModel = jsonObject.getString("model"),
                demand = jsonObject.getString("demand"),
                city = jsonObject.getString("city"),
                is_sold = jsonObject.getString("is_sold"),
                millage = jsonObject.getString("milage"),
                description = jsonObject.getString("description"),
                type = jsonObject.getString("type"),
                fuelType = jsonObject.getString("fuel_type"),
                registered_in = jsonObject.getString("registered_in"),
                assembly = jsonObject.getString("assembly"),
                enginecapacity = jsonObject.getString("engine_capacity"),
                Photos = carPhotosList,
                sellerPhoneNumber = jsonObject.getString("phone_number"),
                secondoryNumber = jsonObject.getString("secondary_number"),
                status = jsonObject.getString("status"),
                created_at = jsonObject.getString("created_at"),
                updated_at = jsonObject.getString("updated_at"),
                isInspected = jsonObject.getBoolean("is_inspected"),
                addedby = jsonObject.getString("added_by"),
            )
            carList.add(car)
        }

        viewModelScope.launch {
            _cars.value = carList
        }
    }

    private fun parseLinkedCarResponse(responseBody: String) {
        val linkedCarList = mutableListOf<LinkedCar>()
        val jsonArray = JSONArray(responseBody)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val photosArray = jsonObject.optJSONArray("photos") ?: JSONArray()
            val carPhotosList = mutableListOf<String>()

            for (j in 0 until photosArray.length()) {
                carPhotosList.add(photosArray.getString(j))
            }

            val linkedCar = LinkedCar(
                salerCarId = jsonObject.getInt("saler_car_id"),
                user = jsonObject.getInt("user"),
                guest = jsonObject.optString("guest", null),
                carName = jsonObject.getString("car_name"),
                carCompany = jsonObject.getString("company"),
                color = jsonObject.getString("color"),
                condition = jsonObject.getString("condition"),
                carModel = jsonObject.getString("model"),
                demand = jsonObject.getString("demand"),
                city = jsonObject.getString("city"),
                isSold = jsonObject.getBoolean("is_sold"),
                mileage = jsonObject.getDouble("milage"),
                description = jsonObject.getString("description"),
                type = jsonObject.getString("type"),
                fuelType = jsonObject.getString("fuel_type"),
                registeredIn = jsonObject.getString("registered_in"),
                assembly = jsonObject.getString("assembly"),
                engineCapacity = jsonObject.getString("engine_capacity"),
                photos = carPhotosList,
                sellerPhoneNumber = jsonObject.getString("phone_number"),
                secondaryNumber = jsonObject.getString("secondary_number"),
                status = jsonObject.getString("status"),
                createdAt = jsonObject.getString("created_at"),
                updatedAt = jsonObject.getString("updated_at"),
                isInspected = jsonObject.getBoolean("is_inspected"),
                addedBy = jsonObject.getString("added_by"),
                inspector = jsonObject.getInt("inspector"),
            )
            linkedCarList.add(linkedCar)
        }

        viewModelScope.launch {
            _linkedCars.value = linkedCarList
        }
    }

    private fun updateApiCompletionState(isFirstApi: Boolean) {
        if (isFirstApi) firstApiCompleted = true else secondApiCompleted = true
        if (firstApiCompleted && secondApiCompleted) {
            viewModelScope.launch { _isLoading.value = false }
        }
    }
}

*/

class GuestListViewModel : ViewModel() {

    private val _cars = MutableStateFlow<List<guest>>(emptyList())
    val cars: StateFlow<List<guest>> = _cars

    private val  _linkedCars = MutableStateFlow<List<LinkedCar>>(emptyList())
    val Linkedcars: StateFlow<List<LinkedCar>> =  _linkedCars

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var firstApiCompleted = false
    private var secondApiCompleted = false

    fun fetchCarListguest(context: Context) {
        _isLoading.value = true
        firstApiCompleted = false
        secondApiCompleted = false
        val userId = getUserId(context)
        Log.d("User", "USERIDDDDDD $userId")

        val url = "${TestApi.get_guest_car_details}?inspector_id=$userId"
        val token = getToken(context)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CarListViewModelI", "API call failed: ${e.message}")
                e.printStackTrace()
                firstApiCompleted = true
                checkLoadingState()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("CarListViewModelI", "Response Code: ${response.code}")
                Log.d("CarListViewModelI", "Response Body: $responseBody")

                if (response.isSuccessful) {
                    try {
                        if (responseBody.isNullOrEmpty() || responseBody == "[]") {
                            Log.e("CarListViewModelI", "Empty response received.")
                            viewModelScope.launch {
                                _cars.value = emptyList()
                            }
                            firstApiCompleted = true
                            checkLoadingState()
                            return
                        }

                        val appointmentsArray = JSONArray(responseBody)

                        val carList = mutableListOf<guest>()
                        for (i in 0 until appointmentsArray.length()) {
                            val appointmentJson = appointmentsArray.getJSONObject(i)
                            val photosArray = appointmentJson.optJSONArray("car_phots") ?: JSONArray()
                            val carPhotosList = mutableListOf<String>()

                            for (j in 0 until photosArray.length()) {
                                carPhotosList.add(photosArray.getString(j))
                            }

                            val car = guest(
                                saler_car_id = appointmentJson.getInt("saler_car_id"),
                                user = appointmentJson.optInt("user", -1), // Handle null values safely
                                carName = appointmentJson.getString("car_name"),
                                carCompany = appointmentJson.getString("company"),
                                color = appointmentJson.getString("color"),
                                condition = appointmentJson.getString("condition"),
                                carModel = appointmentJson.getString("model"),
                                demand = appointmentJson.getString("demand"),
                                city = appointmentJson.getString("city"),
                                is_sold = appointmentJson.getString("is_sold"),
                                millage = appointmentJson.getString("milage"),
                                description = appointmentJson.getString("description"),
                                type = appointmentJson.getString("type"),
                                fuelType = appointmentJson.getString("fuel_type"),
                                registered_in = appointmentJson.getString("registered_in"),
                                assembly = appointmentJson.getString("assembly"),
                                enginecapacity = appointmentJson.getString("engine_capacity"),
                                Photos = carPhotosList,
                                sellerPhoneNumber = appointmentJson.getString("phone_number"),
                                secondoryNumber = appointmentJson.getString("secondary_number"),
                                status = appointmentJson.getString("status"),
                                created_at = appointmentJson.getString("created_at"),
                                updated_at = appointmentJson.getString("updated_at"),
                                isInspected = appointmentJson.getBoolean("is_inspected"),
                                addedby = appointmentJson.getString("added_by"),
                            )
                            carList.add(car)
                        }

                        viewModelScope.launch {
                            _cars.value = carList
                        }

                    } catch (e: JSONException) {
                        Log.e("CarListViewModelI", "Error parsing JSON: ${e.message}")
                    }
                } else {
                    Log.e("CarListViewModelI", "API request failed with code: ${response.code}")
                }

                firstApiCompleted = true
                checkLoadingState()
            }
        })
        fetchSecondApi(client, token, context)
    }

    private fun fetchSecondApi(client: OkHttpClient, token: String?, context: Context) {
        val userId = getUserId(context)
        val url2 = "${TestApi.seller_manual_entries}?inspector_id=$userId"

        val request2 = Request.Builder()
            .url(url2)
            .get()
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        client.newCall(request2).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CarListViewModelI", "Second API call failed-2: ${e.message}")
                e.printStackTrace()
                secondApiCompleted = true
                checkLoadingState()
            }
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("fetchLinkedCars", "Response Code: ${response.code}")
                Log.d("fetchLinkedCars", "Response Body: $responseBody")

                if (response.isSuccessful) {
                    try {
                        if (responseBody.isNullOrEmpty()) {
                            Log.e("fetchLinkedCars", "Empty response received.")
                            viewModelScope.launch {
                                _linkedCars.value = emptyList()
                            }
                            secondApiCompleted = true
                            checkLoadingState()
                            return
                        }

                        val jsonResponse = JSONObject(responseBody) // Parse as JSONObject
                        val linkedCarsArray = jsonResponse.getJSONArray("linked_cars") // Extract array
                        val linkedCarList = mutableListOf<LinkedCar>()

                        for (i in 0 until linkedCarsArray.length()) {
                            val jsonCar = linkedCarsArray.getJSONObject(i)

                            val linkedCar = LinkedCar(
                                salerCarId = jsonCar.getInt("saler_car_id"),
                                inspector = jsonCar.getInt("inspector"),
                                user = jsonCar.optInt("user", -1),
                                carName = jsonCar.getString("car_name"),
                                carCompany = jsonCar.getString("company"),
                                color = jsonCar.getString("color"),
                                condition = jsonCar.getString("condition"),
                                carModel = jsonCar.getString("model"),
                                demand = jsonCar.getString("demand"),
                                city = jsonCar.getString("city"),
                                isSold = jsonCar.getBoolean("is_sold"),
                                mileage = jsonCar.getDouble("milage"),
                                description = jsonCar.getString("description"),
                                type = jsonCar.getString("type"),
                                fuelType = jsonCar.getString("fuel_type"),
                                registeredIn = jsonCar.getString("registered_in"),
                                assembly = jsonCar.getString("assembly"),
                                engineCapacity = jsonCar.getString("engine_capacity"),
                                sellerPhoneNumber = jsonCar.getString("phone_number"),
                                secondaryNumber = jsonCar.getString("secondary_number"),
                                status = jsonCar.getString("status"),
                                guest = jsonCar.optString("guest", ""),
                                createdAt = jsonCar.getString("created_at"),
                                updatedAt = jsonCar.getString("updated_at"),
                                isInspected = jsonCar.getBoolean("is_inspected"),
                                photos = jsonCar.optJSONArray("photos")?.let { photosArray ->
                                    List(photosArray.length()) { index -> photosArray.getString(index) }
                                } ?: emptyList(),
                                addedBy = jsonCar.getString("added_by"),
                            )
                            linkedCarList.add(linkedCar)
                        }

                        viewModelScope.launch {
                            _linkedCars.value = linkedCarList
                        }

                    } catch (e: JSONException) {
                        Log.e("fetchLinkedCars", "Error parsing JSON: ${e.message}")
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



data class LinkedCar(
    val salerCarId: Int,
    val user: Int,
    val guest: String?,
    val carName: String,
    val carCompany: String,
    val color: String,
    val condition: String,
    val carModel: String,
    val demand: String,
    val city: String,
    val isSold: Boolean,
    val mileage: Double,
    val description: String,
    val type: String,
    val fuelType: String,
    val registeredIn: String,
    val assembly: String,
    val engineCapacity: String,
    val photos: List<String>,
    val sellerPhoneNumber: String,
    val secondaryNumber: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val isInspected: Boolean,
    val addedBy: String,
    val inspector: Int
)

data class guest(
    val saler_car_id: Int,
    val user: Int,
    val carName: String,
    val carCompany: String,
    val color: String,
    val condition: String,
    val carModel: String,
    val demand: String,
    val city: String,
    val is_sold: String,
    val millage: String,
    val description: String,
    val type: String,
    val fuelType: String,
    val registered_in: String,
    val assembly: String,
    val enginecapacity: String,
    val Photos: List<String>,
    val sellerPhoneNumber: String,
    val secondoryNumber: String,
    val status: String,
    val created_at: String,
    val updated_at: String,
    val isInspected: Boolean,
    val addedby: String,



    )
