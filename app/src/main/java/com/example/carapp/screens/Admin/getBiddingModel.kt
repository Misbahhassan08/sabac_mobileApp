package com.example.carapp.screens.Admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.Apis.TestApi
import com.example.carapp.models.Live
import com.example.carapp.models.Seller
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class LiveViewModel : ViewModel() {

    private val _assignedSlots = MutableStateFlow<List<Live>>(emptyList())
    val assignedSlots: StateFlow<List<Live>> = _assignedSlots

    fun fetchLiveCars(token: String?) {
        val client = OkHttpClient()
        val url2 = TestApi.get_bidding_cars

        val request2 = Request.Builder()
            .url(url2)
            .get()
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        client.newCall(request2).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("SecondApi", "Second API call failed: ${e.message}")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("SecondApi2", "Second API Response Code: ${response.code}")
                Log.d("SecondApi2", "Second API Response Body: $responseBody")

                if (response.isSuccessful) {
                    try {
                        responseBody?.let {
                            val jsonObject = JSONObject(it)
                            val carsArray = jsonObject.getJSONArray("cars")

                            val assignedSlotsList = mutableListOf<com.example.carapp.models.Live>()

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

                                val slot = com.example.carapp.models.Live(
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
                                    primaryPhoneNumber = if (carJson.isNull("primary_phone_number")) null else carJson.getString(
                                        "primary_phone_number"
                                    ),
                                    secondaryPhoneNumber = if (carJson.isNull("secondary_phone_number")) null else carJson.getString(
                                        "secondary_phone_number"
                                    ),
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
            }
        })
    }
}
